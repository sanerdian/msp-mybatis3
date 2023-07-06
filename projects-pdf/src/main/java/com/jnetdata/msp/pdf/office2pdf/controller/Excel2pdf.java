package com.jnetdata.msp.pdf.office2pdf.controller;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.aspose.cells.Workbook;
import com.aspose.cells.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspose.cells.SaveFormat;
public class Excel2pdf {
    private static final Logger LOG = LoggerFactory.getLogger(Excel2pdf.class);
    /**
     * word2Pdf
     * @param
     * @author
     * @date: 2019年10月29日 下午5:25:26
     */
    public boolean getLicenseExcel() {
        boolean result = false;
        try {

            InputStream is =Excel2pdf.class.getClassLoader().getResourceAsStream("license.xml");
            //注意此处为对应aspose-cells的jar包
            com.aspose.cells.License aposeLic = new com.aspose.cells.License();

            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取license
     *
     * @return
     */
    public boolean getLicense() {
        boolean result = false;
        try {

            InputStream is =Excel2pdf.class.getClassLoader().getResourceAsStream("license.xml");
            //注意此处为对应aspose-cells的jar包
            License aposeLic = new License();

            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Excel 2 pdf.
     *
     * @param inPath     the excel file Path
     * @param outPath   the pdf file path
     */
    public Boolean excel2Pdf(String inPath, String outPath) {
        Boolean flag = false;
        FileOutputStream fileOS = null;
//         验证License
        if (!getLicense()) {
            LOG.error("验证License失败！");
            return flag;
        }
        try {
            Workbook wb = new Workbook(inPath);
            fileOS = new FileOutputStream(new File(outPath));
            // 保存转换的pdf文件
            wb.save(fileOS, SaveFormat.PDF);
            flag = true;
        } catch (Exception e) {
            LOG.error("error:", e);
        } finally {
            try {
                if(fileOS != null){
                    fileOS.close();
                }
            } catch (IOException e) {
                LOG.error("error:", e);
            }
        }
        return flag;
    }


}
