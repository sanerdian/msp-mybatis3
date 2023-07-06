package com.jnetdata.msp.log.reallog.service.impl;

import com.jnetdata.msp.log.reallog.controller.RealLogWebsocket;
import com.jnetdata.msp.log.reallog.service.RealLogService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * Created by TF on 2019/3/28.
 */
@Service
public class RealLogServiceImpl implements RealLogService {

    @Override
    public Map<String,Object> getLog(int pointer) {

        RealLogWebsocket scoket = new RealLogWebsocket();

        Map<String,Object> map = scoket.getLog(pointer);

        return map;
    }

    /**
     * 读取日志文件
     * @return
     */
    @Override
    public List<String> readLog() {
        List<String> stringList=null;
        RandomAccessFile rf=null;
        Map<Object,String> newFile=new HashMap<>();
        try {
            //获取文件夹中文件路径
            File dir=new File(System.getProperty("catalina.home")+File.separator+"logs");
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.getName().startsWith("catalina")){
                    newFile.put(file.lastModified(),file.getAbsolutePath());
                }
            }
            //对文件排序,找最新文件
            Object[] obj = newFile.keySet().toArray();
            Arrays.sort(obj);
            stringList=new ArrayList<>();
            rf=new RandomAccessFile(newFile.get(obj[obj.length-1]),"r");
            long len=rf.length();
            long start=rf.getFilePointer();
            long nextEnd=start+len-1;
            rf.seek(nextEnd);//定位
            int count=1;
            int c=-1;
            String temp=null;
            stringList.add(new String(rf.readLine().getBytes("ISO-8859-1"),"gbk"));
            while (nextEnd>start){
                c=rf.read();
                if (c=='\n'||c=='\r'){
                    temp=new String(rf.readLine().getBytes("ISO-8859-1"),"gbk");
                    stringList.add(temp);
                    count++;
                    nextEnd--;
                }
                nextEnd--;
                rf.seek(nextEnd);
                //读取最后一行
                if(nextEnd==0){
                    stringList.add(new String(rf.readLine().getBytes("ISO-8859-1"),"gbk"));
                }
                if (count==100){
                    break;
                }
            }
         return stringList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                rf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
