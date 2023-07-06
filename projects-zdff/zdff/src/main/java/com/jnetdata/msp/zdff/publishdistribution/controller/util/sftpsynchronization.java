package com.jnetdata.msp.zdff.publishdistribution.controller.util;

import com.jcraft.jsch.*;
import com.jnetdata.msp.zdff.publishdistribution.model.PublishdistriBution;
import com.jnetdata.msp.zdff.publishdistribution.service.impl.CMSUtil;
import com.jnetdata.msp.zdff.publishdistribution.service.impl.ConfigUtil;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 解释一下SFTP的整个调用过程，这个过程就是通过Ip、Port、Username、Password获取一个Session,
 * 然后通过Session打开SFTP通道（获得SFTP Channel对象）,再在建立通道（Channel）连接，最后我们就是
 * 通过这个Channel对象来调用SFTP的各种操作方法.总是要记得，我们操作完SFTP需要手动断开Channel连接与Session连接。
 *
 * @author mahualong
 * @since 2019/08/12
 */
public class sftpsynchronization {

//    private static Logger logger = Logger.getLogger(sftpsynchronization.class);

    /**
     * 生产环境设计多线程大数据量时 ，会出现部分文件丢失，private static FTPClient ftpClient = null，
     * 去掉static后即可解决，原因：FTPClient 对象加了static后会导致 容器中只存在一个ftpClient 所以多线程执行时，
     * 如果存在某一个线程关闭了ftpClient，会导致其他的线程执行报错；所以去掉static每个线程为单独的
     */
    private ChannelSftp channel;

    private Session session;

    /**
     * 规避多线程并发不断开问题
     */
    private ThreadLocal<sftpsynchronization> sftpLocal = new ThreadLocal<sftpsynchronization>();

