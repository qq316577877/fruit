/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.action.tools;

import com.fruit.base.file.upload.aliyun.client.UploadRequest;
import com.fruit.base.file.upload.aliyun.client.UploadResponse;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.JsonResult;
import com.fruit.sys.admin.service.common.FileUploadService;
import com.fruit.sys.admin.utils.BizUtils;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传相关
 */
@Component
@UriMapping("/file")
public class FileUploadAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadAction.class);

    private static final int SIZE_THRESHOLD = 5 * 1024 * 1024;

    public final static String FILE_UPLOAD_INVALIDTYPE = "file.upload.invalidtype";

    public final static String FILE_UPLOAD_EXCEEDED = "file.upload.exceeded";

    public final static String FILE_UPLOAD_ERROR = "file.upload.error";

    public final static String FILE_UPLOAD_INTERRUPTED = "file.upload.interrupted";

    public final static String FILE_UPLOAD_TEMP_DIR = "/data/appdatas/upload/sys-";

    @Autowired
    private FileUploadService fileUploadService;

    @UriMapping(value = "/upload_private_ajax")
    public JsonResult uploadPrivatePicture()
    {
        return this.upload(false);
    }

    @UriMapping(value = "/upload_public_ajax")
    public JsonResult uploadPublicPicture()
    {
        return this.upload(true);
    }
    
    protected JsonResult upload(boolean open)
    {
        String msg = "success";
        boolean successful = false;
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try
        {
            int sysId = super.getLoginUserId();
            FileItem fileItem = this.getSingleFileItem(sysId, SIZE_THRESHOLD);
            UploadResponse uploadResponse = this.doUpload(sysId, open, fileItem);

            successful = uploadResponse.isSuccessful();
            if (successful)
            {
                dataMap.put("url", uploadResponse.getUrl());
                dataMap.put("path", uploadResponse.getPath());
            }
            else
            {
                msg = uploadResponse.getErrorMessage();
                successful = false;
            }
        }
        catch (IllegalArgumentException e)
        {
            successful = false;
            msg = e.getMessage();
        }
        catch (RuntimeException e)
        {
            LOGGER.error("[uploadPicture] fail to uploadPrivatePicture", e);
            successful = false;
            msg = "系统繁忙";
        }

        dataMap.put("successful", successful ? 1 : 0);
        JsonResult result = new JsonResult();
        result.setResult(successful);
        result.setMsg(msg);
        result.setDatas(dataMap);
        return result;
    }

    private UploadResponse doUpload(int sysId, boolean open, FileItem item)
    {
        String filename = item.getName();
        String extension = FilenameUtils.getExtension(filename);
        Validate.isTrue(StringUtils.isNotBlank(extension), "文件名错误!");
        byte[] contents = item.get();

        UploadRequest uploadRequest = new UploadRequest();
        uploadRequest.setContents(contents);
        uploadRequest.setOpen(open);
        uploadRequest.setUserId(sysId);
        uploadRequest.setFileName(filename);
        uploadRequest.setFileExtension(extension);
        uploadRequest.setViewInPage(true);
        UploadResponse response = this.fileUploadService.uploadFile(uploadRequest);
        return response;
    }

    private FileItem getSingleFileItem(int sysId, long maxSize)
    {
        try
        {
            HttpServletRequest request = WebContext.getRequest();
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            Validate.isTrue(isMultipart, "参数错误");
            FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(WebContext.getServletContext());
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setFileCleaningTracker(fileCleaningTracker);
            factory.setSizeThreshold(SIZE_THRESHOLD);
            factory.setRepository(new File(this.getTempFilePath(sysId)));
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 为了能够获取request中的数据，这里先不做大小约束
            upload.setSizeMax(-1);
            upload.setHeaderEncoding(BizUtils.UTF_8);

            List<FileItem> filelist = upload.parseRequest(request);
            for (FileItem item : filelist)
            {
                if (item.isFormField())
                {
                    String fieldName = item.getFieldName();
                    String value = item.getString();
                    LOGGER.info("[upload] upload item, {}: {}" , fieldName, value);
                }
                else
                {
                    if (item.getSize() > maxSize)
                    {
                        LOGGER.error("[FILE UPLOAD] current size: {}; exceeded max size: {}", item.getSize(), maxSize);
                        String errorMessage = super.i18n("file.upload.exceeded", item.getSize() / 1024.0 / 1024.0, maxSize / 1024.0 / 1024.0);
                        throw new IllegalArgumentException(errorMessage);
                    }
                    return item;
                }
            }
        }
        catch (FileUploadException e)
        {
            if (e instanceof FileUploadBase.FileSizeLimitExceededException)
            {
                FileUploadBase.FileSizeLimitExceededException sizeEx = (FileUploadBase.FileSizeLimitExceededException) e;
                LOGGER.error("[FILE UPLOAD] current size: {}; exceeded max size: {}", sizeEx.getActualSize(), sizeEx.getPermittedSize());
                String errorMessage = super.i18n(FILE_UPLOAD_EXCEEDED, sizeEx.getActualSize() / 1024.0 / 1024.0, sizeEx.getPermittedSize() / 1024.0 / 1024.0);
                throw new IllegalArgumentException(errorMessage);
            }
            else if (e instanceof FileUploadBase.SizeLimitExceededException)
            {
                FileUploadBase.SizeLimitExceededException sizeEx = (FileUploadBase.SizeLimitExceededException) e;
                LOGGER.error("[FILE UPLOAD] current size: {}; exceeded max size: {}", sizeEx.getActualSize(), sizeEx.getPermittedSize());
                String errorMessage = super.i18n("file.upload.0x1", sizeEx.getActualSize() / 1024.0 / 1024.0, sizeEx.getPermittedSize() / 1024.0 / 1024.0);
                throw new IllegalArgumentException(errorMessage);
            }
            else if (e instanceof FileUploadBase.InvalidContentTypeException)
            {
                FileUploadBase.InvalidContentTypeException contentEx = (FileUploadBase.InvalidContentTypeException) e;
                LOGGER.error("[FILE UPLOAD] content type is not allowed: {}", contentEx);
                throw new IllegalArgumentException(super.i18n(FILE_UPLOAD_INVALIDTYPE));
            }
            else
            {
                LOGGER.error("[FILE UPLOAD] parse request failure: {}", e);
                throw new IllegalArgumentException(super.i18n(FILE_UPLOAD_ERROR));
            }
        }
        catch (Exception e)
        {
            LOGGER.error("[FILE UPLOAD] failure:", e);
            throw new IllegalArgumentException(super.i18n(FILE_UPLOAD_INTERRUPTED));
        }
        return null;
    }

    private String getTempFilePath(int sysId)
    {
        return FILE_UPLOAD_TEMP_DIR + sysId + "-" + BizUtils.getUUID();
    }

}
