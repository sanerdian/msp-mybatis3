package com.jnetdata.msp.flowable.service.common;

import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.flowable.constants.FlowCons;
import com.jnetdata.msp.flowable.enums.NewsStatus;
import com.jnetdata.msp.flowable.vo.NewsVo;
import com.jnetdata.msp.member.group.mapper.GrpUserMapper;
import com.jnetdata.msp.member.group.model.GrpUser;
import com.jnetdata.msp.member.limit.mapper.PermissionMapper;
import com.jnetdata.msp.member.limit.model.Permission;
import com.jnetdata.msp.member.role.mapper.RoleMapper;
import com.jnetdata.msp.member.role.mapper.RoleUserMapper;
import com.jnetdata.msp.member.role.model.Role;
import com.jnetdata.msp.member.role.model.RoleUser;
import com.jnetdata.msp.member.user.mapper.UserMapper;
import com.jnetdata.msp.member.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户相关服务
 */
@Slf4j
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private GrpUserMapper grpUserMapper;
    @Resource
    private RoleUserMapper roleUserMapper;
    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 获取下一节点审核人列表
     */
    public JsonResult auditorList(NewsVo vo){
        try {
            if(StringUtils.isEmpty(vo.getNewsStatus()) || StringUtils.isEmpty(vo.getColumnId())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "新闻状态(newsStatus)和栏目ID（columnId）都不能为空！");
            }

            //获取当前用户所在组织结构下的人员列表
            List<GrpUser> grpUserList1 = grpUserMapper.selectList(new PropertyWrapper<>(GrpUser.class).eq("userId", SessionUser.getCurrentUser().getId()).getWrapper());
            log.info("当前用户对应组织机构数量：{}", grpUserList1.size());
            if(CollectionUtils.isEmpty(grpUserList1)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "当前用户没有配置组织！");
            }
            List<Long> userIds = new ArrayList<>();
            for(GrpUser grpUser: grpUserList1){
                List<GrpUser> grpUserList2 = grpUserMapper.selectList(new PropertyWrapper<>(GrpUser.class).eq("groupId", grpUser.getGroupId()).getWrapper());
                userIds.addAll(grpUserList2.stream().map(m -> m.getUserId()).collect(Collectors.toList()));
                log.info("组织机构（id:{}）下用户数量：{}", grpUser.getGroupId(), grpUserList2.size());
            }

            //根据栏目id，筛选对该栏目具有审核权限的用户列表，然后取交集
            String permission = "column:limit:" + vo.getColumnId();
            List<Permission> permissionList = permissionMapper.selectList(new PropertyWrapper<>(Permission.class).eq("ownerType", 0).eq("permission", permission).getWrapper());
            List<Long> permissionUserIds = permissionList.stream().map(m -> m.getOwnerId()).collect(Collectors.toList());
            userIds.retainAll(permissionUserIds);
            log.info("具有栏目审核权限的用户数量:{}", permissionList.size());
            log.info("取交集后的用户数量:{}", userIds.size());
            if(CollectionUtils.isEmpty(userIds)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "同组织下的用户没有配置栏目的审核权限！");
            }

            //获取下一个节点的角色id
            String roleCode = null;
            if(NewsStatus.NEW.getCode().equals(vo.getNewsStatus()) || NewsStatus.EDIT.getCode().equals(vo.getNewsStatus())){
                roleCode = FlowCons.EDIT_MANAGER;
            }else if(NewsStatus.AUDIT.getCode().equals(vo.getNewsStatus())){
                roleCode = FlowCons.CHIEF_IN_EDIT;
            }
            List<Role> roleList = roleMapper.selectList(new PropertyWrapper<>(Role.class).eq("code", roleCode).getWrapper());
            Long roleId = roleList.get(0).getId();
            log.info("下一个节点的角色id：{}", roleId);

            //根据配置的角色，筛选出符合条件的用户列表
            List<RoleUser> roleUserList = roleUserMapper.selectList(new PropertyWrapper<>(RoleUser.class).eq("roleId", roleId).in("userId", userIds).getWrapper());
            if(CollectionUtils.isEmpty(roleUserList)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "同组织下的用户没有配置对应的角色！");
            }
            List<Long> resultIds = roleUserList.stream().map(m -> m.getUserId()).collect(Collectors.toList());
            List<User> userList = userMapper.selectList(new PropertyWrapper<>(User.class).in("id", resultIds).getWrapper());
            log.info("符合条件的用户数量：{}", userList.size());
            return JsonResult.success(userList);
        }catch (Exception e){
            log.error("获取下一节点审核人列表异常：{}", e.getMessage());
            return JsonResult.fail();
        }
    }

}
