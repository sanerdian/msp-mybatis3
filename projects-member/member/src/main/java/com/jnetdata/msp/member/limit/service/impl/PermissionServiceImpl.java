package com.jnetdata.msp.member.limit.service.impl;

import com.baidubce.services.bvw.model.matlib.Per;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.member.group.model.GrpUser;
import com.jnetdata.msp.member.group.service.GrpUserService;
import com.jnetdata.msp.member.limit.mapper.PermissionMapper;
import com.jnetdata.msp.member.limit.model.Permission;
import com.jnetdata.msp.member.limit.service.PermissionService;
import com.jnetdata.msp.member.role.model.Role;
import com.jnetdata.msp.member.role.service.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.util.ConditionMap;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by WFJ on 2019/4/2.
 * @author 王树彬
 * @date 2020/5/30
 */
@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private GrpUserService grpUserService;

    @Override
    protected PropertyWrapper<Permission> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("permission")
                .eq("id")
                .eq("isurl")
                .eq("ownerId")
                .eq("ownerType")
                .getWrapper();
    }

    /*
    * 设置权限
    * @param list
    * return boolean
    * @author 王树彬
    * @date 2020/5/23
    * */
    @Override
    public boolean setPermission(List<Permission> list) {

        //该授权对象原权限列表
        List<Permission> old = list(createWrapper().eq("ownerId",list.get(0).getOwnerId()).like("permission",":"));
        //原权限串
        List<String> oldStr = old.stream().map(Permission::getPermission).collect(Collectors.toList());
        //新权限串
        List<String> newStr = list.stream().map(Permission::getPermission).collect(Collectors.toList());
        //要删除的权限id
        List<Long> delIds = old.stream().filter(m -> !newStr.contains(m.getPermission())).map(Permission::getId).collect(Collectors.toList());
        //要添加的权限
        List<Permission> addPermission = list.stream().filter(m -> !oldStr.contains(m.getPermission())).collect(Collectors.toList());

        if(delIds.isEmpty() || (!delIds.isEmpty() && deleteBatchIds(delIds))){
            if(!addPermission.isEmpty()){
                return insertBatch(addPermission);
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    @Override
    public boolean setPermission(Map<String,Boolean> map, Long ownerId, Integer ownerType) {
        List<Permission> add = new ArrayList<>();
        List<String> del = new ArrayList<>();
//        map.keySet().stream().filter(m -> m.indexOf(":inherit:")>0).map(str -> {
//            String[] split = str.split(":");
//            if(split[0].equals("metadata")){
//
//            }
//        })

        map.forEach((key,value) -> {
            if(value){
                Permission permission = new Permission();
                permission.setPermission(key);
                permission.setOwnerType(ownerType);
                permission.setOwnerId(ownerId);
                add.add(permission);
            }else {
                del.add(key);
            }
        });
        if(!add.isEmpty()){
            add.forEach(m -> {
                m.setOwnerId(ownerId);
                m.setOwnerType(ownerType);
            });

            List<Permission> old = list(createWrapper().eq("ownerId",ownerId).like("permission",":"));
            //原权限串
            List<String> oldStr = old.stream().map(Permission::getPermission).collect(Collectors.toList());
            //要添加的权限
            List<Permission> addPermission = add.stream().filter(m -> !oldStr.contains(m.getPermission())).collect(Collectors.toList());

            if(!addPermission.isEmpty()) insertBatch(addPermission);
        }

        if(!del.isEmpty()){
            delete(new PropertyWrapper<>(Permission.class).eq("ownerId",ownerId).in("permission",del));
        }
        return true;
    }

    /*
     * 查询
     * @param entity
     * @author 王树彬
     * @date 2020/5/23
     * */
    @Override
    public List<Permission> list(ConditionMap entity) {
        return search(entity);
    }

    /*
     * 查询用户权限
     * @param userid
     * @author 王树彬
     * @date 2020/5/23
     * return
     * */
    @Override
    public List<Permission> getUserPermission(Long userId) {
        return getUserPermission(userId,null);
    }

    /*
    * 获取用户权限
    * @param userid
    * @param per
    * @author 王树彬
    * @date 2020/5/23
    * return
    * */
    public List<Permission> getUserPermission(Long userId,String per) {
        PropertyWrapper<Permission> pw = new PropertyWrapper<>(Permission.class).eq("ownerId", userId);
        if(!StringUtils.isEmpty(per)){
            pw.like("permission",per);
        }
        List<Permission> list = list(pw);
        List<Role> roles = roleUserService.getRolesByUserId(userId);
        if (!roles.isEmpty()) {
            List<Long> roleIds = roles.stream().map(m -> m.getId()).collect(Collectors.toList());
            pw = new PropertyWrapper<>(Permission.class).in("ownerId",roleIds);
            if(!StringUtils.isEmpty(per)){
                pw.like("permission",per);
            }
            List<Permission> lrs = list(pw);
            list.addAll(lrs);
        }
        List<GrpUser> groups = grpUserService.getGroupByUser(userId); //获取用户组织
        if(!groups.isEmpty()){
            List<Long> groupIds = groups.stream().map(m -> m.getGroupId()).collect(Collectors.toList());
            pw = new PropertyWrapper<>(Permission.class).in("ownerId",groupIds);
            if(!StringUtils.isEmpty(per)){
                pw.like("permission",per);
            }
            List<Permission> lrs = list(pw);
            list.addAll(lrs);
        }
        return list;
    }

    /*
    * 查询用户权限
    * @param userid
    * @author 王树彬
    * @date 2020/5/23
    * return
    * */
    @Override
    public List<String> getUserPermissionStr(Long userId) {
        return getUserPermissionStr(userId,null);
    }

    @Override
    public List<String> getUserPermissionStr(Long userId, String per) {
        List<Permission> list = getUserPermission(userId,per);
        List<String> permissionStrs = list.stream().map(m -> m.getPermission()).distinct().collect(Collectors.toList());
        return permissionStrs;
    }

    @Override
    public List<String> getGroupPermissionStr(Long groupId, String per) {
        PropertyWrapper<Permission> pw = new PropertyWrapper<>(Permission.class).eq("ownerId",groupId);
        if(!StringUtils.isEmpty(per)){
            pw.like("permission",per);
        }
        List<Permission> list = list(pw);
        List<String> strs = list.stream().map(m -> m.getPermission()).collect(Collectors.toList());
        return strs;
    }

    /*
     * 获取用户权限值
     * @param userid
     * @param per
     * @author 王树彬
     * @date 2020/5/23
     * return
     * */
    @Override
    public List<Long> getMyPermissionIds(String per) {
        Long userId = SessionUser.getCurrentUser().getId();
        return getUserPermissionIds(userId,per);
    }

    /*
    * 获取用户权限值
    * @param userid
    * @param per
    * @author 王树彬
    * @date 2020/5/23
    * return
    * */
    @Override
    public List<Long> getUserPermissionIds(Long userId, String per) {
        List<Permission> list = getUserPermission(userId, per);
        List<Long> ids = list.stream().map(m -> Long.parseLong(m.getPermission().split(":")[2])).collect(Collectors.toList());
        return ids;
    }

    /*
    * 获取角色权限id
    * @author 王树彬
    * @date 2020/5/23
    * */
    @Override
    public List<Long> getRolePermissionIds(Long roleId, String per) {
        PropertyWrapper<Permission> pw = new PropertyWrapper<>(Permission.class).eq("ownerId",roleId);
        if(!StringUtils.isEmpty(per)){
            pw.like("permission",per);
        }
        List<Permission> list = list(pw);
        List<Long> ids = list.stream().map(m -> Long.parseLong(m.getPermission().split(":")[2])).collect(Collectors.toList());
        return ids;
    }

    @Override
    public boolean isPermitted(String s) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(s);
        while (s.indexOf(":") > 0){
            s = s.substring(0,s.lastIndexOf(":"));
            strings.add(s);
        }
        int permission = super.count(new PropertyWrapper<>(Permission.class).in("permission", strings).eq("ownerId", SessionUser.getCurrentUser().getId()));
        return permission > 0;
    }

    @Override
    public void checkPermitted(String s) {
        boolean permitted = isPermitted(s);
        if(!permitted) throw new RuntimeException("暂无权限");
    }

    @Override
    public void checkPermittedAndCruser(String s, Long userId) {
        boolean permitted = isPermitted(s);
        if(!permitted && !userId.equals(SessionUser.getCurrentUser().getId())) throw new RuntimeException("暂无权限");
    }
}
