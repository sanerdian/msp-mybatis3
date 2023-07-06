package com.jnetdata.msp.flowable.enums;

import org.springframework.util.StringUtils;

/**
 * 自定义意见类型
 * 表：act_hi_comment
 * 字段：type_
 */
public enum CommentType {
    SUBMIT("submit", "提交"),
    AGREE("agree", "同意"),
    RECALL("recall", "撤回"),//发起人使流程回到编辑节点
    REJECT("reject", "驳回"),
    TRANSFER("transfer", "转交"),
    REVOKE("revoke", "撤销")
    ;
    private String code;    //编号
    private String name;    //名称

    CommentType(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static String getName(String code){
        for(CommentType type: CommentType.values()){
            if(type.getCode().equals(code)){
                return type.getName();
            }
        }
        return null;
    }

    public static String getCode(String name){
        for(CommentType type: CommentType.values()){
            if(type.getName().equals(name)){
                return type.getCode();
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
