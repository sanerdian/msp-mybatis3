package com.jnetdata.msp.media.service.impl;

import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.member.group.mapper.GroupMapper;
import com.jnetdata.msp.member.group.mapper.GrpUserMapper;
import com.jnetdata.msp.member.group.model.Groupinfo;
import com.jnetdata.msp.member.group.model.GrpUser;
import com.jnetdata.msp.member.user.mapper.UserMapper;
import com.jnetdata.msp.member.user.model.User;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UserComponent {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private GrpUserMapper grpUserMapper;
    //获取用户所在的部门（如果有多个，则取第一个）
    public Groupinfo getGroupByUserId(Long userId){
        Groupinfo groupinfo=null;
        List<GrpUser> grpUsers = grpUserMapper.selectList(new PropertyWrapper<>(GrpUser.class).eq("userId", userId).getWrapper());
        if(grpUsers!=null&&grpUsers.size()>0){
            Long groupId=grpUsers.get(0).getGroupId();
            groupinfo=groupMapper.selectById(groupId);
        }
        return groupinfo;
    }
    public Groupinfo getCurrentGroup(){
        return getGroupByUserId(getCurrentUserId());
    }
    public User getCurrentUser(){
        return getUserById(getCurrentUserId());
    }
    //获取用户信息
    public User getUserById(Long userId){
        return userMapper.selectById(userId);
    }

    public Long getCurrentUserId() {//todo 尝试获取用户id，失败则用默认值。测试时临时使用。正式功能时，非登陆用户应该抛异常，让其登陆
        try{
            var currentUser = SessionUser.getCurrentUser();
            return currentUser.getId();
        }catch (Exception e){
            return 0L;
        }
    }
    //获取当前部门中的所有用户
    public List<User> getUsersAtGroup(Long groupId) {
        List<User> users=new ArrayList<>();
        List<GrpUser> grpUsers = grpUserMapper.selectList(new PropertyWrapper<>(GrpUser.class).eq("groupId", groupId).getWrapper());
        if(grpUsers.size()>0){
            List<@NotEmpty(message = "用户id") Long> userIds = grpUsers.stream().map(item -> item.getUserId()).collect(Collectors.toList());
            users=userMapper.selectBatchIds(userIds);
        }
        return users;
    }
    public List<Groupinfo> groupTree4CurrentUser() {
        List<Groupinfo> result=new ArrayList<>();//存结果
        Set<Long> pidUsed=new HashSet<>();//存查询中已经使用过的pid（避免循环数据导致的无限递归查询）
        List<Long> pidCurrent=new ArrayList<>();//当前用于查询的pid
        Long userId= getCurrentUserId();
        //当用户与部门为多对多关系时，取第一个部门的id
        List<GrpUser> grpUsers = grpUserMapper.selectList(new PropertyWrapper<>(GrpUser.class).eq("userId", userId).getWrapper());
        if(grpUsers.size()>0){
            grpUsers.forEach(grpUser -> pidCurrent.add(grpUser.getGroupId()));
        }
        if(pidCurrent.size()>0){
            PropertyWrapper wrapper = new PropertyWrapper(Groupinfo.class);
            wrapper.in("id",pidCurrent);
            List<Groupinfo> groups = groupMapper.selectList(wrapper.getWrapper());
            if(groups!=null){
                result.addAll(groups);
            }
        }
        while (pidCurrent.size()>0){
            //查询
            PropertyWrapper wrapper = new PropertyWrapper(Groupinfo.class);
            wrapper.in("parentId",pidCurrent);
            List<Groupinfo> groups = groupMapper.selectList(wrapper.getWrapper());
            if(groups!=null){
                result.addAll(groups);
            }
            //查询条件重置
            pidUsed.addAll(pidCurrent);//
            pidCurrent.clear();
            if(groups!=null&&groups.size()>0){
                for(Groupinfo group:groups){
                    if(!pidUsed.contains(group.getId())){
                        pidCurrent.add(group.getId());
                    }
                }
            }
        }
        return result;
    }
    public List<User> listUsers(){
        List<User> users= new ArrayList<>();
        List<GrpUser> grpUsers = grpUserMapper.selectList(new PropertyWrapper<>(GrpUser.class).eq("userId", getCurrentUserId()).getWrapper());
        if(grpUsers!=null&&grpUsers.size()>0){
            List<@NotEmpty(message = "组织编号") Long> groupIds = grpUsers.stream().map(item -> item.getGroupId()).collect(Collectors.toList());
            List<GrpUser> grpUsers1 = grpUserMapper.selectList(new PropertyWrapper<>(GrpUser.class).in("groupId", groupIds).getWrapper());
            if(grpUsers1!=null&grpUsers1.size()>0){
                List<@NotEmpty(message = "用户id") Long> userids = grpUsers1.stream().map(item -> item.getUserId()).collect(Collectors.toList());
                users = userMapper.selectBatchIds(userids);
            }
        }
        return users;
    }
}