    /**
     * 创建sftp连接
     * @param map
     */
    public void connectServer(PublishdistriBution map) {
        try {
            // 创建JSch对象
            JSch jsch = new JSch();
            // 根据用户名，主机ip，端口获取一个Session对象
            String sftpPassword = CMSUtil.stringFormat(map.getLoginPwd());
            session = jsch.getSession(CMSUtil.stringFormat(map.getLoginUser()), CMSUtil.stringFormat(map.getTargetServer()), CMSUtil.intFormat(CMSUtil.stringFormat(map.getTargetPort())));
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
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开SFTP Channel、Session连接
     *
     * @param channel
     * @param session
     */
    public void closeChannel(ChannelSftp channel, Session session) {
        try {
            if (channel != null) {
                channel.disconnect();
                sftpLocal.set(null);
            }
            if (session != null) {
                session.disconnect();
            }
        } catch (Exception e) {
//            logger.info(e.getMessage());
        }
    }

    /**
     * 判断sftp是否连接
     *
     * @return
     */
    public boolean isChannel() {
        try {
            if (channel.isConnected()) {
                return true;
            }
        } catch (Exception e) {
//            logger.info(e.getMessage());
        }
        return false;
    }

    /**
     * 获取本地线程存储的sftp客户端
     * @param map
     * @return
     * @throws Exception
     */
    public sftpsynchronization getSftpUtil(PublishdistriBution map) throws Exception {
        sftpsynchronization sftpUtil = sftpLocal.get();
        if (null == sftpUtil || !sftpUtil.isChannel()) {
            sftpLocal.set(new sftpsynchronization(map));
        }
        return sftpLocal.get();
    }

    private sftpsynchronization(PublishdistriBution map) throws Exception {
        super();
        connectServer(map);
    }

    public sftpsynchronization() {
    }

    /**
     * 释放本地线程存储的sftp客户端
     *
     * @param sftpUtil
     */
    public void release(sftpsynchronization sftpUtil) {
        sftpUtil.closeChannel(sftpUtil.channel, sftpUtil.session);
    }

    /**
     * 上传文件
     *
     * @param localFile  指定本地文件
     * @param remoteFile 配置服务路径
     * @param realpath   服务器具体路径
     * @throws SftpException
     */
    public void upload_old(String localFile, String remoteFile, String realpath) throws SftpException {
        realpath = realpath.replaceAll("//", "/");
        File file = new File(localFile);
        try {
            String paths = (remoteFile + "/" + realpath).replaceAll("//", "/");
            System.out.println(paths);
            System.out.println("----------------");
            channel.cd(paths);
        } catch (Exception e) {
            //目录不存在，则创建文件夹
            String[] dirs = realpath.split("/");
            String tempPath = remoteFile;
            for (String dir : dirs) {
                if (null == dir || "".equals(dir)) {
                    continue;
                }
                tempPath += "/" + dir;
                System.out.println(tempPath);
                try {
                    //进入当前文件
                    channel.cd(tempPath);
                } catch (SftpException ex) {
                    //不存在，创建文件夹
                    /*File files = new File(tempPath);
                    if(!files.exists()){
                        files.mkdir();
                    }*/
                    channel.mkdir(tempPath);
                    channel.cd(tempPath);
                }
            }
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length <= 0) {
//                logger.info("读取文件错误");
            }
            for (File f : files) {
                String fp = f.getAbsolutePath();
                if (f.isDirectory()) {
                    String mkdir = remoteFile + "/" + f.getName();
                    try {
                        channel.cd(mkdir);
                    } catch (Exception e) {
                        channel.mkdir(mkdir);
                    }
                    upload(fp, mkdir, realpath);
                } else {
                    //OVERWRITE 覆盖文件
                    channel.put(fp, remoteFile + "/" + realpath, ChannelSftp.OVERWRITE);
                }
            }
        } else {
            String fp = file.getAbsolutePath();
            channel.put(fp, remoteFile + "/" + realpath, ChannelSftp.OVERWRITE);
        }
    }

    /*上传文件*/
    public void upload(String localFile, String remoteFile, String realpath) throws SftpException {
//        realpath = realpath.replaceAll("//", "/");
        File file = new File(localFile);
        try {
            String paths = (remoteFile + "/" + realpath).replaceAll("//", "/");
            System.out.println(paths);
            System.out.println("----------------");
            channel.cd(paths);
        } catch (Exception e) {
            //目录不存在，则创建文件夹
            String[] dirs = realpath.split("/");
            String tempPath = remoteFile;
            for (String dir : dirs) {
                if (null == dir || "".equals(dir)) {
                    continue;
                }
                if(tempPath.split("pub").length==1){
                    tempPath += "/" + dir;
                }

                System.out.println(tempPath);
                try {
                    //进入当前文件
                    channel.cd(tempPath);
                } catch (SftpException ex) {
                    //不存在，创建文件夹
                    /*File files = new File(tempPath);
                    if(!files.exists()){
                        files.mkdir();
                    }*/
                    channel.mkdir(tempPath);
                    channel.cd(tempPath);
                }
            }
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length <= 0) {
//                logger.info("读取文件错误");
            }
            if(remoteFile.split("pub").length==1){
                remoteFile = (remoteFile + "/" + realpath).replaceAll("//", "/");
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
                    upload(fp, mkdir, realpath);
                } else {
                    //OVERWRITE 覆盖文件
                    channel.put(fp, remoteFile, ChannelSftp.OVERWRITE);
                }
            }
        } else {
            String fp = file.getAbsolutePath();
            channel.put(fp, remoteFile, ChannelSftp.OVERWRITE);
        }
    }

    /**
     * 根据配置连接信息获取服务器连接、上传文件、关闭连接
     * 仅发布站点/栏目首页
     *
     * @param map
     * @param localFile
     * @param datapath
     */
    public static void publishdistri(PublishdistriBution map, String localFile, String datapath) {
        String dataPath = CMSUtil.stringFormat(map.getDataPath());
        sftpsynchronization sftpUtil = null;
        try {
            String newdatapah = datapath.replaceAll("//", "/");
            if(dataPath.indexOf("pub")==-1){
                newdatapah = "pub/"+newdatapah;
            }
            try {
                sftpsynchronization sftp = new sftpsynchronization();
                sftpUtil = sftp.getSftpUtil(map);
                System.out.println("localFile = [" + localFile + "], newdatapah = [" + newdatapah + "] dataPath = "+dataPath);
                sftpUtil.upload(localFile, dataPath, newdatapah);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sftpUtil.release(sftpUtil);
        }
    }

    /**
     * udedit编辑器上传视频（注：编辑器图片、视频的文件后期在文档发布时处理解析html,获取文件路径）
     *
     * @param map
     * @param localFile
     * @param datapath
     * @param newpath
     */
    public static void publishdistri(PublishdistriBution map, String localFile, String datapath, String newpath) {
        String dataPath = CMSUtil.stringFormat(map.getDataPath());
        sftpsynchronization sftpUtil = null;
        try {
            String newdatapah = dataPath.replaceAll("//", "/").replaceAll("pub", datapath);
            try {
                sftpsynchronization sftp = new sftpsynchronization();
                sftpUtil = sftp.getSftpUtil(map);
                sftpUtil.upload(localFile, newdatapah, newpath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sftpUtil.release(sftpUtil);
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
        sftpsynchronization sftpUtil = null;
        try {
//            String newdatapah = dataPath.replaceAll("//", "/").substring(0, dataPath.lastIndexOf("/")) + "/" + datapath;

            String newdatapah = datapath.replaceAll("//", "/");
//            if(dataPath.indexOf("upload")==-1){
                newdatapah = "//"+newdatapah;
//            }
            try {
                sftpsynchronization sftp = new sftpsynchronization();
                sftpUtil = sftp.getSftpUtil(map);
                for (int j = 0; j < listImgUrl.size(); j++) {
                    String olddatapath = CMSUtil.stringFormat(listImgUrl.get(j));
                    String configPath = ConfigUtil.getSysConfigByKey(datapath);
                    String localFile = configPath.substring(0, configPath.indexOf(datapath)) + olddatapath;
                    sftpUtil.upload(localFile.replaceAll("//", "/"), newdatapah, "/ueditor/uploadimage" + newpath.substring(0,newpath.length()-1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sftpUtil.release(sftpUtil);
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
        sftpsynchronization sftpUtil = null;
        try {
            String newdatapah = datapath.replaceAll("//", "/");
//            if(dataPath.indexOf("pub")==-1){
//                newdatapah = "pub/"+newdatapah;
//            }
            try {
                sftpsynchronization sftp = new sftpsynchronization();
                sftpUtil = sftp.getSftpUtil(map);
                //E:\fastdevxp\web\pub\html\zkjw\ptcp\sjyzsfw\lby.html
                localFile = localFile.substring(0,localFile.indexOf("pub"))+"pub";
                newdatapah = "/pub";
                sftpUtil.upload(localFile, dataPath, newdatapah);
                //复制附件或附图
                newdatapah ="/upload";
                copyFileAndPic(sftpUtil, docId, siteId, dataPath, newdatapah,localFile.replaceAll("pub","upload").replaceAll("web",""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sftpUtil.release(sftpUtil);
        }
    }

/*    *//**
     * 获取所有的图片路径
     *
     * @param docid
     * @param sFile
     *//*
    public static void restoreDocPic(sftpsynchronization sftpUtil, Long docid, String sFile) {
        IDocumentService documentService = new DocumentService();
        Map<String, Object> docAll = documentService.getDocAll(docid);
        List<String> doccontent = getImageSrc(CMSUtil.stringFormat(docAll.get("DOCHTMLCON")));
        if (doccontent.size() > 0) {
            String newFilePath = "";
            String sysConfigByKey = ConfigUtil.getSysConfigByKey("upload").split("upload")[0];
            for (int i = 0; i < doccontent.size(); i++) {
                try {
                    sftpUtil.upload(sysConfigByKey + File.separator + doccontent.get(i), sFile, doccontent.get(i));
                } catch (SftpException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    *//**
     * 解析html
     *
     * @param htmlCode
     * @return
     *//*
    public static List<String> getImageSrc(String htmlCode) {
        List<String> imageSrcList = new ArrayList<String>();
        Pattern p = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic)\\b)[^>]*>", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);
        String quote = null;
        String src = null;
        while (m.find()) {
            quote = m.group(1);
            src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("\\s+")[0] : m.group(2);
            imageSrcList.add(src);
        }
        return imageSrcList;
    }*/

    /**
     * 附件、附图上传
     *
     * @param sftpUtil
     * @param docId
     * @param siteId
     * @param dataPath
     * @param newdatapah
     */
    private static void copyFileAndPic(sftpsynchronization sftpUtil, Long docId, Long siteId, String dataPath, String newdatapah,String localFile) {
        /*IPublishdistrbutionService ips = new PublishdistrbutionService();
        List<Map<String, Object>> list = ips.findappendixpicandappendixpic(docId);
        String filepath = "";
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String type = CMSUtil.stringFormat(list.get(i).get("TYPE"));
                String appfile = CMSUtil.stringFormat(list.get(i).get("APPFILE"));
                if (type.equals("1")) {//附图复制
                    filepath = ConfigUtil.getPublishPath("webpic", siteId, 1, appfile);
                } else if (type.equals("2")) {//附件复制
                    filepath = ConfigUtil.getPublishPath("upload", siteId, 1, appfile);
                }

                int index_=appfile.indexOf("_")+1;
                //获取老文件的地址，给ftp/sftp放同样的位置
                String newdatapah2=newdatapah+"/"+appfile.substring(index_,index_+6)+"/"+appfile.substring(index_,index_+8);
                int index_a=newdatapah.indexOf("/",newdatapah.indexOf("/")+1);
                newdatapah2=newdatapah2.replaceAll(newdatapah.substring(index_a,newdatapah.length()),"");
                newdatapah2=newdatapah2.replaceAll("//","/");
                try {
                    sftpUtil.upload(filepath, dataPath, newdatapah2);
                    sftpUtil.upload(filepath, dataPath, newdatapah);
                } catch (SftpException e) {
                    e.printStackTrace();
                }
            }
        }*/
        try {
            localFile = localFile.replaceAll("//", "/");
            sftpUtil.upload(localFile, dataPath, newdatapah);
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filepath = "D:/CS/WORK/JMETADATA/webpic/zkjw_3/202004/20200409/jpg_202004091212030_94920.jpg";
        String appfile = "jpg_202004091212030_94920.jpg";
        String newdatapah = "zkjw_3/zdff/201911/20191118";
        String newdatapahs = "zkjw_3/zdff";
        String newdatapahss = "kpzpxxk//zpgl//202004/20200405";
        String newdatapahssd = "/data/JMETADATA/webpic/kpzpxxk/202004/20200410/jpg_202004100854043_67428.jpg";
        int index_=appfile.indexOf("_")+1;
        String newdatapah2=newdatapah+"/"+appfile.substring(index_,index_+6)+"/"+appfile.substring(index_,index_+8);
//        System.out.println(newdatapah2);
//        int index_a=newdatapah.lastIndexOf("/", -2);
//        int index_a=newdatapah.indexOf("/",newdatapah.indexOf("/")+1);
//        System.out.println(newdatapah.substring(index_a,newdatapah.length()));
//        System.out.println(newdatapah2.replaceAll(newdatapah.substring(index_a,newdatapah.length()),""));
//        int index_b=newdatapahs.indexOf("/",newdatapahs.indexOf("/"));
//        System.out.println(newdatapahs.substring(0,index_b));
        newdatapahssd=newdatapahssd.replaceAll("//","/");
        int index_a=newdatapahssd.indexOf("/",newdatapahssd.lastIndexOf("/")-1);
        String  dfg = newdatapahssd.substring(index_a,newdatapahssd.length());
//        System.out.println(dfg);

        String  dfg1 = newdatapahssd.substring(0,index_a);
        System.out.println(dfg1.replaceAll("/data/JMETADATA/webpic",""));

        newdatapahss=newdatapahss.replaceAll("//","/");
        int index_c=newdatapahss.indexOf("/",newdatapahss.indexOf("/")+1);
        System.out.println(newdatapahss.substring(index_c,newdatapahss.length()));

        String newdatapahssdsss = "/data/JMETADATA/webpic/";
//        int index_dd=newdatapahssdsss.lastIndexOf("/");
//        System.out.println(newdatapahssdsss.substring(0,index_dd));
        String out = newdatapahssdsss.replaceFirst("(.*/)(.*)(/[^/]*)", "$1" + newdatapahssdsss + "$3");
        System.out.println(out);
    }

}
