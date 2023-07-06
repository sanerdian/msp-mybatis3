package com.jnetdata.msp.docs.document.controller;

import java.io.IOException;

/**
 * 文件上传帮助接口
 */
public interface FileUploadHelper {
    /**
     * 是否空
     * @return
     */
    boolean isEmpty();

    /**
     * 原文件名
     * @return
     */
    String getOriginalFilename();

    /**
     * 保存到
     * @param filePath
     * @throws IOException
     */
    void saveTo(String filePath) throws IOException;

    /**
     * 文件尺寸
     * @return
     */
    long getSize();

}
