package com.jnetdata.msp.docs.upload.controller;

import com.jnetdata.msp.docs.upload.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.web.JsonResult;

/**
 * @author: ZKJW
 * @date: 2019/5/9
 */
@Controller
@RequestMapping("/docs/upload")
@Api(value = "UploadController",description = "上传文件")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @ApiOperation("文件上传")
    @PostMapping("/uploadFile")
    @ResponseBody
    public JsonResult<Long> uploadFile(@RequestParam("file") MultipartFile file,
                                       @RequestParam("uploadType") String uploadType,
                                       @RequestParam(value = "description", defaultValue = "")String description) {
        String result = uploadService.uploadFile(file, uploadType, description);
        return JsonResult.success(result);
    }




}
