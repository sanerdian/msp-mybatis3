package com.jnetdata.msp.manage.publish.core.common.constant;

import java.util.HashMap;

public enum WebsitePublishEnum {

    INDEX("index", "publish", "仅发布站点首页","需传入站点id"),
    INCREMENT("increment", "moreSite", "增量发布这个站点","需传入站点id"),
    ALL("all", "allPublish", "完全发布这个站点","需传入站点id"),
    PREVIEW("preview", "无", "预览站点", "需要传入站点id"),
    CANCEL("cancel", "无", "撤销发布这个站点","需传入站点id");

    private WebsitePublishEnum(String value, String name, String desc, String remark) {
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

    private static HashMap<String, WebsitePublishEnum> map = new HashMap<>();

    public static WebsitePublishEnum readByValue(String value) {
        if(map.isEmpty()){
            synchronized (WebsitePublishEnum.class){
                for(WebsitePublishEnum chnl : WebsitePublishEnum.values()){
                    map.put(chnl.getValue(), chnl);
                }
            }
        }
        return map.get(value);
    }
}
