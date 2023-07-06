package com.jnetdata.msp.flowable.constants;

public interface FlowCons {
    //前端传的“审核意见”字段名
    String AUDIT_OPINION = "auditOpinion";
    //前端传的“审核参数”字段名
    String AUDIT_PARAM = "auditParam";
    //前端传的“用户id”字段名
    String AUDIT_USER_ID = "auditUserId";
    //前端传的流程跳转条件字段名
    String CONDITION = "condition";
    //通知消息类型：审核
    Long MSG_TYPE_AUDIT = 2L;
    //根据用户组id获取用户列表sql
    String SQL_GET_USERS = "select t1.* from act_id_user t1, act_id_membership t2 where t1.id_ = t2.user_id_ and t2.group_id_ = #{groupId}";
    //角色编号：编辑主任、主编
    String EDIT_MANAGER = "edit_manager";
    String CHIEF_IN_EDIT = "chief_in_edit";

}
