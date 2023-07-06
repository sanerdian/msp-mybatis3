package com.jnetdata.msp.resources.picture.controller;

import com.jnetdata.msp.resources.picture.model.Picture;
import com.jnetdata.msp.resources.picture.model.Watermark;
import com.jnetdata.msp.resources.picture.service.PictureService;
import com.jnetdata.msp.resources.picture.service.WatermarkService;
import com.jnetdata.msp.resources.theclient.SystemClient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author: YUEHAO
 * @Date: 2019/11/1
 */
public class VideoWatermarkThread extends Thread{
    private Long id;
    private String ids;
    private WatermarkService watermarkService;
    private SystemClient systemService;
    private PictureService service;
    public VideoWatermarkThread(String ids, Long id, WatermarkService watermarkService, SystemClient systemService, PictureService service){
        this.watermarkService = watermarkService;
        this.systemService =systemService;
        this.service =service;
        this.ids = ids;
        this.id = id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public void setIds(String ids){
        this.ids = ids;
    }

    @Override
    public void run(){
        String[] s = ids.split(",");

        /*根据id获取水印对象*/
        Watermark watermark = watermarkService.getById(id);
        int location = watermark.getLocation();
        String wmkpath = watermark.getWmkPath();
        List<Long > longList = new ArrayList<>();
        for(int i = 0;i<s.length;i++){
            longList.add(Long.parseLong(s[i]));
        }
        for(int j=0;j<longList.size();j++){
            Picture picture = service.getById(longList.get(j));
            picture.setProcessing(1);
            service.updateById(picture);
            Picture picture1 = new Picture();
            String path = systemService.getById(1167404682386190338L).getValue();
            UUID uuid = UUID.randomUUID();
            String newpath1 = path+"/"+uuid+".mp4";
            picture1.setPath(newpath1);
            service.insert(picture1);
            picture.setNewvideoID(picture1.getId());
            String newpath = "/files"+path.substring(path.lastIndexOf("/"))+"/"+uuid+".mp4";
            String command = null;
            if(location==1){
                command  = "ffmpeg -i"+" "+picture.getPath()+" "+"-i "+wmkpath+" -filter_complex \"[1:v]colorkey=white:0.6:1.0[ckout];[0:v][ckout]overlay=x=0:y=0[out]\" -map \"[out]\" -movflags faststart "+newpath1;
            }else if(location==3){
                command  = "ffmpeg -i"+" "+picture.getPath()+" "+"-i "+wmkpath+" -filter_complex \"[1:v]colorkey=white:0.6:1.0[ckout];[0:v][ckout]overlay=x=W-w:y=0[out]\" -map \"[out]\" -movflags faststart "+newpath1;
            }else if(location==7){
                command  = "ffmpeg -i"+" "+picture.getPath()+" "+"-i "+wmkpath+" -filter_complex \"[1:v]colorkey=white:0.6:1.0[ckout];[0:v][ckout]overlay=x=0:y=H-h[out]\" -map \"[out]\" -movflags faststart "+newpath1;
            }else if(location==9){
                command  = "ffmpeg -i"+" "+picture.getPath()+" "+"-i "+wmkpath+" -filter_complex \"[1:v]colorkey=white:0.6:1.0[ckout];[0:v][ckout]overlay=x=W-w:y=H-h[out]\" -map \"[out]\" -movflags faststart "+newpath1;
            }
            String[] commend = command.split(" ");
            Runtime runtime = null;
            String returnString = "";
            Process pro = null;
            Runtime runTime = Runtime.getRuntime();
            if (runTime == null) {
                java.lang.System.err.println("Create runtime false!");
            }
            try {
                java.lang.System.out.println(command);
//                pro = runTime.exec(new String[]{"sh","-c",command});
                pro = runTime.exec(commend);

                BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
                String line;
                while ((line = input.readLine()) != null) {
                    java.lang.System.out.println("line: " + line);
                    returnString = returnString + line + "\n";
                }

                java.lang.System.out.println("返回值:" + returnString);
                input.close();
                output.close();
                pro.destroy();
                picture.setProcessing(2);
                picture.setVideoUrl(newpath);
                service.updateById(picture);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
