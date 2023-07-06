package com.jnetdata.msp.member.role.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.member.role.model.Role;
import com.jnetdata.msp.member.role.model.RoleUser;
import com.jnetdata.msp.member.role.vo.RoleModelVo;
import com.jnetdata.msp.member.role.vo.RoleUserVo;

import java.util.List;

/**
 * Created by WFJ on 2019/4/2.
 * @author hongshou
 * @date 2020/5/26
 */
public interface RoleUserService extends BaseService<RoleUser> {

    /**
     * 用户添加角色
     * @param userIds 用户ids
     * @param roleId 角色
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    boolean addUserRole(String userIds,String roleId);
    /**
     * 获取角色下用户信息
     * @param vo
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    RoleModelVo getUser(RoleUserVo vo);
    /**
     * 组长设置
     * @param ids ids
     * @param type 类型（0：取消组长，1：设为组长）
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    boolean updataLoader(Long[] ids,int type);
    /**
     * 用户，角色关联id
     * @param userId
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    List<Role> getRolesByUserId(Long userId);


}
