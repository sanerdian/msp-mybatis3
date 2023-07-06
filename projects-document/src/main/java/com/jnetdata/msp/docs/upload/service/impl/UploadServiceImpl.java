package com.jnetdata.msp.docs.upload.service.impl;

import com.jnetdata.msp.core.context.ThreadLocalContextConstant;
import com.jnetdata.msp.core.context.ThreadLocalContextUtil;
import com.jnetdata.msp.core.service.BaseServiceException;
import com.jnetdata.msp.docs.document.model.Document;
import com.jnetdata.msp.docs.document.service.DocumentService;
import com.jnetdata.msp.docs.upload.config.UploadConfig;
import com.jnetdata.msp.docs.upload.service.UploadService;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.Pair;

import java.io.File;
import java.util.UUID;

/**
 * @author: ZKJW
 * @date: 2019/5/9
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private UploadConfig config;

    @Autowired
    private DocumentService documentService;


    @Override
    public String uploadFile(MultipartFile multipartFile, String uploadType, String description) {

        if (multipartFile.isEmpty()) {
            throw new BaseServiceException("文件不可为空");
        }
        Pair<String, String> pair = saveFile(multipartFile, config.getBasePath());
        return addDocument(pair, multipartFile.getOriginalFilename(), multipartFile.getContentType(), description);
    }

    @SneakyThrows
    private Pair<String, String> saveFile(MultipartFile multipartFile, String basePath) {
        File file = new File(basePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uploadPath = basePath + "/" + UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
        //multipartFile.transferTo(new File(uploadPath));
        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(),new File(uploadPath));
        String uploadHost = (String) ThreadLocalContextUtil.getContext(ThreadLocalContextConstant.UPLOAD_HOST);
        String url = uploadHost + uploadPath;
        return new Pair<>(uploadPath, url);
    }

    /**
     * 添加文件数据表
     *
     * @param pair<本地地址   网络地址>
     * @param filename    文件名
     * @param description 描述
     * @return
     */
    private String addDocument(Pair<String, String> pair, String filename, String contentType, String description) {
        Document document = new Document();
        document.setName(filename);
        document.setContentType(contentType);
        document.setUploadPathName(pair.getKey());
        document.setUrl(pair.getValue());
        document.setDescription(description);
        documentService.insert(document);
        return document.getId().toString();
    }

}
