package com.jnetdata.msp.pdf.office2pdf.controller;

import com.aspose.words.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Word2pdf {

    public static void main(String[] args) {
//        word2pdf("E:/opt/fastdev/upload/992f455f-4653-4425-a25e-2480467c4432.北京工商大学“双一流”学科建设方案.docx","E:/opt/fastdev/upload/testword.pdf");
    }

    /**
     * 验证License 若不验证则转化出的pdf文档会有水印产生
     * @return
     */
    public boolean getLicense(){
        boolean result = false;
        try {
            InputStream is =Word2pdf.class.getClassLoader().getResourceAsStream("license.xml");
            //注意此处为对应aspose-slides的jar包
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * word转pdf
     * @param path      pdf输出路径
     * @param
     * @param
     */
    public boolean word2pdf(String path, String outpath) {
        Boolean flag = false;
        if (!getLicense()) {
            System.out.println("非法");
            return flag;
        }
        //新建一个空白pdf文档
        long old = System.currentTimeMillis();
        File file = new File(outpath);
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Address是将要被转化的word文档
        Document doc = null;
        try {
            doc = new Document(path);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            doc.save(os, SaveFormat.PDF);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        long now = System.currentTimeMillis();
        //转化用时
        System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");
        return flag;
    }

}
