package com.jnetdata.msp.resources.file.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.resources.picture.model.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface FileService extends BaseService<Picture> {
    //注册用户上传头像
    Map<String,Object> upload(MultipartFile file);

    //非标准接口
    Map<String,Object> upload(MultipartFile file,boolean bool);
    Map<String,Object> upload(String fileUrl,boolean bool);
    //为url对应的视频（必须是一体化上传的）抽取截图
    List<String> grabberImage(String videoUrl,Integer counts, Integer second, Integer inteval);
    String pdf2html(InputStream in) throws IOException;

    Map saveBase64Image(String src) throws IOException;
}
