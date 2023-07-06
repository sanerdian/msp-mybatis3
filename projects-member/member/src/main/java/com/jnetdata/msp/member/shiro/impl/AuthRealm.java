package com.jnetdata.msp.member.shiro.impl;


import com.jnetdata.msp.member.limit.service.PermissionService;
import com.jnetdata.msp.member.role.model.Role;
import com.jnetdata.msp.member.role.service.RoleUserService;
import com.jnetdata.msp.member.user.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author
 * Created by Administrator on 2018/9/30.
 */
public abstract class AuthRealm extends AuthorizingRealm {

    @Lazy @Autowired
    private RoleUserService roleUserService;
    @Lazy @Autowired
    private PermissionService permissionService;


    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Collection collection = principalCollection.fromRealm(getName());
        if (!collection.isEmpty()) {
            User user = (User) principalCollection.fromRealm(getName()).iterator().next();

            //获取用户角色
            List<Role> roles = roleUserService.getRolesByUserId(user.getId());
            info.addRoles(roles.stream().map(Role::getName).collect(Collectors.toList()));

            //用户和角色权限
            info.addStringPermissions(permissionService.getUserPermissionStr(user.getId()));
        }
        return info;
    }

    protected User cloneToCache(User user) {
        User obj = new User();
        BeanUtils.copyProperties(user, obj);
        // 密码不能持久化到缓存（如果以后用分布式缓存，有安全风险）
        obj.setPassWord(null);
        obj.setMdPassWord(null);
        return obj;
    }

    public void clearAuthz() {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }

}
