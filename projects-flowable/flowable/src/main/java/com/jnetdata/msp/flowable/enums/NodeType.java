package com.jnetdata.msp.flowable.enums;

/**
 * 节点类型
 */
public enum NodeType {
    EXCLUSIVE_GATEWAY("1", "排他网关"),
    END_EVENT("2", "结束事件"),
    ;
    private String code;
    private String desc;

    NodeType(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }
}
