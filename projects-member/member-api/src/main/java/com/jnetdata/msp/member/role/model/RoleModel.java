package com.jnetdata.msp.member.role.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

/**
 * Created by WFJ on 2019/4/8.
 */
@Data
public class RoleModel {

    //id
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    //用户名
    private String name;
    //真实姓名
    private String trueName;
    //用户状态
    private int status;
    //是否是组长
    private int isLeader;
}
