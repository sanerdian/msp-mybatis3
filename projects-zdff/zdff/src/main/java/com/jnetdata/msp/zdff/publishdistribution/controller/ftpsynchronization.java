package com.jnetdata.msp.zdff.publishdistribution.controller;


import java.io.*;
import com.jnetdata.msp.zdff.publishdistribution.model.ftpentity;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 获取ftp连接，上传下载文件
 *
 * @author xuning
 * @crtime 2022-07-24
 */
public class ftpsynchronization {

//  private static Logger logger = Logger.getLogger(ftpsynchronization.class);
    private static FTPClient ftp;

    /**
     * 获取ftp连接
     *
     * @param user
     * @return
     * @throws Exception
     */
    public static boolean connectFtp(ftpentity user) throws Exception {
        ftp = new FTPClient();
        boolean flag = false;
        int reply;
        if (user.getPort() == null) {
            //默认端口21
            ftp.connect(user.getIpAddr(), 21);
        } else {
            ftp.connect(user.getIpAddr(), user.getPort());
        }
        ftp.login(user.getUserName(), user.getPwd());
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            return flag;
        }
        //ftp.changeWorkingDirectory(user.getPath());
        flag = true;
        return flag;
    }

    /**
     * ftp上传文件
     *
     * @param f
     * @throws Exception
     * @author ma.hualong
     */
    public static void upload(File f, String url) throws Exception {
        /**
         * ftp.enterLocalPassiveMode();这个方法是每次数据连接之前，
         * 客户端向服务器的FTP端口（默认是21）发送连接请求，服务器接受连接，建立一条命令链路。
         * 当需要传送数据时， 服务器在命令链路上用PASV命令告诉客户端：“我打开了***X端口，你过来连接我”。
         * 于是客户端向服务器的***X端口发送连接请求，建立一条数据链 路来传送数据。
         */
        ftp.enterLocalPassiveMode();
        if (f.isDirectory()) {
            ftp.makeDirectory(f.getName());
            ftp.changeWorkingDirectory(f.getName());
            String[] files = f.list();
            for (String fstr : files) {
                File file1 = new File(f.getPath() + File.separator + fstr);
                if (file1.isDirectory()) {
                    upload(file1, url);
                    ftp.changeToParentDirectory();
                } else {
                    File file2 = new File(f.getPath() + File.separator + fstr);
                    fileupload(file2, url);
                }
            }
        } else {
            fileupload(f, url);
        }
    }

    /**
     * 文件上传ftp
     *
     * @param file
     * @param url
     * @throws Exception
     * @author ma.hualong
     */
    public static boolean fileupload(File file, String url) throws Exception {
        File file2 = new File(file.getPath());
        FileInputStream input = new FileInputStream(file2);
        //编码转换new String((file2.getName()).getBytes("UTF-8"), "iso-8859-1"),path可操作的绝对路径
        //设置文件编码，处理文件中包含中文路径
        boolean b = ftp.storeFile(new String((url + file2.getName()).getBytes("UTF-8"), "iso-8859-1"), input);
        input.close();
        return b;
    }

    /**
     * 下载链接配置
     *
     * @param f
     * @param localBaseDir  本地目录
     * @param remoteBaseDir 远程目录
     * @throws Exception
     */
    public static void startDown(ftpentity f, String localBaseDir, String remoteBaseDir) throws Exception {
        if (ftpsynchronization.connectFtp(f)) {
            try {
                FTPFile[] files = null;
                //设置文件编码，处理文件中包含中文路径
                boolean changedir = ftp.changeWorkingDirectory(new String(remoteBaseDir.getBytes("UTF-8"), "ISO-8859-1"));
                if (changedir) {
                    //获取文件之前，先连接通道
                    ftp.enterLocalPassiveMode();
                    files = ftp.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        try {
                            ftp.setControlEncoding("UTF-8");
                            String name = files[i].getName();
                            System.out.println(name);
                            //未查询出返回不是文件的原因，这块代码待处理
                            if (!name.equals(".") && !name.equals("..")) {
                                downloadFile(files[i], localBaseDir, remoteBaseDir);
                            }
                        } catch (Exception e) {
//                            logger.error(e);
//                            logger.error("<" + files[i].getName() + ">下载失败");
                            System.out.println("<" + files[i].getName() + ">下载失败");
                        }
                    }
                }
            } catch (Exception e) {
//                logger.error(e);
//                logger.error("下载过程中出现异常");
                System.out.println("下载过程中出现异常");
            }
        } else {
//            logger.error("链接失败！");
            System.out.println("链接失败！");
        }

    }


    /**
     * 下载FTP文件
     * 当你需要下载FTP文件的时候，调用此方法
     * 根据<b>获取的文件名，本地地址，远程地址</b>进行下载
     *
     * @param ftpFile
     * @param relativeLocalPath
     * @param relativeRemotePath
     */
    private static void downloadFile(FTPFile ftpFile, String relativeLocalPath, String relativeRemotePath) {
        if (ftpFile.isFile()) {
            if (ftpFile.getName().indexOf("?") == -1) {
                OutputStream outputStream = null;
                try {
                    File locaFile = new File(relativeLocalPath + File.separator + ftpFile.getName());
                    //判断文件是否存在，存在则返回
                    if (locaFile.exists()) {
                        return;
                    } else {
                        outputStream = new FileOutputStream(relativeLocalPath + File.separator + ftpFile.getName());
                        ftp.retrieveFile(ftpFile.getName(), outputStream);
                        /*清空输出流*/
                        outputStream.flush();
                        /*关闭输出流*/
                        outputStream.close();
                    }
                } catch (Exception e) {
//                    logger.error(e);
                    System.out.println(e);
                } finally {
                    try {
                        if (outputStream != null) {
                            /*关闭输出流*/
                            outputStream.close();
                        }
                    } catch (IOException e) {
//                        logger.error("输出文件流异常");
                        System.out.println("输出文件流异常");
                    }
                }
            }
        } else {
            String newlocalRelatePath = relativeLocalPath + ftpFile.getName();
            String newRemote = new String(relativeRemotePath + ftpFile.getName().toString());
            File fl = new File(newlocalRelatePath);
            if (!fl.exists()) {
                fl.mkdirs();
            }
            try {
                newlocalRelatePath = newlocalRelatePath + File.separator;
                newRemote = newRemote + File.separator;
                String currentWorkDir = ftpFile.getName().toString();
                boolean changedir = ftp.changeWorkingDirectory(currentWorkDir);
                if (changedir) {
                    FTPFile[] files = null;
                    files = ftp.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        downloadFile(files[i], newlocalRelatePath, newRemote);
                    }
                }
                if (changedir) {
                    ftp.changeToParentDirectory();
                }
            } catch (Exception e) {
//                logger.error(e);
                System.out.println(e);
            }
        }
    }

    /**
     * 关闭ftp连接
     */
    public static void closeFtp() {
        if (ftp != null && ftp.isConnected()) {
            try {
                /*退出ftp*/
                ftp.logout();
                ftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws Exception {
        ftpentity user = new ftpentity();
        user.setIpAddr("www.jnetdata.com");
        user.setUserName("zkjw");
        user.setPort(2121);
        user.setPwd("41v6r68S");
        boolean b = ftpsynchronization.connectFtp(user);
        System.out.println(b);
        //上传文件绝对路径
        String url = "/06员工交接文件/测试";
        File file = new File("D:/trshybase-api.jar");
        //ftpsynchronization.upload(file,url);//把文件上传在ftp上
        ftpsynchronization.startDown(user, "D:/WORK/", url);//下载ftp文件测试
        System.out.println("ok");

    }

}
