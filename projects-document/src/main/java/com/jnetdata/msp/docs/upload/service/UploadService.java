package com.jnetdata.msp.docs.upload.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 上传文件
 */
public interface UploadService {

    /**
     * 文件上传
     * @param multipartFile 文件
     * @param uploadType 文件上传类型
     * @param description 描述
     * @return
     */
     String uploadFile(MultipartFile multipartFile, String uploadType, String description);
}
