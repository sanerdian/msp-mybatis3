package com.jnetdata.msp.zdff.publishdistribution.service.impl;

import com.jnetdata.msp.zdff.publishdistribution.controller.util.filesynchronization;
import com.jnetdata.msp.zdff.publishdistribution.controller.util.ftpsynchronization;
import com.jnetdata.msp.zdff.publishdistribution.controller.util.sftpsynchronization;
import com.jnetdata.msp.zdff.publishdistribution.model.PublishdistriBution;

import java.util.List;
import java.util.Map;

public class CMSPublishUtil {

    /**
     * 站点分发(仅发布站点/栏目首页)
     *
     * @param publishdistriBution
     * @param targetFile
     * @param datapath
     * @author mahualong
     */
    public static void publishdistriBution(List<PublishdistriBution> publishdistriBution, String targetFile, String datapath) {
        if (publishdistriBution != null && publishdistriBution.size() > 0) {
            for(PublishdistriBution p:publishdistriBution){
                String targetType = CMSUtil.stringFormat(p.getTargetType());
                if (targetType.equals("1")) {
                    //跨服务器文件添加
                    sftpsynchronization.publishdistri(p, targetFile, datapath);
                } else if (targetType.equals("2")) {
                    //系统文件复制
                    filesynchronization.copyFile(targetFile, CMSUtil.stringFormat(p.getDataPath()), "pub");
                } else if (targetType.equals("3")) {
                    //ftp文件复制
                    ftpsynchronization.publishdistri(p, targetFile);
                }
            }
        }
    }

    /**
     * 站点分发(udedit编辑器上传视频)
     *
     * @param publishdistriBution
     * @param localFile
     * @param datapath
     * @param newpath
     */
    public static void publishdistriBution(List<PublishdistriBution> publishdistriBution, String localFile, String datapath, String newpath) {
        if (publishdistriBution != null && publishdistriBution.size() > 0) {
            for(PublishdistriBution p:publishdistriBution){
                String targetType = CMSUtil.stringFormat(p.getTargetType());
                if (targetType.equals("1")) {//跨服务器文件添加
                    sftpsynchronization.publishdistri(p, localFile, datapath, newpath);
                } else if (targetType.equals("2")) {//系统文件复制
                    filesynchronization.copyFile(localFile, CMSUtil.stringFormat(p.getDataPath()), datapath);
                } else if (targetType.equals("3")) {//ftp文件复制
                    ftpsynchronization.publishdistri(p, localFile, datapath);
                }
            }
        }
    }

    /**
     * 站点分发(uedit编辑器上传内容附图)
     *
     * @param publishdistriBution
     * @param listImgUrl
     * @param datapath
     * @param newpath
     */
    public static void publishdistriBution(List<PublishdistriBution> publishdistriBution, List<String> listImgUrl, String datapath, String newpath) {
        if (publishdistriBution != null && publishdistriBution.size() > 0) {
            for(PublishdistriBution p:publishdistriBution){
                String targetType = CMSUtil.stringFormat(p.getTargetType());
                if (targetType.equals("1")) {//跨服务器文件添加
                    sftpsynchronization.publishdistrift(p, listImgUrl, datapath, newpath);
                } else if (targetType.equals("2")) {//系统文件复制
                    filesynchronization.copyFile(listImgUrl, CMSUtil.stringFormat(p.getDataPath()), datapath);
                } else if (targetType.equals("3")) {//ftp文件复制
                    ftpsynchronization.publishdistrift(p, listImgUrl, datapath, newpath);
                }
            }
        }
    }

    /**
     * 站点分发(增量发布、完全发布、直接发布这些、直接发布这篇、仅发布模板细览)
     *
     * @param publishdistriBution
     * @param targetFile
     * @param datapath
     * @param docId
     * @param siteId
     */
    public static void publishdistriBution(List<PublishdistriBution> publishdistriBution, String targetFile, String datapath, Long docId, Long siteId) {
        if (publishdistriBution != null && publishdistriBution.size() > 0) {
            for(PublishdistriBution p:publishdistriBution){
                String targetType = CMSUtil.stringFormat(p.getTargetType());
                if (targetType.equals("1")) {//跨服务器文件添加
                    sftpsynchronization.publishdistri(p, targetFile, datapath, docId, siteId);
                } else if (targetType.equals("2")) {//系统文件复制
                    filesynchronization.copyFile(targetFile, CMSUtil.stringFormat(p.getDataPath()), siteId, docId);
                } else if (targetType.equals("3")) {//ftp文件复制
                    ftpsynchronization.publishdistri(p, targetFile, datapath, docId, siteId);
                }
            }
        }
    }

}
