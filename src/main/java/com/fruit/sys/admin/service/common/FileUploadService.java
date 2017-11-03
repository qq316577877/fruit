/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.service.common;

import com.fruit.base.file.upload.aliyun.client.FileUploadClient;
import com.fruit.base.file.upload.aliyun.client.UploadRequest;
import com.fruit.base.file.upload.aliyun.client.UploadResponse;
import com.fruit.loan.biz.https.util.UUIDUtils;
import com.fruit.sys.admin.service.BaseService;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.utils.BizConstants;
import com.fruit.sys.admin.utils.BizUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1511-17
 * Project        : fruit
 * File Name      : UploadService.java
 */
@Service
public class FileUploadService extends BaseService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadService.class);

    private static final String DEFUALT_PIC = "1-noimage-20151231170402709-00002489536d.jpg";

    private static final String OPEN_PICTURE_DOMAIN = "http://picture.fruit.com/";

    private static final String CAPTCHA_IMG_FOLDER = "gcaptcha";

    @Autowired
    private EnvService envService;

    @Autowired
    private FileUploadClient fileUploadClient;

    private String uploadDisk = "/data/appdatas/ovfintech";

    private String nginxFolder = "/fruitupload";

    private String uploadFolder = uploadDisk + nginxFolder;

    @PostConstruct
    public void init()
    {
        if (null != this.envService)
        {
            String config = this.envService.getConfig("file.upload.disk");
            Validate.notEmpty(config, "Global configure 'file.upload.disk' not found.");
            this.uploadDisk = config;

            config = this.envService.getConfig("file.upload.nginx.folder");
            Validate.notEmpty(config, "Global configure 'file.upload.nginx.folder' not found.");
            this.nginxFolder = config;

            uploadFolder = uploadDisk + nginxFolder;
        }
    }

    public UploadResponse uploadFile(UploadRequest request)
    {
//        return this.fileUploadClient.upload(request); // Service层隔离是否采用阿里云
        UploadResponse response = new UploadResponse();
        String path = this.getUploadedFilename(request.getUserId(), request.getFileName());
        File file = this.getUploadFile(path);
        this.saveFileContent(request, response, file);
        if (response.isSuccessful())
        {
            response.setPath(path);
            response.setUrl(this.buildDiskUrl(path));
            this.fileUploadClient.saveUploadRecord(request, path);
        }
        return response;
    }

    public UploadResponse uploadCaptchaImg(UploadRequest request)
    {
        UploadResponse response = new UploadResponse();
        String path = this.getUploadedCaptchaFilename(request.getFileName());
        File file = this.getUploadFile(path);
        this.saveFileContent(request, response, file);
        if (response.isSuccessful())
        {
            response.setPath(path);
            response.setUrl(this.buildDiskUrl(path));
        }
        return response;
    }

    public String buildDiskUrl(String path)
    {
        return "http://" + this.envService.getPortalDomain() + path;
    }

    public String buildAliyunFileUrl(String key, boolean open)
    {
        String url = "";
        if (StringUtils.isNotBlank(key))
        {
            if (key.startsWith("http://") || key.startsWith("https://"))
            {
                url = key;
            }
            else if (key.startsWith("//"))
            {
                url = "http:" + key;
            }
            else
            {
                if (open)
                {
                    url = OPEN_PICTURE_DOMAIN + key;
                }
                else
                {
                    url = this.fileUploadClient.generateUrl(key, open);
                    url = url.replace("0ku-private-pictures.oss.aliyuncs.com", "private.0ku.com");
                }
            }
        }
        else
        {
            url = OPEN_PICTURE_DOMAIN + DEFUALT_PIC;
        }
        return url;
    }

    private void saveFileContent(UploadRequest request, UploadResponse response, File file)
    {
        FileOutputStream fos = null;
        try
        {
            fos =new FileOutputStream(file);
            IOUtils.write(request.getContents(), fos);
            response.setSuccessful(true);
        }
        catch (Exception e)
        {
            LOGGER.error("[FILE SAVE] parse request failure: {}", e);
            response.setSuccessful(false);
            response.setErrorMessage("系统错误");
        }
        finally
        {
            IOUtils.closeQuietly(fos);
        }
    }

    /**
     * 获取上传文件的文件名（包含nginx代理目录名）
     *
     * @param sysId
     * @param filename
     * @return
     */
    private String getUploadedFilename(int sysId, String filename)
    {
        Date date = new Date();
        String[] path2MonthDir = {this.nginxFolder, "sys-" + sysId, DateFormatUtils.format(date, "yyyyMM")};
        String monthDirPath = StringUtils.join(path2MonthDir, BizConstants.SLASH);
        String dateTime = DateFormatUtils.format(date, "yyyyMMddHHmmss-");
//        String baseName = FilenameUtils.getBaseName(filename);
        String baseName = UUIDUtils.getUUID();//为防止文件名中含有特殊符号，不再使用其本身上传的文件名
        String extension = FilenameUtils.getExtension(filename);
        Validate.isTrue(StringUtils.isNotBlank(extension), "文件名错误!");
        String newFileName = monthDirPath + BizConstants.SLASH + dateTime + Integer.toString(BizUtils.random6Int()) + "-" + baseName;
        newFileName = StringUtils.abbreviate(newFileName, 128 - 1 - extension.length());
        return newFileName + "." + extension;
    }

    private String getUploadedCaptchaFilename(String filename)
    {
        Date date = new Date();
        String[] path2DayDir = {this.nginxFolder, CAPTCHA_IMG_FOLDER, DateFormatUtils.format(date, "yyyy-MM-dd")};
        String path2DayPath = StringUtils.join(path2DayDir, BizConstants.SLASH);
        return path2DayPath + BizConstants.SLASH + filename;
    }
    /**
     * 获取上传文件的绝对存储路径
     *
     * @param uploadedFilename
     * @return
     */
    private File getUploadFile(String uploadedFilename)
    {
        File file = new File(this.getDiskDirectory().getAbsolutePath() + BizConstants.SLASH + uploadedFilename);
        File parentFile = file.getParentFile();
        if (null != parentFile && !parentFile.exists())
        {
            parentFile.mkdirs();
        }
        return file;
    }

    private File getDiskDirectory()
    {
        File directory = new File(this.uploadDisk);
        if (!directory.exists())
        {
            directory.mkdirs();
        }
        return directory;
    }

    public File getUploadFolder()
    {
        File directory = new File(this.uploadFolder);
        if (!directory.exists())
        {
            directory.mkdirs();
        }
        return directory;
    }
}
