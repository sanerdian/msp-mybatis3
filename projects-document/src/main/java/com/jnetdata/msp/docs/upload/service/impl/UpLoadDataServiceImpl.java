package com.jnetdata.msp.docs.upload.service.impl;

import com.jnetdata.msp.docs.upload.service.UpLoadDataService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Created by WFJ on 2019/4/18.
 */
@Service
public class UpLoadDataServiceImpl implements UpLoadDataService {
    //头像
    private final int HEAD_PIC = 1;
    private final String HEAD_PATH = "/picture/head";


    @Override
    public String UpLoadData(MultipartFile file,int type) {
        String path ="";
        if(type==HEAD_PIC){
            path = saveData(file,HEAD_PATH);
        }
        return path;
    }

    /**
     * 存储文件
     * @param file 文件
     * @param path 地址
     * @return
     */
    private String saveData(MultipartFile file,String path){
        String temp = file.getOriginalFilename();
        try {
            String tempPath = path+"/"+String.valueOf(System.currentTimeMillis())+"-"+temp;
            File tempFile = new File(tempPath);
            setFolder(tempFile);
            //file.transferTo(tempFile);
            FileUtils.copyInputStreamToFile(file.getInputStream(),tempFile);
            return tempPath;
        }catch (Exception e){
            System.out.print(e);
            return "上传文件失败";
        }
    }

    /**
     * 判断文件夹是否存在，不存在进行创建
     * @param file 文件
     */
    private void setFolder(File file){
        if  (!file.exists()  && !file.isDirectory()) {
            file .mkdirs();
        }

    }

}
