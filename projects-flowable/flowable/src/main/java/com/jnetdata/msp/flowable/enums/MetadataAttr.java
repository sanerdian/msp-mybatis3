package com.jnetdata.msp.flowable.enums;

/**
 * 元数据属性名
 */
public enum MetadataAttr {
    AUDIT_STATUS("auditStatus", "审核状态：1-审核中，2-已终止，3-已撤回，4-已完成，5-已驳回"),
    AUDIT_TIME("auditTime", "审核日期"),
    AUDITOR_ID("auditorId", "当前处理人id"),
    AUDITOR_NAME("auditorName", "当前处理人姓名"),
    PROC_INST_ID("procInstId", "流程实例ID"),
    FINAL_RESULT("finalResult", "最终审核结果（同意、不同意、---）"),
    TASK_ID("FLOW_ID", "任务id"),
    ;

    MetadataAttr(String name, String desc){
        this.name = name;
        this.desc = desc;
    }

    /**
     * 字段名称
     */
    private String name;
    /**
     * 字段描述
     */
    private String desc;

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
