package com.jnetdata.msp.log.reallog.controller;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TF on 2019/3/28.
 */

public class RealLogWebsocket {

    private final String LOG_PATH = "D://logtest/test.log";

    public Map<String,Object> getLog(int pointer){
        int num = getLogLine(LOG_PATH);
        if(pointer==0){
            pointer = getPrinter(num);
        }

        Map<String,Object> map = readLog(pointer);

        return map;
    }

    /**
     *读取日志文件
     * @return
     */
    public Map<String,Object> readLog(int pointer){
        List<String> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();

        try{
            File file = new File(LOG_PATH);
            RandomAccessFile raf=new RandomAccessFile(file, "r");
            raf.seek(pointer);//移动文件指针位置
            String line =null;
            //循环读取
            while((line = raf.readLine())!=null){
                if(line.equals("")){
                    continue;
                }
                //打印读取的内容,并将字节转为字符串输入，做转码处理，要不然中文会乱码
                line = new String(line.getBytes("ISO-8859-1"),"gb2312");
                list.add(line);
            }
            map.put("point",raf.getFilePointer());
            map.put("list",list);
        }catch(Exception e){
            System.out.println("异常："+ e.getMessage());
            e.printStackTrace();
        }

        return map;
    }


    /**
     * 获取文件行数
     * @param path 文件地址
     * @return
     */
    public int getLogLine(String path) {
        int linenumber = 0;
        try{
            File file =new File(path);

            if(file.exists()){
                FileReader fr = new FileReader(file);
                LineNumberReader lnr = new LineNumberReader(fr);

                while (lnr.readLine() != null){
                    linenumber++;
                }
                System.out.println("Total number of lines : " + linenumber);
                lnr.close();
            }else{
                System.out.println("File does not exists!");
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        return linenumber;

    }

    /**
     * 获取日志初始指针
     * @param num 日志行数
     * @return
     */
    public int getPrinter(int num){
        int pointer = 0;
        if(num>100){
            pointer = num - 100;
        }
        return pointer;
    }
}
