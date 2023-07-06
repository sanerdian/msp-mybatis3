package com.jnetdata.msp.member.limit.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.member.limit.model.Permission;
import org.thenicesys.data.api.util.ConditionMap;

import java.util.List;
import java.util.Map;

/**
 * Created by WFJ on 2019/4/2.
 */
public interface PermissionService extends BaseService<Permission> {

    /*
     * 设置权限
     * @param list
     * return boolean
     * @author 王树彬
     * @date 2023/3/22
     * */
    boolean setPermission(List<Permission> list);


    boolean setPermission(Map<String,Boolean> map, Long ownerId, Integer ownerType);

    /*
    * 查询
    * @param entity
    * @author 王树彬
    * @date 2023/3/22
    * */
    List<Permission> list(ConditionMap entity);

    /*
    * 查询用户权限
    * @param userid
    * @author 王树彬
    * @date 2023/3/22
    * return
    * */
    List<Permission> getUserPermission(Long userId);

    /*
    * 查询当前用户的权限
    * @param userid
    * @author 王树彬
    * @date 2023/3/22
    * return
    * */
    List<String> getUserPermissionStr(Long userId);

    /*
     * 查询当前用户的权限
     * @param userid
     * @author 王树彬
     * @date 2023/3/22
     * return
     * */
    List<String> getUserPermissionStr(Long userId,String per);
    /*
     * 查询当前用户的权限
     * @param userid
     * @author 王树彬
     * @date 2023/3/22
     * return
     * */
    List<String> getGroupPermissionStr(Long groupId,String per);

    List<Long> getMyPermissionIds(String per);

    /**
     * 获取用户权限id
     * @param userId
     * @param per
     * @author 王树彬
     * @date 2023/3/22
     * @return
     */
    List<Long> getUserPermissionIds(Long userId,String per);

    /**
     * 获取角色权限id
     * @param roleId
     * @param per
     * @author 王树彬
     * @date 2023/3/22
     * @return
     */
    List<Long> getRolePermissionIds(Long roleId,String per);

    boolean isPermitted(String s);

    void checkPermitted(String s);

    void checkPermittedAndCruser(String s,Long userId);
}
