package com.jnetdata.msp.member.role.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.member.role.mapper.RoleUserMapper;
import com.jnetdata.msp.member.role.model.Role;
import com.jnetdata.msp.member.role.model.RoleModel;
import com.jnetdata.msp.member.role.model.RoleUser;
import com.jnetdata.msp.member.role.service.RoleService;
import com.jnetdata.msp.member.role.service.RoleUserService;
import com.jnetdata.msp.member.role.vo.RoleModelVo;
import com.jnetdata.msp.member.role.vo.RoleUserVo;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by WFJ on 2019/4/2.
 */
@Service
public class RoleUserServiceImpl extends BaseServiceImpl<RoleUserMapper,RoleUser> implements RoleUserService {

    @Lazy @Autowired
    private UserService userService;

    @Lazy @Autowired
    private RoleService roleService;

    /**
     * 用户添加角色
     * @param userIds 用户ids
     * @param roleId 角色
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    public boolean addUserRole(String userIds, String roleId) {
        String[] str = userIds.split(",");

        List<RoleUser> list = new ArrayList<>();
        for(int i=0;i<str.length;i++){
            List<RoleUser> getRoleuser = list(new PropertyWrapper<>(RoleUser.class).eq("userid",Long.valueOf(str[i])));
            for(int j = 0;j<getRoleuser.size();j++){
                if(getRoleuser.get(j).getRoleId().equals(Long.valueOf(roleId))){
                    return false;
                }
            }
            RoleUser roleUser = new RoleUser();
            roleUser.setUserId(Long.valueOf(str[i]));
            roleUser.setRoleId(Long.valueOf(roleId));
            Date date =new Date();
            roleUser.setCreateTime(date);
            list.add(roleUser);
        }

        return super.insertBatch(list);
    }

    /**
     * 获取角色下用户信息
     * @param vo
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    public RoleModelVo getUser(RoleUserVo vo) {

        Pager pager = new Pager();
        pager.setSize(vo.getPager().getSize());
        pager.setCurrent(vo.getPager().getCurrent());
        Pager<RoleUser> list = super.list(pager,new PropertyWrapper<>(RoleUser.class).eq("roleId",vo.getEntity().getId()));

        if(list.getRecords().size()==0){
            RoleModelVo tempVo = new RoleModelVo();
            return tempVo;
        }

        String userIds = list.getRecords().stream().map(RoleUser -> String.valueOf(RoleUser.getUserId())).collect(Collectors.joining(","));

        List<User> userList =userService.list(new PropertyWrapper<>(User.class).in("USERID",userIds));

        RoleModelVo modelVo = setModelData(list,userList);
        return modelVo;
    }

    /**
     *
     * @param pager 用户，角色关联数据
     * @param userList 用户数据
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    private RoleModelVo setModelData(Pager<RoleUser> pager, List<User> userList){

        Map<Long,User> map1 = new HashMap<>();
        userList.forEach(res->{
            map1.put(res.getId(),res);
        });

        List<RoleModel> list = new ArrayList<>();
        pager.getRecords().forEach(res->{
            RoleModel model = new RoleModel();
            model.setId(res.getId());
            model.setName(map1.get(res.getUserId()).getName());
            model.setTrueName(map1.get(res.getUserId()).getTrueName());
            model.setStatus(map1.get(res.getUserId()).getStatus());
            model.setIsLeader(res.getIsLeader());
            list.add(model);
        });

        RoleModelVo vo = new RoleModelVo();
        vo.setTotal(pager.getTotal());
        vo.setList(list);
        return vo;
    }

    /**
     * 组长设置
     * @param ids ids
     * @param type 类型（0：取消组长，1：设为组长）
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    public boolean updataLoader(Long[] ids, int type) {
        List<RoleUser> list = new ArrayList<>();
        for(Long id : ids){
            RoleUser roleUser = new RoleUser();
            roleUser.setId(id);
            roleUser.setIsLeader(type);
            list.add(roleUser);
        }
        return  super.updateBatchById(list);
    }

    /**
     * 用户，角色关联id
     * @param userId
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    public List<Role> getRolesByUserId(Long userId) {

        List<RoleUser> roleUsers = super.list(new PropertyWrapper<>(RoleUser.class).eq("userId", userId));
        if (roleUsers.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> roleIds = roleUsers.stream().map(RoleUser::getRoleId).collect(Collectors.toList());
        List<Role> roles = roleService.listByIds(roleIds);
        return roles;
    }

}
