package com.jnetdata.msp.resources.file.service.Impl;

import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.resources.file.mapper.FileMapper;
import com.jnetdata.msp.resources.file.service.FileService;
import com.jnetdata.msp.resources.picture.model.Picture;
import com.spire.pdf.PdfPageBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.bytedeco.javacpp.Loader;

import org.fit.pdfdom.PDFDomTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import sun.misc.BASE64Decoder;

import java.awt.geom.Rectangle2D;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Slf4j
@Service
public class FileServiceImpl extends BaseServiceImpl<FileMapper, Picture> implements FileService {
    @Autowired
    private ConfigModelService configModelService;

    protected PropertyWrapper<Picture> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("name")
                .eq("mark")
                .getWrapper();
    }


    /*添加水印*/
    static void insertWatermark(PdfPageBase page,String path) {
        path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + path;
        page.setBackgroundImage(path);
        //设置背景区域
        Rectangle2D.Float rect = new Rectangle2D.Float();
        rect.setRect(100, 100, 500, 500);
        page.setBackgroundRegion(rect);
    }

    /**
     * 注册用户上传头像
     * @param file
     */
    @Override
    public Map<String,Object> upload(MultipartFile file) {
        return upload(file,false);
    }

    //非标准接口
    /**
     * 注册用户上传头像
     * @param file
     */
    @Override
    public Map<String,Object> upload(MultipartFile file,boolean bool) {
        Map<String,Object> resultMap = new HashMap<>();
        String fileName=file.getResource().getFilename();//原文件名
        if(ObjectUtils.isEmpty(fileName)){
            resultMap.put("err","未找到要上传的文件。");
            return resultMap;
        }

        String type=null;
        String targetFileName;
        String uuid=UUID.randomUUID().toString();
        if(fileName.contains(".")){
            type=fileName.substring(fileName.lastIndexOf(".")+1);
            targetFileName=uuid+"."+type;
        }else {
            targetFileName=uuid;
        }
        String folder = "";
        if(bool){
            folder = configModelService.getUploadPath(ConfigModel.dir_headimg,"dir_upload_base_cdn");
        }else{
            folder = configModelService.getUploadPath(ConfigModel.dir_headimg);
        }
        File targetFile = new File(folder, targetFileName);
        targetFile.getParentFile().mkdirs();
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);
        } catch (IOException e) {
            e.printStackTrace();
            resultMap.put("err","保存文件遇到异常"+e.getMessage());
            return resultMap;
        }
        String url = getUrlByFile(targetFile, bool);
        resultMap.put("url",url);
        resultMap.put("title",targetFileName);
        resultMap.put("original",fileName);
        resultMap.put("type",type);
        resultMap.put("size", targetFile.length());
        resultMap.put("path",targetFile.getAbsolutePath());
        if(file.getContentType().contains("video")){//如果是视频，则截取视频图片
            List<File> images = VideoTimeUtils.grabberFFmpegImage(targetFile, 1, 0, null);
            if(!ObjectUtils.isEmpty(images)){
                resultMap.put("cutPicUrl",getUrlByFile(targetFile,bool));
            }else {
                resultMap.put("cutPicErr","截取视频封面失败:");
            }
            resultMap.putAll(VideoTimeUtils.videoInfo(targetFile));
//            try {
//                vz(targetFile.getAbsolutePath());
//            } catch (Exception e) {
//                resultMap.put("err","视频转码失败:"+e.getMessage());
//                e.printStackTrace();
//            }
        }
        return resultMap;
    }
    @Override
    public Map<String,Object> upload(String fileUrl,boolean bool){
        Map<String,Object> resultMap = new HashMap<>();
        String fileName=fileUrl.replace("\\","/");
        if(fileName.contains("?")){
            fileName=fileName.substring(0,fileName.indexOf("?"));
        }
        fileName=fileName.substring(fileUrl.lastIndexOf("/")+1);//原文件名
        String type=null;
        String targetFileName;
        String uuid=UUID.randomUUID().toString();
        if(fileName.contains(".")){
            type=fileName.substring(fileName.lastIndexOf(".")+1);
            targetFileName=uuid+"."+type;
        }else {
            targetFileName=uuid;
        }
        String folder = "";
        if(bool){
            folder = configModelService.getUploadPath(ConfigModel.dir_headimg,"dir_upload_base_cdn");
        }else{
            folder = configModelService.getUploadPath(ConfigModel.dir_headimg);
        }
        File targetFile = new File(folder, targetFileName);
        targetFile.getParentFile().mkdirs();
        try{
            downloadRemoteFile(new URL(fileUrl),targetFile);
        }catch (IOException e){
            e.printStackTrace();
            resultMap.put("err","保存文件遇到异常"+e.getMessage());
            return resultMap;
        }
        String url = getUrlByFile(targetFile, bool);
        resultMap.put("url",url);
        resultMap.put("title",targetFileName);
        resultMap.put("original",fileName);
        resultMap.put("type",type);
        resultMap.put("size", targetFile.length());
        resultMap.put("path",targetFile.getAbsolutePath());
        return resultMap;
    }
    private void downloadRemoteFile(URL url, File file) throws IOException {
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        HttpURLConnection httpUrl=null;
        try{
            httpUrl= (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(file));
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            bos.flush();
        }finally {
            if(httpUrl!=null){
                httpUrl.disconnect();
            }
            if(bis!=null){
                bis.close();
            }
            if(bos!=null){
                bos.close();
            }
        }
    }
    public List<String> grabberImage(String videoUrl, Integer counts, Integer second, Integer inteval){
        if(counts==null||counts<=0||counts>100){
            counts=10;
        }
        List<String> imageUrls=new ArrayList<>();
        File videoFile=getFileByUrl(videoUrl);
        List<File> files = VideoTimeUtils.grabberFFmpegImage(videoFile, counts, second, inteval);
        if(files!=null){
            for (File file:files){
                String url=getUrlByFile(file,videoUrl.contains("/cdn/"));
                if(url!=null){
                    imageUrls.add(url);
                }
            }
        }
        return imageUrls;
    }
    private File getFileByUrl(String url){
        File file=null;
        if(url!=null){
            url=url.replace("\\","/");
            boolean bool=url.contains("/cdn/");
            String baseUrl;
            String basePath;
            String path=null;
            if(bool){
                baseUrl="/cdn";
                basePath = configModelService.getBaseUploadPath("dir_upload_base_cdn");
                int index=url.indexOf(baseUrl+"/");
                if(index>-1){
                    path=url.substring(index+baseUrl.length());
                    file=new File(basePath,path);
                }
            }else {
                baseUrl="/files";
                basePath = configModelService.getBaseUploadPath();
                int index=url.indexOf(baseUrl+"/");
                if(index>-1){
                    path=url.substring(index+baseUrl.length());
                    file=new File(basePath,path);
                }
            }
        }
        if(file!=null&&file.exists()){
            return file;
        }else {
            throw new RuntimeException("未找到该文件！请确认该url是本系统上传文件后生成的url！");
        }
    }
    @Override
    public String pdf2html(InputStream in) throws IOException {
        PDDocument pdf = PDDocument.load(in);
        PDFDomTree parser = new PDFDomTree();
        String html=parser.getText(pdf);
        pdf.close();
        return html;
    }
    public Map saveBase64Image(String base64Data) throws IOException {
        String suffix="png";
        int start=base64Data.indexOf("/");
        int end=base64Data.indexOf(";");
        if(start>-1&&start<end){
            suffix=base64Data.substring(start+1,end);
        }
        String path = configModelService.getUploadPath(ConfigModel.dir_headimg);
        File picture=new File(path,UUID.randomUUID().toString()+"."+suffix);
        String imgBase64=base64Data.substring(base64Data.indexOf("base64,")+"base64,".length());
        BASE64Decoder decoder = new BASE64Decoder();
        //Base64解码
        byte[] b;
        b = decoder.decodeBuffer(imgBase64);
        OutputStream out = new FileOutputStream(picture);
        out.write(b);
        out.flush();
        out.close();
        Map resultMap=new HashMap();
        String url = getUrlByFile(picture, false);
        resultMap.put("url",url);
        resultMap.put("title",picture.getName());
        resultMap.put("type",suffix);
        resultMap.put("size", picture.length());
        resultMap.put("path",picture.getAbsolutePath());
        return resultMap;
    }
    private String getUrlByFile(File file, boolean bool) {
        String url;
        String basePath = "";
        if(bool){
            basePath = configModelService.getBaseUploadPath("dir_upload_base_cdn");
        }else{
            basePath = configModelService.getBaseUploadPath();
        }
        basePath=basePath.replace("\\","/");
        String fileUrl = file.getParent().replace("\\","/").replace(basePath,"");
        if(bool){
            url="/cdn"+fileUrl+"/"+file.getName();
        }else{
            url="/files"+fileUrl+"/"+file.getName();
        }
        return url;
    }

    private void vz(String path) throws IOException, InterruptedException, ClassNotFoundException {
        Class.forName("org.bytedeco.ffmpeg.global.avdevice");
        Loader.load(org.bytedeco.ffmpeg.global.avdevice.class);
        Loader.load(org.bytedeco.ffmpeg.presets.avdevice.class);
        String ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);
        String path1080 = path.replace(".mp4", "_1080.mp4");
//        String path720 = path.replace(".mp4", "_720.mp4");
//        String path480 = path.replace(".mp4", "_480.mp4");
        ProcessBuilder pb = new ProcessBuilder(ffmpeg, "-i", path,"-vf","scale=1920:1080", "-vcodec", "h264", path1080);
//        ProcessBuilder pb1 = new ProcessBuilder(ffmpeg, "-i", path,"-vf","scale=1280:720", "-vcodec", "h264", path720);
//        ProcessBuilder pb2 = new ProcessBuilder(ffmpeg, "-i", path,"-vf","scale=720:480", "-vcodec", "h264", path480);
        pb.inheritIO().start().waitFor();
//        pb1.inheritIO().start().waitFor();
//        pb2.inheritIO().start().waitFor();
    }
}
