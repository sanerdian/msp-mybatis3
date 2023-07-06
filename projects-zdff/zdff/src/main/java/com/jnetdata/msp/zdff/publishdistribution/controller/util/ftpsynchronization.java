package com.jnetdata.msp.zdff.publishdistribution.controller.util;

import com.jnetdata.msp.zdff.publishdistribution.model.PublishdistriBution;
import com.jnetdata.msp.zdff.publishdistribution.service.impl.CMSUtil;
import com.jnetdata.msp.zdff.publishdistribution.service.impl.ConfigUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 获取ftp连接，上传下载文件
 *
 * @author ma.hualong
 * @crtime 2019-09-23
 */
public class ftpsynchronization {

//    private static Logger logger = Logger.getLogger(ftpsynchronization.class);

    private FTPClient ftp;

    //默认端口21
    private static final int port = 21;

    /**
     * 规避多线程并发不断开问题
     */
    private ThreadLocal<ftpsynchronization> ftpLocal = new ThreadLocal<ftpsynchronization>();

    /**
     * 获取ftp连接
     * @param map
     * @throws Exception
     */
    public void connectFtp(PublishdistriBution map) throws Exception {
        ftp = new FTPClient();
        int reply;
        Integer targetPort = CMSUtil.intFormat(CMSUtil.stringFormat(map.getTargetPort()));
        if (targetPort==0) {
            ftp.connect(CMSUtil.stringFormat(map.getTargetServer()),port);
        } else {
            ftp.connect(CMSUtil.stringFormat(map.getTargetServer()), targetPort);
        }
        ftp.login(CMSUtil.stringFormat(map.getLoginUser()), CMSUtil.stringFormat(map.getLoginPwd()));
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
        }
    }

    /**
     * ftp上传文件
     *
     * @param newFilePath
     * @param url
     * @throws Exception
     */
    public void upload(String newFilePath, String url) throws Exception {
        /**
         * ftp.enterLocalPassiveMode();这个方法是每次数据连接之前，
         * 客户端向服务器的FTP端口（默认是21）发送连接请求，服务器接受连接，建立一条命令链路。
         * 当需要传送数据时， 服务器在命令链路上用PASV命令告诉客户端：“我打开了***X端口，你过来连接我”。
         * 于是客户端向服务器的***X端口发送连接请求，建立一条数据链 路来传送数据。
         *
         * 注：对于中文路径需要进行处理转字节码
         */
        ftp.enterLocalPassiveMode();
        String[] dirs = null;
        String realPath = "";
        //ptf路径最后必须是/或\
        if (systemtype()) {
            /*
            *lastIndexOf("\\")
            * 查找'\\'最后出现的位置
            * */
            newFilePath = newFilePath.replace("/", "\\");
            realPath = newFilePath.substring(0, newFilePath.lastIndexOf("\\") + 1);
            dirs = realPath.split("\\\\");
        } else {
            newFilePath = newFilePath.replace("\\", "/");
            realPath = newFilePath.substring(0, newFilePath.lastIndexOf("/") + 1);
            dirs = realPath.split("/");
        }
        String tempPath = "";
        for (String dir : dirs) {
            tempPath = dir;
            //如果是中文路径需要处理
            boolean changeWorkingDirectory = ftp.changeWorkingDirectory(new String(tempPath.getBytes("UTF-8"), ftp.DEFAULT_CONTROL_ENCODING));
            if (!changeWorkingDirectory) {
                if (null == dir || "".equals(dir)) {
                    continue;
                } else {
                    boolean makeDirectory = ftp.makeDirectory(tempPath);
                    if (!makeDirectory) {
//                        logger.error("创建文件夹失败！");
                    }
                    boolean WorkingDirectory = ftp.changeWorkingDirectory(new String(tempPath.getBytes("UTF-8"), ftp.DEFAULT_CONTROL_ENCODING));
                    if (!WorkingDirectory) {
//                        logger.error("进入当前目录失败！");
                    }
                }
            } else {
                continue;
            }
        }
        //ftp完成文件夹创建返回根目录，避免创建文件混乱情况。
        ftp.changeWorkingDirectory("/");
        fileupload(new File(url), realPath);
    }

    public ftpsynchronization getftpUtil(PublishdistriBution map) throws Exception {
        ftpsynchronization ftpUtil = ftpLocal.get();
        if (null == ftpUtil || !ftpUtil.isChannel()) {
            ftpLocal.set(new ftpsynchronization(map));
        }
        return ftpLocal.get();
    }


    private ftpsynchronization(PublishdistriBution map) throws Exception {
        super();
        connectFtp(map);
    }


    /**
     * 文件上传ftp
     *
     * @param file
     * @param url
     * @throws Exception
     * @author ma.hualong
     */
    public void fileupload(File file, String url) throws Exception {
        FileInputStream input = new FileInputStream(file);
        //编码转换new String((file2.getName()).getBytes("UTF-8"), "iso-8859-1"),path可操作的绝对路径
        boolean b = ftp.storeFile(new String((url + file.getName()).getBytes("UTF-8"), ftp.DEFAULT_CONTROL_ENCODING), input);
        if (!b) {
//            logger.error("文件上传:" + b + ",上传文件指向路径错误：" + url);
        }
        input.close();
    }

    public ftpsynchronization() {
    }


    /**
     * 根据配置连接信息获取服务器连接、上传文件、关闭连接
     * 仅发布站点/栏目首页
     *
     * @param map
     * @param localFile
     */
    public static void publishdistri(PublishdistriBution map, String localFile) {
        String dataPath = CMSUtil.stringFormat(map.getDataPath());
        ftpsynchronization ftpUtil = null;
        try {
            String newdatapah = dataPath + File.separator + "pub" + File.separator + localFile.split("pub")[1];
            try {
                ftpsynchronization ftp = new ftpsynchronization();
                ftpUtil = ftp.getftpUtil(map);
                ftpUtil.upload(newdatapah, localFile);
                System.out.println("执行ftp");
//                copyFileAndPic(ftpUtil, 0L, 0L, dataPath,newdatapah);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ftpUtil.closeFtp(ftpUtil);
        }
    }

    /**
     * 根据配置连接信息获取服务器连接、上传文件、关闭连接
     * 增量发布、完全发布、直接发布这些、直接发布这篇、仅发布模板细览
     *
     * @param map
     * @param localFile
     * @param datapath
     * @param docId
     * @param siteId
     */
    public static void publishdistri(PublishdistriBution map, String localFile, String datapath, Long docId, Long siteId) {
        String dataPath = CMSUtil.stringFormat(map.getDataPath());
        ftpsynchronization ftpUtil = null;
        try {
            try {
                System.out.println("333333333333333333");
                String filepath = dataPath +"/pub".replaceAll("//","/");
                localFile = localFile.substring(0,localFile.indexOf("pub"))+"pub";
                ftpsynchronization ftp = new ftpsynchronization();
                ftpUtil = ftp.getftpUtil(map);
//                String filepath = dataPath + "pub" + localFile.split("pub")[1];
//                newdatapah = "/pub";
                ftpUtil.upload(filepath, localFile);
                //复制附件或附图
                System.out.println("执行ftp1");
                copyFileAndPic(ftpUtil, docId, siteId, dataPath,datapath);
                //restoreDocPic(sftpUtil,docId,dataPath);//编辑器附图复制
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ftpUtil.closeFtp(ftpUtil);
        }
    }

    /**
     * udedit编辑器上传内容附图（注：编辑器图片、视频的文件后期在文档发布时处理解析html,获取文件路径）
     *
     * @param map
     * @param listImgUrl
     * @param datapath
     * @param newpath
     */
    public static void publishdistrift(PublishdistriBution map, List<String> listImgUrl, String datapath, String newpath) {
        String dataPath = CMSUtil.stringFormat(map.getDataPath());
        ftpsynchronization ftpUtil = null;
        try {
            try {
                ftpsynchronization ftp = new ftpsynchronization();
                ftpUtil = ftp.getftpUtil(map);
                for (int j = 0; j < listImgUrl.size(); j++) {
                    String olddatapath = CMSUtil.stringFormat(listImgUrl.get(j));
                    String configPath = ConfigUtil.getSysConfigByKey(datapath);
                    if(olddatapath.contains("/upload/ueditor/uploadimage")){
                        String localFile = configPath.substring(0, configPath.indexOf(datapath)) + olddatapath;
                        ftpUtil.upload(dataPath + "/upload/ueditor/uploadimage" + newpath, localFile);
                    }else if(olddatapath.contains("/upload/ueditor/uploadfile")){
                        String localFile = configPath.substring(0, configPath.indexOf(datapath)) + olddatapath;
                        ftpUtil.upload(dataPath + "/upload/ueditor/uploadfile" + newpath, localFile);
                    }else if(olddatapath.contains("video")){
                        configPath = ConfigUtil.getSysConfigByKey("video");
                        String localFile = configPath.substring(0, configPath.indexOf("video")) + olddatapath;
//                        ftpUtil.upload(dataPath + "/video" + newpath, localFile);
                        ftpUtil.upload(olddatapath.substring(0,olddatapath.lastIndexOf("/")), localFile);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ftpUtil.closeFtp(ftpUtil);
        }
    }


    /**
     * udedit编辑器上传视频（注：编辑器图片、视频的文件后期在文档发布时处理解析html,获取文件路径）
     *
     * @param map
     * @param localFile
     * @param datapath
     */
    public static void publishdistri(PublishdistriBution map, String localFile, String datapath) {
        String dataPath = CMSUtil.stringFormat(map.getDataPath());
        ftpsynchronization ftpUtil = null;
        try {
            String newdatapah = dataPath + File.separator + datapath + File.separator + localFile.split(datapath)[1];
            try {
                ftpsynchronization ftp = new ftpsynchronization();
                ftpUtil = ftp.getftpUtil(map);
                System.out.println("localFile = [" + localFile + "], newdatapah = [" + newdatapah + "]");
                ftpUtil.upload(newdatapah, localFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ftpUtil.closeFtp(ftpUtil);
        }
    }


    /**
     * 附件、附图上传
     *
     * @param ftpUtil
     * @param docId
     * @param siteId
     * @param dataPath
     */
    private static void copyFileAndPic(ftpsynchronization ftpUtil, Long docId, Long siteId, String dataPath,String datapath) {
//        IPublishdistrbutionService ips = new PublishdistrbutionService();
//        List<Map<String, Object>> list = ips.findappendixpicandappendixpic(docId);
        List<Map<String, Object>> list = null;
        String filepath = "";
        String newFilePath = "";
        String newFilePath1 = "";
        String newFilePath2 = "";
        String datapaths = "";
        String filepathftp = "";
        String filepathftps = "";
        String filepathftpss = "";
        try {
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String type = CMSUtil.stringFormat(list.get(i).get("TYPE"));
                String appfile = CMSUtil.stringFormat(list.get(i).get("APPFILE"));
                filepathftp = datapath;
                filepathftp = filepathftp.replaceAll("//","/");
                int index_a=filepathftp.indexOf("/",filepathftp.indexOf("/")+1);
                filepathftp=filepathftp.replaceAll(filepathftp.substring(index_a,filepathftp.length()),"");
                System.out.println(filepathftp);
                datapaths = filepathftp;
                int index_b=datapaths.indexOf("/",datapaths.indexOf("/"));
                System.out.println(datapaths.substring(0,index_b));

                filepathftps=datapath.replaceAll("//","/");
                int index_c=filepathftps.indexOf("/",filepathftps.indexOf("/")+1);
                System.out.println(filepathftps.substring(index_c,filepathftps.length()));

                if (type.equals("1")) {//附图复制
//                    filepath = ConfigUtil.getPublishPath("webpic", siteId, 1, appfile);
                    filepathftpss=filepath.replaceAll("//","/");
                    int index_d=filepathftpss.indexOf("/",filepathftpss.lastIndexOf("/")-1);
//                    String configPath=ConfigUtil.getSysConfigByKey("webpic");
                    String  dfg1 = filepathftpss.substring(0,index_d).replaceAll("/data/JMETADATA/webpic","").replaceAll("/"+datapaths.substring(0,index_b),"");
                    newFilePath = dataPath + File.separator + "webpic" + File.separator + filepath.split("webpic")[1];
                    newFilePath1 = dataPath +  File.separator + "pub"  + File.separator + filepath.split("webpic")[1].replaceAll(datapaths.substring(0,index_b),filepathftp).replaceAll(dfg1,filepathftps.substring(index_c,filepathftps.length()));
                    newFilePath2 = dataPath +  File.separator + "pub"  + File.separator + filepath.split("webpic")[1].replaceAll(datapaths.substring(0,index_b),filepathftp);
                    System.out.println("附图复制:"+newFilePath);
                    System.out.println("附图复制1:"+newFilePath1);
                } else if (type.equals("2")) {//附件复制
//                    filepath = ConfigUtil.getPublishPath("upload", siteId, 1, appfile);
                    filepathftpss=filepath.replaceAll("//","/");
                    int index_d=filepathftpss.indexOf("/",filepathftpss.lastIndexOf("/")-1);
                    String  dfg1 = filepathftpss.substring(0,index_d).replaceAll("/data/JMETADATA/upload","").replaceAll("/"+datapaths.substring(0,index_b),"");
                    newFilePath = dataPath + File.separator + "upload" + File.separator + filepath.split("upload")[1];
                    newFilePath1 = dataPath + File.separator + "pub"  + File.separator + filepath.split("upload")[1].replaceAll(datapaths.substring(0,index_b),filepathftp).replaceAll(dfg1,filepathftps.substring(index_c,filepathftps.length()));
                    newFilePath2 = dataPath +  File.separator + "pub"  + File.separator + filepath.split("upload")[1].replaceAll(datapaths.substring(0,index_b),filepathftp);
                }

                System.out.println("执行开始");
                ftpUtil.upload(newFilePath, filepath);
                ftpUtil.upload(newFilePath1, filepath);
                ftpUtil.upload(newFilePath2, filepath);
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ftpUtil.closeFtp(ftpUtil);
        }
    }


    /**
     * 下载链接配置
     *
     * @param f
     * @param localBaseDir  本地目录
     * @param remoteBaseDir 远程目录
     * @throws Exception
     */
/*    public static void startDown(Ftpentity f, String localBaseDir, String remoteBaseDir) throws Exception {
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
                            logger.error(e);
                            logger.error("<" + files[i].getName() + ">下载失败");
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e);
                logger.error("下载过程中出现异常");
            }
        } else {
            logger.error("链接失败！");
        }

    }*/


    /**
     * 下载FTP文件
     * 当你需要下载FTP文件的时候，调用此方法
     * 根据<b>获取的文件名，本地地址，远程地址</b>进行下载
     *
     * @param ftpFile
     * @param relativeLocalPath
     * @param relativeRemotePath
     */
/*    private static void downloadFile(FTPFile ftpFile, String relativeLocalPath, String relativeRemotePath) {
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
                        outputStream.flush();
                        outputStream.close();
                    }
                } catch (Exception e) {
                    logger.error(e);
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        logger.error("输出文件流异常");
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
                logger.error(e);
            }
        }
    }*/

    /**
     * 判断sftp是否连接
     *
     * @return
     */
    public boolean isChannel() {
        try {
            if (ftp.isConnected()) {
                return true;
            }
        } catch (Exception e) {
//            logger.info(e.getMessage());
        }
        return false;
    }

    /**
     * 关闭ftp连接
     */
    public void closeFtp(ftpsynchronization ftpUtil) {
        if (ftpUtil.isChannel()) {
            ftpLocal.set(null);
            if (ftp != null && ftp.isConnected()) {
                try {
                    ftp.logout();
                    ftp.disconnect();
//                    logger.error("关闭ftp连接成功！");
                } catch (IOException e) {
//                    logger.error("关闭ftp连接失败！");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 当前所属系统
     *
     * @return
     */
    public static boolean systemtype() {
        boolean flag = false;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            flag = true;
        }
        return flag;
    }

    public static void main(String[] args) throws Exception {
        /*//ftpsynchronization.connectFtp("www.jnetdata.com",2121,"zkjw","41v6r68S");
        ftpsynchronization ftp = new ftpsynchronization();
        ftp.getftpUtil("39.105.94.108", 2121, "ygtest", "ygtest123");
        //上传文件绝对路径
        String url = "/06员工交接文件/测试/pub/ftpwjfz//wjff//201909/20190923/j_2019092309560900015692039244562536.html";
        String url1 = "/06员工交接文件/测试/pub/ftpwjfz//wjff//201909/20190923/";
        File file = new File("D:\\CS\\WORK\\JMETADATA\\pub\\ftpwjfz\\wjff\\201909\\20190923\\j_2019092309560900015692039244562536.html");
        upload(url, "D:/CS/WORK/JMETADATA/pub/ftpwjfz//wjff//201909/20190923/j_2019092315570500015692255807042622.html");
        //ftpsynchronization.fileupload(file, url1);//把文件上传在ftp上
        //ftpsynchronization.startDown(user, "D:/WORK/", url);//下载ftp文件测试
        System.out.println("ok");*/

    }

}
