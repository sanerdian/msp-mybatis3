package com.jnetdata.msp.resources.picture.controller;

import com.jnetdata.msp.resources.picture.model.Picture;
import com.jnetdata.msp.resources.picture.service.PictureService;
import com.jnetdata.msp.resources.theclient.SystemClient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author: YUEHAO
 * @Date: 2019/11/6
 */
public class MP3Thread extends Thread{
    private String ids;
    private SystemClient systemService;
    private PictureService service;
    private String path;
    private String basePath;
    public MP3Thread (String ids,String path,String basePath,PictureService service){
        this.ids = ids;
        this.path = path;
        this.basePath = basePath;
        this.service = service;
    }
    @Override
    public void run(){

        String[] s = ids.split(",");
        List<Long> longList = new ArrayList<>();
        for(int i = 0;i<s.length;i++){
            longList.add(Long.parseLong(s[i]));
        }
        for(int j = 0;j<longList.size();j++){
            Picture picture = service.getById(longList.get(j));
            picture.setProcessing(1);
            service.updateById(picture);
            UUID uuid = UUID.randomUUID();
            String newpath = path+"/"+uuid+picture.getName()+"_H264"+".mp3";
            //String command  = "ffmpeg -i"+" "+picture.getPath()+" "+"-f mp3 -acodec libmp3lame -y "+newpath;
            String[] command  = {"ffmpeg", "-i", picture.getPath(), "-f", "mp3", "-acodec", "libmp3lame", "-y", newpath};
            StringBuilder returnString = new StringBuilder();
            Process pro = null;
            Runtime runTime = Runtime.getRuntime();
            if (runTime == null) {
                java.lang.System.err.println("Create runtime false!");
            }
            try {
                java.lang.System.out.println(String.join(" ", command));
                //pro = runTime.exec(new String[]{"sh","-c",command});
                pro = runTime.exec(command);

                BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                BufferedReader error = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
                //PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
                //String line;
                //while ((line = input.readLine()) != null) {
                //    java.lang.System.out.println("line: " + line);
                //    returnString = returnString + line + "\n";
                //}

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
                            if (line2 != null){
                                returnString.append(line2).append("\n");
                            }
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
                pro.waitFor();

                java.lang.System.out.println("返回值:" + returnString);
                //释放资源
                input.close();
                //释放资源
                error.close();
                //output.close();
                pro.destroy();
                picture.setProcessing(2);

                String fileUrl = newpath.replace(basePath,"");
                if(File.separator.equals("\\")){
                    fileUrl = fileUrl.replace("\\","/");
                }
                String url="/files"+fileUrl;

                picture.setMp3url(url);
                service.updateById(picture);
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
