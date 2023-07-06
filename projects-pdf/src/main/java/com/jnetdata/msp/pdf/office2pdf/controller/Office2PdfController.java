package com.jnetdata.msp.pdf.office2pdf.controller;

import com.aspose.words.Document;
//import com.aspose.words.SaveFormat;
import com.jnetdata.msp.core.controller.BaseController;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.web.JsonResult;
import com.aspose.words.License;
import com.aspose.cells.Workbook;
import com.aspose.cells.SaveFormat;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 活动表 前端控制器
 * </p>
 *
 * @author zyj
 * @since 2021-06-22
 */
@Controller
@RequestMapping("/office2pdf")
public class Office2PdfController extends BaseController {


    @Autowired
    public Office2PdfController() {

    }

    @ApiOperation(value = "文档转pdf", notes="文档转pdf")
    @GetMapping(value = "/readpdf")
    @ResponseBody
    public JsonResult readpdf(String path) {
        String dir = path;
        if(dir.startsWith("/files")){
            dir = dir.replace("/files","/opt/fastdev/upload");
        }
        String savepath = "/opt/fastdev/upload/webfile/test.pdf";
        String savefile = "/files/webfile/test.pdf";

        File dir1 = new File("/opt/fastdev/upload/webfile");
        if (!dir1.exists()) {
            dir1.mkdirs();
        }

        Boolean flag = false;
        if(dir.endsWith("doc")||dir.endsWith("docx")){
            Word2pdf word2pdf = new Word2pdf();
            flag = word2pdf.word2pdf(dir,savepath);
        } else if(dir.endsWith("xls")||dir.endsWith("xlsx")){
            Excel2pdf excel2pdf = new Excel2pdf();
            flag = excel2pdf.excel2Pdf(dir,savepath);
        } else if(dir.endsWith("ppt")||dir.endsWith("pptx")){
            Ppt2pdf ppt2pdf = new Ppt2pdf();
            flag = ppt2pdf.ppt2Pdf(dir,savepath);
        }
        if(flag){
            return renderSuccess(savefile);
        } else {
            return renderError();
        }
    }


}

