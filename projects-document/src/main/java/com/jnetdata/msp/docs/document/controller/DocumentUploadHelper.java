package com.jnetdata.msp.docs.document.controller;

import com.jnetdata.msp.docs.document.vo.PicUploadVo;
import javafx.util.Pair;
import org.apache.commons.io.FileUtils;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Administrator
 */
public class DocumentUploadHelper {

    private static Pair<String,String> parseBase64File(String base64Data) {

        String dataPrix = "";
        String data = "";
        if (base64Data == null || "".equals(base64Data)) {
            throw new RuntimeException("上传失败，上传图片数据为空");
        } else {
            String[] d = base64Data.split("base64,");
            if (d != null && d.length == 2) {
                dataPrix = d[0];
                data = d[1];
            } else {
                throw new RuntimeException("上传失败，数据不合法");
            }
        }
        String suffix = "";
        if ("data:image/jpeg;".equalsIgnoreCase(dataPrix)) {//data:image/jpeg;base64,base64编码的jpeg图片数据
            suffix = ".jpg";
        } else if ("data:image/x-icon;".equalsIgnoreCase(dataPrix)) {//data:image/x-icon;base64,base64编码的icon图片数据
            suffix = ".ico";
        } else if ("data:image/gif;".equalsIgnoreCase(dataPrix)) {//data:image/gif;base64,base64编码的gif图片数据
            suffix = ".gif";
        } else if ("data:image/png;".equalsIgnoreCase(dataPrix)) {//data:image/png;base64,base64编码的png图片数据
            suffix = ".png";
        } else {
            throw new RuntimeException("上传图片格式不合法");
        }
        String tempFileName = UUID.randomUUID().toString().replaceAll("-","")  + suffix;
        return new Pair<>(tempFileName, data);
    }

    public static FileUploadHelper fromPicUpload(PicUploadVo vo) {
        Pair<String,String> pair = parseBase64File(vo.getBase64File());
        byte[] bs = Base64Utils.decodeFromString(pair.getValue());
        FileUploadHelper helper = new FileUploadHelper(){
            @Override
            public boolean isEmpty() {
                return bs.length==0;
            }
            @Override
            public String getOriginalFilename() {
                return pair.getKey();
            }
            @Override
            public void saveTo(String filePath) throws IOException {
                FileUtils.writeByteArrayToFile(new File(filePath), bs);
            }
            @Override
            public long getSize() {
                return bs.length;
            }
        };
        return helper;
    }


    public static FileUploadHelper[] fromMultipartFile(MultipartFile[] files) {
        FileUploadHelper[] helpers = new FileUploadHelper[files.length];
        for(int i=0; i<files.length; i++) {
            helpers[i] = fromMultipartFile(files[i]);
        }
        return helpers;
    }

    private static FileUploadHelper fromMultipartFile(MultipartFile file) {
        return new FileUploadHelper() {
            @Override
            public boolean isEmpty() {
                return file.isEmpty();
            }
            @Override
            public String getOriginalFilename() {
                return file.getOriginalFilename();
            }
            @Override
            public void saveTo(String filePath) throws IOException {
                //file.transferTo(new File(filePath));
                FileUtils.copyInputStreamToFile(file.getInputStream(),new File(filePath));
            }
            @Override
            public long getSize() {
                return file.getSize();
            }
        };
    }


}
