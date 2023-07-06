package com.jnetdata.msp.manage.publish.core.common.constant;

import java.util.HashMap;

public enum BelongPublishEnum {

    OWNER("owner", "owner", "所属者", "documents,channels置标"),
    PARENT("parent", "parent", "父级", "documents,channels置标"),
    CHILD("child", "child", "子级", "documents,channels置标"),
    CHANNELID("channelid:", "channelid", "栏目id", "documents,channels置标"),
    SITE("site", "site", "站点", "channels置标"),
    SITEID("siteid:", "siteid", "站点id", "channels置标");

    private BelongPublishEnum(String value, String name, String desc, String remark) {
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


    private static HashMap<String, BelongPublishEnum> map = new HashMap<>();

    public static BelongPublishEnum readByValue(String value) {
        if (map.isEmpty()) {
            synchronized (BelongPublishEnum.class) {
                for (BelongPublishEnum chnl : BelongPublishEnum.values()) {
                    map.put(chnl.getValue(), chnl);
                }
            }
        }
        return map.get(value);
    }
}
