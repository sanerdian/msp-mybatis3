package com.jnetdata.msp.docs.upload.controller;

import com.jnetdata.msp.docs.upload.service.UpLoadDataService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.web.BaseController;
import org.thenicesys.web.JsonResult;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by WFJ on 2019/4/18.
 */

@Controller
@Slf4j
@RequestMapping("/simple/upLoadData")
@Deprecated
public class UpLoadDataController extends BaseController {
    @Autowired
    UpLoadDataService service;

    @ApiOperation(value = "上传文件", notes="上传文件")
    @PostMapping("/upload")
    @ResponseBody
    public JsonResult uploadExcel(MultipartFile file, @RequestParam("type") int type)throws Exception {
        return JsonResult.renderSuccess((Object)getService().UpLoadData(file,type));
    }

    @ApiOperation(value = "上传文件", notes="上传文件")
    @PostMapping("/file")
    @ResponseBody
    public JsonResult file(MultipartFile file, @RequestParam("type") String type)throws Exception {
        ResourceBundle resource = ResourceBundle.getBundle("config");
        String basePath = resource.getString("upload_base_path");
        String path = resource.getString("upload_path_"+type);
        String fileHead = resource.getString("file_head");
        String temp = file.getOriginalFilename();
        Map<String,String> map = new HashMap();
        try {
            String tempPath = basePath + path + File.separator;
            String newName = String.valueOf(System.currentTimeMillis())+"-"+temp;
            File tempFile = new File(tempPath+newName);
            if (!tempFile.exists()&&!tempFile.isDirectory()){
                tempFile.mkdir();
            }
            //file.transferTo(tempFile);
            FileUtils.copyInputStreamToFile(file.getInputStream(),tempFile);
            if(File.separator.equals("\\")){
                path = path.replace("\\","/");
            }
            map.put("url",fileHead+""+path+"/"+newName);
            map.put("path",tempPath+newName);
            map.put("fileName",temp);
            map.put("newName",newName);
        }catch (Exception e){
            System.out.print(e);
            return renderError("上传文件失败");
        }

        return JsonResult.renderSuccess(map);
    }

    private UpLoadDataService getService() {
        return this.service;
    }

}
