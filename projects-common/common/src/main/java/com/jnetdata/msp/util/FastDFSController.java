package com.jnetdata.msp.util;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.util.FastDFSClient;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.web.JsonResult;

@RestController
@RequestMapping("/fdfs")
public class FastDFSController extends BaseController {

    @Autowired
    private FastDFSClient fdfsClient;

    /**
     * 文件上传
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    public JsonResult<Map<String, Object>> upload(MultipartFile file) throws Exception{

        String url = fdfsClient.uploadFile(file);

        Map<String,Object> result = new HashMap<>();
        result.put("url", url);

        return renderSuccess(result);
    }

    /**
     * 文件下载
     * @param fileUrl  url 开头从组名开始
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "文件下载")
    @GetMapping("/download")
    public void  download(String fileUrl, HttpServletResponse response) throws Exception{

        byte[] data = fdfsClient.download(fileUrl);

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("test.jpg", "UTF-8"));

        // 写出
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.write(data, outputStream);
    }
}
