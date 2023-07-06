package com.jnetdata.msp.manage.publish.core.common.constant;

import java.util.HashMap;

public enum ChannelPublishEnum {

    INDEX("index", "publishChnl-1", "仅发布栏目首页", "需要传入栏目id"),
    INCREMENT("increment", "publishChnl-2", "增量发布栏目", "需要传入栏目id"),
    ALL("all", "publishChnl-2", "完全发布栏目", "需要传入栏目id"),
    PREVIEW("preview", "无", "预览栏目", "需要传入栏目id"),
    CANCEL("cancel", "publishDelChnl", "撤销发布这个栏目", "需要传入栏目id");

    private ChannelPublishEnum(String value, String name, String desc, String remark) {
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


    private static HashMap<String, ChannelPublishEnum> map = new HashMap<>();

    public static ChannelPublishEnum readByValue(String value) {
        if(map.isEmpty()){
            synchronized (ChannelPublishEnum.class){
                for(ChannelPublishEnum chnl : ChannelPublishEnum.values()){
                    map.put(chnl.getValue(), chnl);
                }
            }
        }
        return map.get(value);
    }
}
