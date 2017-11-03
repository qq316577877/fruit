package com.fruit.sys.admin.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.im4java.core.ConvertCmd;
import org.im4java.core.GMOperation;
import org.im4java.process.ProcessStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Created by wills on 15/7/8.
 */
public class FtpUploadUtils
{

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpUploadUtils.class);

    private static final String CONFIG_KEY_IP = "to-operation-web.static.ftp.ip";

    private static final String CONFIG_KEY_USER = "to-operation-web.static.ftp.username";

    private static final String CONFIG_KEY_PASSWORD = "to-operation-web.static.ftp.password";

    private static final String CONFIG_KEY_GM_PATH = "to-operation-web.gm.path";

    private static final String BAK_SUFFIX = ".bak";

    private static String LOCAL_CHARSET = "UTF-8";

    private static String SERVER_CHARSET = "ISO-8859-1";

    private FTPClient ftpClient;

    private FtpUploadUtils()
    {
    }

    private static class SingletonHolder
    {

        private static FtpUploadUtils ftpUploadUtils = new FtpUploadUtils();
    }

    public static FtpUploadUtils instance()
    {
        return SingletonHolder.ftpUploadUtils;
    }

    /**
     * 替换或新增一个新的文件
     */
    public void addFile(String destFilePath, String fileName, String fileType, boolean shouldFilter, InputStream inputStream) throws IOException
    {

        fileName = new String(fileName.getBytes(LOCAL_CHARSET), SERVER_CHARSET);

        reConnect();

        buildFolder(destFilePath);
        changeWorkingDirectory(ftpClient, destFilePath);
        List<String> fileNames = Arrays.asList(this.ftpClient.listNames());

        if (fileNames.contains(fileName))
        {
            copyFile(fileName, fileName + BAK_SUFFIX, ftpClient);
        }

        if (shouldFilter)
        {

            inputStream = filterRedundantData(inputStream, "." + fileType);
        }
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        this.ftpClient.storeFile(fileName, new BufferedInputStream(inputStream));
    }

    private InputStream filterRedundantData(InputStream inputStream, String endFix) throws IOException
    {
        UUID uuid = UUID.randomUUID();
        String gmPath = (
                "/usr/local/GraphicsMagick/bin");
        String tempFileName = "/tmp/" + uuid.toString() + endFix;
        ProcessStarter.setGlobalSearchPath(gmPath);
        ConvertCmd cmd = new ConvertCmd();
        GMOperation operation = new GMOperation();
        File tmp = new File(tempFileName);
        try
        {
            tmp.createNewFile();
            OutputStream outStream = new FileOutputStream(tmp);
            IOUtils.copy(inputStream, outStream);
            operation.addImage(tempFileName);
            operation.p_profile("*");
            operation.addImage(tempFileName);
            cmd.run(operation);
            return FileUtils.openInputStream(tmp);
        }
        catch (Exception e)
        {
            LOGGER.error("Run GraphicsMagick Error! ", e);
            throw new IOException("图片文件清除冗余信息失败. ");
        }
    }

    /**
     * 重连
     */
    private void reConnect()
    {
        String ftpIp = "";
        String userName = "";
        String password = "";

        ftpClient = new FTPClient();
        try
        {
            ftpClient.connect(ftpIp);
            ftpClient.login(userName, password);
        }
        catch (IOException e)
        {
            LOGGER.error("connect ftp error :", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 切换FTP路径
     *
     * @param client FtpClient
     * @param path   切换到的路径
     * @throws IOException
     */
    private void changeWorkingDirectory(FTPClient client, String path) throws IOException
    {
        if (!client.changeWorkingDirectory(path))
        {
            client.mkd(path);
            client.changeWorkingDirectory(path);
        }
    }

    /**
     * 拷贝文件
     *
     * @param source from
     * @param dest   to
     * @param client FtpClient
     * @throws IOException
     */
    private void copyFile(String source, String dest, FTPClient client) throws IOException
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        client.retrieveFile(source, output);
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        client.storeFile(dest, input);
    }

    /**
     * 创建文件上传之前需要创建的目录结构
     *
     * @param path 上传到FTP的目录结构
     * @throws IOException
     */
    private void buildFolder(String path) throws IOException
    {
        if (StringUtils.isEmpty((path)))
        {
            return;
        }
        if (!path.startsWith("/"))
        {
            throw new IOException(String.format("Illegal path:%s, at start ", path));
        }
        int position = 0, nextPosition;
        nextPosition = path.indexOf('/', position + 1);
        while (nextPosition != -1)
        {
            if (nextPosition - position == 1)
            {
                throw new IOException(String.format("Illegal path:%s, at %s", path,
                        nextPosition));
            }
            changeWorkingDirectory(this.ftpClient, path.substring(0, nextPosition + 1));
            position = nextPosition;
            nextPosition = path.indexOf('/', position + 1);
        }

    }

    /**
     * 获取指定目录下的所有文件路径
     */
    public List<String> getFtpFilePath(String rootPath)
    {
        List<String> resultFilePath = new ArrayList<String>();
        try
        {
            reConnect();
            if (!ftpClient.changeWorkingDirectory(rootPath))
            {
                throw new IOException();
            }
            ftpFilePathWalker(rootPath, resultFilePath);
        }
        catch (IOException e)
        {
            resultFilePath = Collections.emptyList();
        }

        return resultFilePath;
    }

    /**
     * 遍历一个目录下的所有文件路径
     *
     * @param rootPath
     * @param resultFilePath
     * @throws IOException
     */
    private void ftpFilePathWalker(String rootPath, List<String> resultFilePath) throws IOException
    {
        changeWorkingDirectory(ftpClient, rootPath);
        FTPFile[] ftpFiles = ftpClient.listFiles();
        for (FTPFile ftpFile : ftpFiles)
        {
            if (ftpFile.isDirectory())
            {
                resultFilePath.add(rootPath + ftpFile.getName() + "/");
                ftpFilePathWalker(rootPath + ftpFile.getName() + "/", resultFilePath);
            }
            else
            {
                resultFilePath.add(rootPath + ftpFile.getName());
            }
        }
    }
}
