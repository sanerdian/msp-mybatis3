package com.jnetdata.msp.zdff.publishdistribution.controller.util;

import com.jnetdata.msp.zdff.publishdistribution.service.impl.CMSUtil;
import com.jnetdata.msp.zdff.publishdistribution.service.impl.ConfigUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统文件复制
 *
 * @author ma.hualong
 * @since 2019/09/10
 */
public class filesynchronization {

    static final String pub = "pub";//发布所在常量文件

    static final String webpic = "webpic";//附图上传地址常量文件

    static final String upload = "upload";//附件上传地址常量文件

    /**
     * 站点分发(增量发布、完全发布、直接发布这些、直接发布这篇、仅发布模板细览)(带附图、附件)
     *
     * @param oldfile 原文件路径
     * @param sFile   新文件路径
     * @param siteId  站点ID
     * @param docId   文档ID
     */
    public static void copyFile(String oldfile, String sFile, Long siteId, Long docId) {
        copyFileHtml(oldfile, sFile);//复制文本文件
//        copyFileAndPic(docId, siteId, sFile);//复制附图、附件
        restoreDocPic(docId, sFile);//复制正文中图片
    }

    /**
     * uedit编辑器上传内容附图
     *
     * @param listImgUrl
     * @param sFile
     */
    public static void copyFile(List<String> listImgUrl, String sFile, String datapath) {
        String newdatapath = "";
        String olddatapath = "";
        String configPath = "";
        String localFile = "";
        File file = null;
        for (int j = 0; j < listImgUrl.size(); j++) {
            olddatapath = CMSUtil.stringFormat(listImgUrl.get(j));
            configPath = ConfigUtil.getSysConfigByKey(datapath);
            localFile = configPath.substring(0, configPath.indexOf(datapath)) + olddatapath;
            newdatapath = mkdirs(sFile, olddatapath);
            file = new File(localFile);
            copyFileNew(newdatapath, file);//复制文件
        }
    }

    /**
     * uedit编辑器上传内容附件、仅发布站点/栏目首页
     *
     * @param rootPath
     * @param sFile
     * @param datapath
     */
    public static void copyFile(String rootPath, String sFile, String datapath) {
        String newPath = datapath + File.separator + rootPath.split(datapath)[1];
        String newdatapath = mkdirs(sFile, newPath);
        File file = new File(rootPath);
        copyFileNew(newdatapath, file);//复制文件
    }

    /**
     * 文本文件
     *
     * @param oldfile 原文件路径
     * @param sFile   新文件路径
     */
    private static void copyFileHtml(String oldfile, String sFile) {
        String path = oldfile.split(pub)[1];
        File file = new File(oldfile);
        String outFilePath = mkdirs(sFile, pub + File.separator + path);
        copyFileNew(outFilePath, file);//复制文件
    }

    /**
     * 附图、附件
     *
     * @param sFile  新文件路径
     * @param docId  文档ID
     * @param siteId 站点ID
     */
    private static void copyFileAndPic(Long docId, Long siteId, String sFile) {
        /*IPublishdistrbutionService ips = new PublishdistrbutionService();
        List<Map<String, Object>> list = ips.findappendixpicandappendixpic(docId);
        String filepath = "";
        if (list.size() > 0) {
            String newFilePath = "";
            for (int i = 0; i < list.size(); i++) {
                String type = CMSUtil.stringFormat(list.get(i).get("TYPE"));
                String appfile = CMSUtil.stringFormat(list.get(i).get("APPFILE"));
                if (type.equals("1")) {//附图
                    filepath = ConfigUtil.getPublishPath(webpic, siteId, 1, appfile);
                    newFilePath = mkdirs(sFile, webpic + filepath.split(webpic)[1]);
                } else if (type.equals("2")) {//附件
                    filepath = ConfigUtil.getPublishPath(upload, siteId, 1, appfile);
                    newFilePath = mkdirs(sFile, upload + filepath.split(upload)[1]);
                }
                File file = new File(filepath);
                copyFileNew(newFilePath, file);//复制文件
            }
        }*/
    }

    /**
     * 获取所有的图片路径
     *
     * @param docid
     * @param sFile
     */
    public static void restoreDocPic(Long docid, String sFile) {
        /*IDocumentService documentService = new DocumentService();
        Map<String, Object> docAll = documentService.getDocAll(docid);
        List<String> doccontent = getImageSrc(CMSUtil.stringFormat(docAll.get("DOCHTMLCON")));
        if (doccontent.size() > 0) {
            String newFilePath = "";
            String sysConfigByKey = ConfigUtil.getSysConfigByKey(upload).split(upload)[0];
            for (int i = 0; i < doccontent.size(); i++) {
                newFilePath = mkdirs(sFile, doccontent.get(i));
                File file = new File(sysConfigByKey + File.separator + doccontent.get(i));
                copyFileNew(newFilePath, file);//复制文件
            }
        }*/
    }


    /**
     * 解析html读取文件路径
     *
     * @param htmlCode
     * @return
     */
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
    }


    /**
     * 文件内容复制
     *
     * @param outFilePath 新文件地址
     * @param inFilePath  原文件地址
     */
    private static void copyFileNew(String outFilePath, File inFilePath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            /*创建文件输入、输出对象*/
            out = new FileOutputStream(outFilePath);
            in = new FileInputStream(inFilePath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    /*关闭资源*/
                    in.close();
                }
                if (out != null) {
                    /*关闭资源*/
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建文件、文件夹
     *
     * @param remoteFile
     * @param realpath
     * @return
     */
    private static String mkdirs(String remoteFile, String realpath) {
        String[] dirs = null;
        if (realpath.indexOf("/") != -1) {
            dirs = realpath.split("/");
        } else if (realpath.indexOf("\\") != -1) {
            dirs = realpath.split("\\\\");
        }
        String tempPath = remoteFile;
        for (int i = 0; i < dirs.length; i++) {
            if (null == dirs[i] || "".equals(dirs[i])) {
                continue;
            }
            tempPath += File.separator + dirs[i];
            File file = new File(tempPath);
            if (i == dirs.length - 1) {
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (!file.exists()) {
                    file.mkdir();
                }
            }
        }
        return tempPath;
    }
}
