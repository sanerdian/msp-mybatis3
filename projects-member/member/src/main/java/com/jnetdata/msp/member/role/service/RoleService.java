package com.jnetdata.msp.member.role.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.member.role.model.Role;

import java.util.List;

/**
 * Created by WFJ on 2019/4/2.
 */
public interface RoleService extends BaseService<Role> {

    List<Role> getRoleTree();

/*
* 获取角色管理树对象
* */
    List<Role> tree();
}
