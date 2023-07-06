package com.jnetdata.msp.visual.util;

import java.io.*;

/**
 * 将内容追加到文件尾部
 */
public class FileUtil {
    /**
     * A方法追加文件：使用RandomAccessFile
     * @param fileName 文件名
     * @param content 追加的内容
     */
    public static void appendMethodA(String fileName,
                                     String content){
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");

            // 文件长度，字节数
            long fileLength = randomFile.length();

            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    /**
     * B方法追加文件：使用FileWriter
     * @param fileName
     * @param content
     */
    public static void appendMethodB(String fileName, String content){
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void inputstreamtofile(InputStream ins, File file){
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {


        String fileName = "D:/data/Desktop/20221114/list.js";
//        String content = "\nvar serviceId = \"/exercise\";";
        StringBuilder builder = new StringBuilder();
        builder.append("\nvar serviceId = \"/exercise\";")
                .append("\nvar url = \"/computer\";")
                .append("\nvar columns = [")
                .append("\n\t{type: 'checkbox', fixed: 'left'}")
                .append("\n\t, {type: 'numbers', title: '序号', fixed: 'left'}")
                .append("\n\t, {field: 'computerCode', title: '电脑编号'}")
                .append("\n\t, {field: 'userPerson', title: '使用员工名称'}")
                .append("\n\t, {field: 'startTime', title: '开始使用时间'}")
                .append("\n];")
                .append("\nsetListData(serviceId, url, curr, defaultPageSize,{});")
                .append("\ngetSearch(serviceId, url, curr, defaultPageSize);");
//按方法A追加文件
//        AppendToFile.appendMethodA(fileName, content);
//        AppendToFile.appendMethodA(fileName, "append end. n");

//按方法B追加文件
        FileUtil.appendMethodB(fileName, builder.toString());
        FileUtil.appendMethodB(fileName, "\nappend end. n");

    }
}
