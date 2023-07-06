package com.jnetdata.msp.pdf.office2pdf.controller;

import com.aspose.slides.License;
import com.aspose.slides.Presentation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class Ppt2pdf {

    /**
     * 验证License 若不验证则转化出的pdf文档会有水印产生
     * @return
     */
    public boolean getLicensePpt(){
        boolean result = false;
        try {

            InputStream is =Ppt2pdf.class.getClassLoader().getResourceAsStream("license.xml");
            //注意此处为对应aspose-slides的jar包
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean ppt2Pdf(String path,String outpath) {
        Boolean flag = false;
        if (!getLicensePpt()) {
            System.out.println("非法------------");
            return flag;
        }
        File file = new File(outpath);
        try {
            Presentation pres = new Presentation(path);//输入pdf路径
            FileOutputStream fileOS = new FileOutputStream(file);
            pres.save(fileOS, com.aspose.slides.SaveFormat.Pdf);
            fileOS.close();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;

    }

}
