package com.jnetdata.msp.zdff.publishdistribution.controller;

import com.jcraft.jsch.*;
import java.io.File;
import java.util.Properties;

/**
 * 解释一下SFTP的整个调用过程，这个过程就是通过Ip、Port、Username、Password获取一个Session,
 * 然后通过Session打开SFTP通道（获得SFTP Channel对象）,再在建立通道（Channel）连接，最后我们就是
 * 通过这个Channel对象来调用SFTP的各种操作方法.总是要记得，我们操作完SFTP需要手动断开Channel连接与Session连接。
 *
 * @author xuning
 * @since 2022/07/24
 */
public class sftpsynchronization {

//  private static Logger logger = Logger.getLogger(sftpsynchronization.class);
    private static ChannelSftp channel;
    private static Session session;

    public static boolean connectServer(String sftpHost, int sftpPort, String sftpUserName, String sftpPassword) {
        try {
            // 创建JSch对象
            JSch jsch = new JSch();
            // 根据用户名，主机ip，端口获取一个Session对象
            session = jsch.getSession(sftpUserName, sftpHost, sftpPort);
            if (sftpPassword != null) {
                // 设置密码
                session.setPassword(sftpPassword);
            }
            Properties configTemp = new Properties();
            configTemp.put("StrictHostKeyChecking", "no");
            // 为Session对象设置properties
            session.setConfig(configTemp);
            // 设置timeout时间
            session.setTimeout(60000);
            session.connect();
            // 通过Session建立链接
            // 打开SFTP通道
            channel = (ChannelSftp) session.openChannel("sftp");
            // 建立SFTP通道的连接
            channel.connect();
            return true;
        } catch (JSchException e) {
            return false;
        }
    }

    /**
     * 断开SFTP Channel、Session连接
     */
    public static void closeChannel() {
        try {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        } catch (Exception e) {
//            logger.error("关闭异常！");
            System.out.println("关闭异常！");
        }
    }

    /**
     * 上传文件
     *
     * @param localFile  本地文件
     * @param remoteFile 远程文件
     */
    public static void upload(String localFile, String remoteFile) throws SftpException {
        File file = new File(localFile);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length <= 0) {
//                logger.error("读取文件失败！");
                System.out.println("读取文件失败！");
            }
            for (File f : files) {
                /*返回文件的绝对路径*/
                String fp = f.getAbsolutePath();
                if (f.isDirectory()) {
                    String mkdir = remoteFile + "/" + f.getName();
                    try {
                        channel.cd(mkdir);
                    } catch (Exception e) {
                        channel.mkdir(mkdir);
                    }
                    upload(fp, mkdir);
                } else {
                    channel.put(fp, remoteFile, ChannelSftp.OVERWRITE);
                }
            }
        } else {
            /*返回文件的绝对路径*/
            String fp = file.getAbsolutePath();
            channel.put(fp, remoteFile, ChannelSftp.OVERWRITE);
        }
    }

    public static void main(String[] args) {
       /* try {
            connectServer("47.93.151.74", 22, "root", "Zkjw@1234", "");
            upload("D:/WORK/JMETADATA/pub", "/home/JMETADATA/pub");
            closeChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
//        String sign = MD5Util.MD5Encode("中国", "UTF-8").toLowerCase();
//        System.out.println(sign);
    }

}
