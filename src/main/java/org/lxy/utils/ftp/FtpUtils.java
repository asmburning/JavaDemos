package org.lxy.utils.ftp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric.Liu on 2016/11/10.
 */
@Slf4j
public class FtpUtils {

    /**
     * 获取ftp连接
     *
     * @param host     host
     * @param port     port
     * @param user     user
     * @param password password
     * @return FTPClient if success , null if false
     */
    public static FTPClient connectFtp(String host, int port, String user, String password) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(host, port);
            if (ftpClient.login(user, password)) {
                return ftpClient;
            }
        } catch (Exception e) {
            log.error("failed to connect to ftp server with host : {} , port : {} , user : {} , password :{} ",
                    host, port, user, password, e);
        }
        return null;
    }

    /**
     * 关闭ftp连接
     *
     * @param ftpClient ftpClient
     */
    public static void closeFtpClient(FTPClient ftpClient) {
        if (ftpClient != null) {
            try {
                ftpClient.disconnect();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 更改ftp目录
     *
     * @param ftpClient ftpClient
     * @param path      path
     * @return true if change success , false if not
     */
    public static boolean changePath(FTPClient ftpClient, String path) {
        try {
            ftpClient.changeWorkingDirectory(path);
            return true;
        } catch (Exception e) {
            log.error("failed to changeWorkingDirectory with path : {} ", path, e);
        }
        return false;
    }

    /**
     * 创建ftp文件夹
     *
     * @param ftpClient ftpClient
     * @param dir       dir
     * @return true is success , false if not
     */
    public static boolean createFtpDir(FTPClient ftpClient, String dir) {
        try {
            ftpClient.makeDirectory(dir);
            return true;
        } catch (Exception e) {
            log.error("failed to create dir : {} ", dir, e);
        }
        return false;
    }

    /**
     * 获取当前目录下的所有文件名
     *
     * @param ftpClient ftpClient
     * @return fileNames if success , null if not
     */
    public static String[] getFileNames(FTPClient ftpClient) {
        String[] fileNames = null;
        try {
            fileNames = ftpClient.listNames();
        } catch (Exception e) {
            log.error("failed to getFileNames ", e);
        }
        return fileNames;
    }

    /**
     * 批量上次文件
     *
     * @param ftpClient ftpClient
     * @param fileVos   fileVos
     * @return transferResults
     */
    public static List<TransferResult> uploadFiles(FTPClient ftpClient, FileVo... fileVos) {
        List<TransferResult> transferResults = new ArrayList<>(fileVos.length);
        for (FileVo fileVo : fileVos) {
            transferResults.add(uploadFile(ftpClient, fileVo));
        }
        return transferResults;
    }

    /**
     * 上次文件
     *
     * @param ftpClient ftpClient
     * @param fileVo    fileVo
     * @return TransferResult
     */
    public static TransferResult uploadFile(FTPClient ftpClient, FileVo fileVo) {
        InputStream inputStream = null;
        String message = "";
        if (!ftpClient.isConnected()) {
            message = "ftp is not connected";
            return new TransferResult(fileVo, message);
        }
        try {
            inputStream = new FileInputStream(fileVo.getLocalFilePath());
            ftpClient.storeFile(fileVo.getRemoteName(), inputStream);
            log.info("upload file success with fileVo : {} ", fileVo);
            return new TransferResult(fileVo);
        } catch (Exception e) {
            message = e.getMessage();
            log.error("failed to uploadFile fileVo:{}", fileVo, e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return new TransferResult(fileVo, message);
    }

    /**
     * 批量下载文件
     *
     * @param ftpClient ftpClient
     * @param fileVos   fileVos
     * @return transferResults
     */
    public static List<TransferResult> downloadFiles(FTPClient ftpClient, FileVo... fileVos) {
        List<TransferResult> transferResults = new ArrayList<>(fileVos.length);
        for (FileVo fileVo : fileVos) {
            transferResults.add(downloadFile(ftpClient, fileVo));
        }
        return transferResults;
    }

    /**
     * 下载文件
     *
     * @param ftpClient ftpClient
     * @param fileVo    fileVo
     * @return TransferResult
     */
    public static TransferResult downloadFile(FTPClient ftpClient, FileVo fileVo) {
        OutputStream outputStream = null;
        String message = "";
        if (!ftpClient.isConnected()) {
            message = "ftp is not connected";
            return new TransferResult(fileVo, message);
        }
        try {
            outputStream = new FileOutputStream(fileVo.getLocalFilePath() + "/" + fileVo.getLocalName());
            ftpClient.retrieveFile(fileVo.getRemoteName(), outputStream);
            log.info("downloadFile success with fileVo : {} ", fileVo);
            return new TransferResult(fileVo);
        } catch (Exception e) {
            message = e.getMessage();
            log.error("failed to downloadFile with fileVo : {} ", fileVo, e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
        return new TransferResult(fileVo, message);
    }
}
