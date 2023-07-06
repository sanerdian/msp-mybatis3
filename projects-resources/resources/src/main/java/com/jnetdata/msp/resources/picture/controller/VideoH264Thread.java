package com.jnetdata.msp.resources.picture.controller;

import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.resources.picture.model.Picture;
import com.jnetdata.msp.resources.picture.service.PictureService;
import com.jnetdata.msp.resources.theclient.SystemClient;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: YUEHAO
 * @Date: 2019/11/6
 */
public class VideoH264Thread extends Thread {
    private String ids;
    private SystemClient systemService;
    private PictureService service;
    private String path;
    private String basePath;

    public VideoH264Thread(String ids, String path, String basePath, PictureService service) {
        this.ids = ids;
        this.path = path;
        this.basePath = basePath;
        this.service = service;
    }

    @Autowired
    private ConfigModelService configModelService;

    @Override
    public void run() {
        String[] s = ids.split(",");
        List<Long> idList = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            idList.add(Long.parseLong(s[i]));
        }
        for (int j = 0; j < idList.size(); j++) {
            Picture picture = service.getById(idList.get(j));
            picture.setProcessing(1);
            service.updateById(picture);
            /*创建一个随机的唯一标识*/
            UUID uuid = UUID.randomUUID();
            String newpath = path + "/" + uuid + picture.getName() + "_H264" + ".mp4";
            Picture picture1 = new Picture();
            picture1.setSign(1);
            picture1.setIsNew(1);
            picture1.setColumnId(picture.getColumnId());
            picture1.setName(picture.getName() + "_H264");
            picture1.setForm("mp4");
            picture1.setPath(newpath);
            picture1.setCover(picture.getCover());
            picture1.setDatetime(new Date());
            picture1.setStatus(1);
            picture1.setResParentId(idList.get(j));
            picture1.setLength(picture.getLength());
            picture1.setWidth(picture.getWidth());
            picture1.setHeight(picture.getHeight());
            picture1.setBitRate(picture.getBitRate());
            picture1.setSampleRate(picture.getSampleRate());
            picture1.setAudioNum(picture.getAudioNum());
            picture1.setCollection(picture.getCollection());

//            String url="/files"+path.substring(path.lastIndexOf("/"))+"/"+uuid+picture.getName()+"H264"+"."+picture.getForm();
            String fileUrl = newpath.replace(basePath, "");
            if (File.separator.equals("\\")) {
                fileUrl = fileUrl.replace("\\", "/");
            }
            String url = "/files" + fileUrl;
            picture1.setUrl(url);
            picture.setH264Url(url);
            String[] command = {"ffmpeg", "-i", picture.getPath(), "-vcodec", "h264", newpath, "-hide_banner"};
            Runtime runTime = Runtime.getRuntime();
            if (runTime == null) {
                java.lang.System.err.println("Create runtime false!");
            }
            try {
                java.lang.System.out.println("start transH264----thread");
                System.out.println(Thread.currentThread());
                java.lang.System.out.println(String.join(" ", command));
                Process pro = runTime.exec(command);
                BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                BufferedReader error = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
                //PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
                //String line;
                //while ((line = input.readLine()) != null) {
                //    java.lang.System.out.println("line: " + line);
                //    returnString = returnString + line + "\n";
                //}
                //java.lang.System.out.println("返回值:" + returnString);
                new Thread(() -> {
                    try {
                        String line1 = null;
                        while ((line1 = input.readLine()) != null) {
                            if (line1 != null){}
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally{
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                new Thread(() -> {
                    try {
                        String line2 = null ;
                        while ((line2 = error.readLine()) !=  null ) {
                            if (line2 != null){}
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally{
                        try {
                            error.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                pro.waitFor(); //容易造成主线程的阻塞
                //input.close();
                //output.close();
                pro.destroy();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            picture.setProcessing(2);
            picture.setIsNew(0);
            //get newVideo size
            File pictureFile = new File(picture1.getPath());
            BigDecimal bd = new BigDecimal((double) pictureFile.length() / 1024);
            double size = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            picture1.setFsize(size);
            service.updateById(picture);
            service.insert(picture1);
        }
    }
}
