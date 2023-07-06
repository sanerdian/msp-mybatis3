package com.jnetdata.msp.manage.publish.explain.common.constant;

import lombok.Getter;

/**
 * Created by xujian on 2017/10/20.
 * 支持的指标
 */
public enum TagsEnum {
    JNET_SITE(TagsConstant.JNET_SITE, "站点"),
    JNET_CHANNELS(TagsConstant.JNET_CHANNELS, "栏目集合"),
    JNET_CHANNEL(TagsConstant.JNET_CHANNEL, "栏目信息"),
    JNET_CHANNELLOGO(TagsConstant.JNET_CHANNELLOGO, "栏目logo"),
    JNET_DOCUMENTS(TagsConstant.JNET_DOCUMENTS, "文档列表"),
    JNET_DOCUMENT(TagsConstant.JNET_DOCUMENT, "文档"),
    JNET_METADATAS(TagsConstant.JNET_METADATAS, "元数据列表"),
    JNET_METADATA(TagsConstant.JNET_METADATA, "元数据文档"),
    JNET_PREDOCUMENTS(TagsConstant.JNET_PREDOCUMENTS, "文档前集合"),
    JNET_NEXTDOCUMENTS(TagsConstant.JNET_NEXTDOCUMENTS, "文档后集合"),
    JNET_ROWNUM(TagsConstant.JNET_ROWNUM, "文档序号"),
    JNET_APPENDIXS(TagsConstant.JNET_APPENDIXS, "文档附件集合"),
    JNET_APPENDIX(TagsConstant.JNET_APPENDIX, "文档附件"),
    JNET_TEMPLATE(TagsConstant.JNET_TEMPLATE, "嵌套模板"),
    ;

    @Getter
    private final String code;
    @Getter
    private final String name;

    TagsEnum(final String code, final String name) {
        this.code = code;
        this.name = name;
    }
}
