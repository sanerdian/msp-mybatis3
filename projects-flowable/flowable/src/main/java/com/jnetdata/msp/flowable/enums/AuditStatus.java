package com.jnetdata.msp.flowable.enums;

/**
 * 审核状态
 */
public enum AuditStatus {
    AUDITING("1","审核中"),
    TERMINATED("2","已终止"),
    RECALL("3","已撤回"),
    FINISHED("4", "已完成"),
    REJECTED("5", "已驳回"),
    ;
    AuditStatus(String code, String name){
        this.code = code;
        this.name = name;
    }
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getName(String code){
        for(AuditStatus status: AuditStatus.values()){
            if(status.getCode().equals(code)){
                return status.getName();
            }
        }
        return null;
    }
}
