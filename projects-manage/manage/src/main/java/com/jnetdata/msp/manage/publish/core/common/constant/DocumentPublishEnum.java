package com.jnetdata.msp.manage.publish.core.common.constant;

import java.util.HashMap;

public enum DocumentPublishEnum {

    DOC("index", "publishDoc", "发布文档细览","需要传入chnlDocId"),
    CHNL_DOC("docs", "publishChnlDoc", "(批量)发布文档","需要传入chnlDocId"),
    ALL_DOCS("all", "publishAll", "发布栏目下全部文档","需要传入多个chnlId"),
    PREVIEW("preview", "previewChnlDoc", "文档预览","需要传入chnlDocId"),
    CANCEL("cancel", "publishDelDocBach", "批量撤销文档发布","需要传入多个chnlDocId"),
    CANCEL_QUOTE("cancelBatch", "publishKuclyDoc", "批量撤销文档发布","需要传入多个chnlDocId");

    private DocumentPublishEnum(String value, String name, String desc, String remark) {
        this.value = value;
        this.name = name;
        this.desc = desc;
        this.remark = remark;
    }

    public String getDesc() {
        return desc;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }


    /**
     * 状态值
     */
    private String value;
    /**
     * 显示名称
     */
    private String name;
    /**
     * 描述
     */
    private String desc;
    /**
     * 备注
     */
    private String remark;

    private static HashMap<String, DocumentPublishEnum> map = new HashMap<>();

    public static DocumentPublishEnum readByValue(String value) {
        if(map.isEmpty()){
            synchronized (DocumentPublishEnum.class){
                for(DocumentPublishEnum chnl : DocumentPublishEnum.values()){
                    map.put(chnl.getValue(), chnl);
                }
            }
        }
        return map.get(value);
    }
}
