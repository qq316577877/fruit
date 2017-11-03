/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.tools;

import com.fruit.base.file.upload.aliyun.client.UploadRequest;
import com.fruit.base.file.upload.aliyun.client.UploadResponse;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.FileUploadRequest;
import com.fruit.sys.admin.service.UploadService;
import com.fruit.sys.admin.utils.BizConstants;
import com.fruit.sys.admin.utils.PinyinUtils;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : points-biz-api
 * File Name      : FileUploadUtils.java
 */
public class FileUploadBasicAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadBasicAction.class);

    private static final int SIZE_THRESHOLD = 5 * 1024 * 1024;

    public final static String FILE_UPLOAD_INVALIDTYPE = "file.upload.invalidtype";

    public final static String FILE_UPLOAD_EXCEEDED = "file.upload.exceeded";

    public final static String FILE_UPLOAD_ERROR = "file.upload.error";

    public final static String FILE_UPLOAD_INTERRUPTED = "file.upload.interrupted";

    @Autowired
    private UploadService uploadService;

    protected FileUploadRequest doUpload(String tempFile, String localFileDir, int sysId) throws Exception
    {
        FileUploadRequest uploadRequest = new FileUploadRequest();
        List<String> downloadUrl = new ArrayList<String>();
        String errorMessage = "";
        boolean isMultipart = ServletFileUpload.isMultipartContent(WebContext.getRequest());
        uploadRequest.setMultipart(isMultipart);
        if (isMultipart)
        {
            FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(WebContext.getServletContext());
            DiskFileItemFactory factory = new DiskFileItemFactory(
                    DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,
                    new File(tempFile));
            factory.setFileCleaningTracker(fileCleaningTracker);
            factory.setSizeThreshold(SIZE_THRESHOLD);
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 为了能够获取request中的数据，这里先不做大小约束
            upload.setSizeMax(-1);
            upload.setHeaderEncoding(BizConstants.UTF_8);
            HttpServletRequest request = WebContext.getRequest();
            try
            {
                List<FileItem> filelist = upload.parseRequest(request);
                for (Iterator<FileItem> it = filelist.iterator(); it.hasNext(); )
                {
                    FileItem item = it.next();
                    if (!item.isFormField())
                    {
                        if (item.getSize() > SIZE_THRESHOLD)
                        {
                            LOGGER.error("[FILE UPLOAD] current size: {}; exceeded max size: {}", item.getSize(), SIZE_THRESHOLD);
                            errorMessage = super.i18n("file.upload.exceeded", item.getSize() / 1024.0 / 1024.0, SIZE_THRESHOLD / 1024.0 / 1024.0);
                            throw new IllegalArgumentException(errorMessage);
                        }
                        uploadRequest.setContents(item.get());
                        uploadRequest.setFileName(item.getName());
                        UploadResponse uploadResponse = this.uploadToServer(sysId, false,item);
                        if (uploadResponse.isSuccessful())
                        {
                            downloadUrl.add(uploadResponse.getPath());
                        }
                    }
                    else
                    {
                        String fieldName = item.getFieldName();
                        String value = item.getString("utf8");
//                        value = HtmlUtils.htmlEscape(value);
                        uploadRequest.getFormParameters().put(fieldName, value);
                    }
                }
                uploadRequest.setUrlPath(downloadUrl);
                if (null != uploadRequest.getContents())
                {
                    saveUploadFile(localFileDir, uploadRequest);
                }
            }
            catch (FileUploadException e)
            {
                if (e instanceof FileUploadBase.FileSizeLimitExceededException)
                {
                    FileUploadBase.FileSizeLimitExceededException sizeEx = (FileUploadBase.FileSizeLimitExceededException) e;
                    LOGGER.error("[FILE UPLOAD] current size: {}; exceeded max size: {}", sizeEx.getActualSize(),
                            sizeEx.getPermittedSize());
                    errorMessage = super.i18n(FILE_UPLOAD_EXCEEDED,
                            sizeEx.getActualSize() / 1024.0 / 1024.0,
                            sizeEx.getPermittedSize() / 1024.0 / 1024.0);
                }
                else if (e instanceof FileUploadBase.SizeLimitExceededException)
                {
                    FileUploadBase.SizeLimitExceededException sizeEx = (FileUploadBase.SizeLimitExceededException) e;
                    LOGGER.error("[FILE UPLOAD] current size: {}; exceeded max size: {}", sizeEx.getActualSize(),
                            sizeEx.getPermittedSize());
                    errorMessage =
                            super.i18n("file.upload.0x1", sizeEx.getActualSize() / 1024.0 / 1024.0,
                                    sizeEx.getPermittedSize() / 1024.0 / 1024.0);
                }
                else if (e instanceof FileUploadBase.InvalidContentTypeException)
                {
                    FileUploadBase.InvalidContentTypeException contentEx = (FileUploadBase.InvalidContentTypeException) e;
                    LOGGER.error("[FILE UPLOAD] content type is not allowed: {}", contentEx);
                    errorMessage = super.i18n(FILE_UPLOAD_INVALIDTYPE);
                }
                else
                {
                    LOGGER.error("[FILE UPLOAD] parse request failure: {}", e);
                    errorMessage = super.i18n(FILE_UPLOAD_ERROR);
                }
                LOGGER.error(errorMessage, e);
            }
            catch (Exception e)
            {
                LOGGER.error("[FILE UPLOAD] failure:", e);
                errorMessage = FILE_UPLOAD_INTERRUPTED;
                throw e;
            }
        }
        return uploadRequest;
    }

    private void saveUploadFile(String localFileDir, FileUploadRequest request)
    {
        if (null != localFileDir)
        {
            File localDir = new File(localFileDir);
            if (localDir.exists() && localDir.isDirectory())
            {
                String newFileName = localDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + "_" + request.getFileName();
                OutputStream out = null;
                try
                {
                    out = new FileOutputStream(newFileName);
                    out.write(request.getContents());
                    request.setLocalFilePath(newFileName);
                }
                catch (IOException e)
                {
                    LOGGER.error("保存文件=" + request.getFileName() + "失败!", e);
                }
                finally
                {
                    IOUtils.closeQuietly(out);
                }
            }
        }

    }
    private UploadResponse uploadToServer(int userId, boolean open, FileItem item)
    {
        String fileName = item.getName();
        fileName = PinyinUtils.getPinYin(fileName);
        int index = fileName.lastIndexOf('.');
        Validate.isTrue(index > 0 && index < fileName.length() - 1, "文件名错误!");
        String fileExtension = fileName.substring(index + 1);
        byte[] contents = item.get();
        UploadRequest uploadRequest = new UploadRequest();
        uploadRequest.setContents(contents);
        uploadRequest.setOpen(open);
        uploadRequest.setUserId(userId);
        uploadRequest.setFileName(fileName);
        uploadRequest.setViewInPage(true);
        uploadRequest.setFileExtension(fileExtension);
        UploadResponse uploadResponse = this.uploadService.upload(uploadRequest);
        return uploadResponse;
    }
}
