package com.jnetdata.msp.resources.file.service.Impl;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

public class VideoTimeUtils {
    public static Map videoInfo(File videFile){
        Map<String,Number> map=new HashMap<>();
        FFmpegFrameGrabber grabber = null;
        try {
            grabber = new FFmpegFrameGrabber(videFile);
            grabber.start();
            // 获取视频时长， / 1000000 将单位转换为秒
            long lengthInTime = grabber.getLengthInTime();
            map.put("duration",lengthInTime/1000000);
            map.put("width",grabber.getImageWidth());
            map.put("height",grabber.getImageHeight());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (grabber != null) {
                    grabber.stop();
                    grabber.release();
                }
            } catch (FFmpegFrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    /**
     * 从指定视频文件中抽取图片
     * @param videFile 视频文件
     * @param count 抽取图片数
     * @param second second单位为秒，最大值为视频总时长。该值未指定时，是从视频文件的随机时刻抽取图片。该值存在时，需结合inteval
     * @param inteval intval单位为秒，当second存在而inteval不存在时，仅在该秒内抽取图片（例如second为5，则在[5,6)这个区间内抽取图片）。
     *                当second与inteval同时存在时，在指定的播放序列中抽取图片（例如second为5，inteval为3，则在5，8，11，14，17，20....这些时刻抽取图片）
     * @return
     */
    public static List<File>  grabberFFmpegImage(File videFile, int count,Integer second,Integer inteval){
        List<File> images = new ArrayList<>();
        FFmpegFrameGrabber grabber = null;
        String folder=videFile.getParent();
        String videoName=videFile.getName();
        if(videoName.contains(".")){
            videoName=videoName.substring(0,videoName.lastIndexOf("."));
        }
        try {
            grabber = new FFmpegFrameGrabber(videFile);
            grabber.start();
            // 获取视频总帧数
            // 获取视频时长， / 1000000 将单位转换为秒
            long lengthInTime = grabber.getLengthInTime();
            Random random=new Random();
            for (int i = 0; i < count; i++) {
                // 跳转到响应时间
                long num=-1;
                if(second==null){
                    num=Math.abs(random.nextLong()%lengthInTime);
                }else if(inteval==null){
                    num=second*1000000L+random.nextInt(1000)*1000;
                }else {
                    num=(second+inteval*i)*1000000L;
                }
                if(num>=lengthInTime||num<0){
                    continue;
                }
                grabber.setVideoTimestamp(num);
                Frame f = grabber.grabImage();
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage bi = converter.getBufferedImage(f);
                String imageName = videoName+"_"+num+".jpg";
                File out = new File(folder, imageName);
                ImageIO.write(bi, "jpg", out);
                images.add(out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (grabber != null) {
                    grabber.stop();
                    grabber.release();
                }
            } catch (FFmpegFrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
        return images;
    }

    public static void main(String[] args) {
        File file = new File("D:\\BaiduNetdiskDownload\\(001).mp4");
//        grabberFFmpegImage(file,10,21,null);
//        grabberFFmpegImage(file,10,null,null);
//        grabberFFmpegImage(file,10000,0,10);
        System.out.println(videoInfo(file));
    }
}
