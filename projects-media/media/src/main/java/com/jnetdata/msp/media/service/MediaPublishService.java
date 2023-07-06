package com.jnetdata.msp.media.service;

import java.io.IOException;

public interface MediaPublishService {
    /**
     * 发布详情页
     * @param docid 新闻id
     * @return
     */
    String publishDetail(Long docid) throws IOException;

    /**
     * 发布栏目的列表页
     * @param columnid 栏目id
     * @param isPage
     * @param pageSize
     * @return
     */
    String publishList(Long columnid, boolean isPage, Integer pageSize) throws IOException;

    /**
     * 发布栏目，需发布栏目的列表页及栏目下的所有详情页
     * @param columnid
     * @return
     */
    String publishColumn(Long columnid) throws IOException;

    /**
     * 撤销发布详情页
     * @param docid 新闻id
     * @return
     */
    void revokeDetail(Long docid) throws IOException;

    /**
     * 预览融媒体
     * @param docid 融媒体id
     * @return 地址
     */
    String previewDetail(Long docid) throws IOException;

    String publishHome(Long siteid) throws IOException;
}
