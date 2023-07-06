package com.jnetdata.msp.flowable.enums;

/**
 * 元数据字段
 * 工作流启动后，实时更新这些数据
 */
public enum MetadataColumn {
    AUDIT_STATUS("STATUS", "审核状态：1-审核中，2-已终止，3-已撤回，4-已完成，5-已驳回"),
    AUDIT_TIME("OPERTIME", "审核日期"),
    AUDITOR_ID("FLOW_USER", "当前处理人id"),
    AUDITOR_NAME("DOCTITLE", "当前处理人姓名"),
    PROC_INST_ID("LINKURL", "流程实例ID"),
    FINAL_RESULT("OPERUSER", "最终审核结果（1-同意、0-不同意、---）"),
    TASK_ID("FLOW_ID", "任务id"),
    ;

    MetadataColumn(String name, String desc){
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
