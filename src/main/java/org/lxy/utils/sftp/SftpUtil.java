package org.lxy.utils.sftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.lxy.utils.ftp.FileVo;
import org.lxy.utils.ftp.TransferResult;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by xinyi.liu on 2016/9/6.
 */
@Slf4j
public class SftpUtil {

    public static ChannelSftp connect(String host, int port, String username, String password) {
        try {
            JSch jSch = new JSch();
            Session session = jSch.getSession(username, host, port);
            session.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(sshConfig);
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            return (ChannelSftp) channel;
        } catch (Exception e) {
            log.error("failed to connect sftp", e);
            return new ChannelSftp();
        }
    }

    public static void disconnect(ChannelSftp channelSftp) {
        if (channelSftp != null && channelSftp.isConnected()) {
            channelSftp.disconnect();
        }
    }

    /**
     * 创建sftp目录
     *
     * @param channelSftp
     * @param dir
     * @return
     * @throws
     */
    public static boolean mkSftpDir(ChannelSftp channelSftp, String dir) {
        if (dir.startsWith("/")) {
            dir = dir.substring(1);
        }
        if (dir.endsWith("/")) {
            dir = dir.substring(0, dir.length() - 1);
        }
        boolean result = false;
        if (StringUtils.isBlank(dir)) {
            return result; //不需要创建目录直接返回
        }
        try {
            channelSftp.cd(dir);
            return true;
        } catch (Exception e) {
            try {
                channelSftp.mkdir(dir);
                channelSftp.cd(dir);
                result = true;
            } catch (Exception e1) {
                log.error("failed to mkDir : {} ", dir, e);
            }
        }
        return result;
    }

    public static boolean changeDir(ChannelSftp channelSftp, String dir) {
        try {
            channelSftp.cd(dir);
            return true;
        } catch (Exception e) {
            log.error("failed to changeDir : {} ", dir, e);
        }
        return false;
    }

    /**
     * 获取当前目录下的所有的文件名(不包含文件夹)
     *
     * @param channelSftp channelSftp
     * @return fileNames if no such file empty List
     */
    public static List<String> getFileNames(ChannelSftp channelSftp) {
        return getFileNames(channelSftp, "");
    }

    /**
     * 获得所有特定后缀的文件名
     *
     * @param channelSftp channelSftp
     * @param extension   extension eg: .txt  .xls
     * @return fileNames
     */
    public static List<String> getFileNames(ChannelSftp channelSftp, String extension) {
        List<String> fileNames = new ArrayList<>();
        Vector vector = null;
        try {
            vector = channelSftp.ls("*" + extension);
            if (CollectionUtils.isEmpty(vector)) {
                log.info("没有符合条件的文件 : {} ", extension);
                return fileNames;
            }
            for (int i = 0; i < vector.size(); i++) {
                String lsResult = vector.get(i).toString();
                if (lsResult.indexOf(".") > 0) {
                    String fileName = lsResult.substring(lsResult.lastIndexOf(" ") + 1);
                    log.info("vector String : {} , fileName : {} ", lsResult, fileName);
                    fileNames.add(fileName);
                }
            }
        } catch (Exception e) {
            log.error("failed to get fileNames ", e);
        }
        return fileNames;
    }

    /**
     * 批量下载文件
     *
     * @param channelSftp channelSftp
     * @param fileVos     fileVos
     * @return transferResults
     */
    public static List<TransferResult> downloadFiles(ChannelSftp channelSftp, FileVo... fileVos) {
        List<TransferResult> transferResults = new ArrayList<>();
        for (FileVo fileVo : fileVos) {
            transferResults.add(downloadFile(channelSftp, fileVo));
        }
        return transferResults;
    }

    /**
     * 单个下载文件
     *
     * @param channelSftp channelSftp
     * @param fileVo      fileVo
     * @return
     */
    public static TransferResult downloadFile(ChannelSftp channelSftp, FileVo fileVo) {
        String message = "";
        try (OutputStream outputStream = new FileOutputStream(fileVo.getLocalFilePath())) {
            channelSftp.get(fileVo.getRemoteName());
            return new TransferResult(fileVo);
        } catch (Exception e) {
            message = e.getMessage();
            log.error("failed to downloadFile : {} ", fileVo, e);
        }
        return new TransferResult(fileVo, message);
    }

    /**
     * 批量上传文件
     *
     * @param channelSftp channelSftp
     * @param fileVos     fileVos
     * @return transferResults
     */
    public static List<TransferResult> uploadFiles(ChannelSftp channelSftp, FileVo... fileVos) {
        List<TransferResult> transferResults = new ArrayList<>();
        for (FileVo fileVo : fileVos) {
            transferResults.add(uploadFile(channelSftp, fileVo));
        }
        return transferResults;
    }

    /**
     * 单个上传文件
     *
     * @param channelSftp channelSftp
     * @param fileVo      fileVo
     * @return transferResult
     */
    public static TransferResult uploadFile(ChannelSftp channelSftp, FileVo fileVo) {
        String message = "";
        try (InputStream inputStream = new FileInputStream(fileVo.getLocalFilePath())) {
            channelSftp.put(inputStream, fileVo.getRemoteName());
            return new TransferResult(fileVo);
        } catch (Exception e) {
            message = e.getMessage();
            log.error("failed to uploadFile : {} ", fileVo, e);
        }
        return new TransferResult(fileVo, message);
    }

}
