package com.jnetdata.msp.flowable.enums;

/**
 * 消息对象的属性信息
 */
public enum MsgAttr {
    MSG_TYPE("type", "信息类型"),
    MSG_CONTENT("content", "信息内容"),
    RECEIVER_ID("receptionId", "接收用户id"),
    METADATA_ID("configid", "元数据id"),
    FLOW_ID("range", "任务id"),
    CREATE_TIME("createTime", "创建时间"),
    MODIFY_TIME("modityTime", "修改时间")
    ;

    MsgAttr(String name, String desc){
;
        this.name = name;
        this.desc = desc;
    }

    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String desc;

    public String getName() {
        return name;
    }
}
