package com.jnetdata.msp.media.directive.tag.config;

import lombok.Data;

import java.util.Date;

/**
 * @title 文档详情配置
 */
@Data
public class DocumentConfig extends BaseConfigDetail {
    String title;
    String docpuburl;//文章发布路径
    Date ptime;//发布时间

    @Override
    public String getUrl() {
        return this.getDocpuburl();
    }

    @Override
    public String getName() {
        return this.getTitle();
    }
}
