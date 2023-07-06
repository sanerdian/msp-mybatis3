package com.jnetdata.msp.member.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jnetdata.msp.core.model.util.PasswordHelper;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.member.companyinfo.vo.BaseVo;
import com.jnetdata.msp.member.group.mapper.GrpUserMapper;
import com.jnetdata.msp.member.group.model.GroupModel;
import com.jnetdata.msp.member.group.model.Groupinfo;
import com.jnetdata.msp.member.group.model.GrpUser;
import com.jnetdata.msp.member.group.service.GroupService;
import com.jnetdata.msp.member.group.service.GrpUserService;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.service.UserService;
import com.jnetdata.msp.member.user.vo.AddUserVo;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by WFJ on 2019/4/1.
 */
@Service
public class GrpUserServiceImpl extends BaseServiceImpl<GrpUserMapper, GrpUser> implements GrpUserService {
    @Autowired
    UserService userService;
    @Autowired
    GroupService groupService;


    /**
     * 用户添加机构
     * @param userIds 用户ids
     * @param GroupId 机构id
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    public boolean addUserGroup(String userIds, String GroupId) {
        String[] str = userIds.split(",");

        List<GrpUser> list = new ArrayList<>();
        for(int i=0;i<str.length;i++){
            List<GrpUser> getGrpuser = list(new PropertyWrapper<>(GrpUser.class).eq("userid",Long.valueOf(str[i])));
            for(int j = 0;j<getGrpuser.size();j++){
                if(getGrpuser.get(j).getGroupId().equals(Long.valueOf(GroupId))){
                    return false;
                }
            }
            GrpUser GrpUser = new GrpUser();
            GrpUser.setUserId(Long.valueOf(str[i]));
            GrpUser.setGroupId(Long.valueOf(GroupId));
            list.add(GrpUser);
        }

        return super.insertBatch(list);
    }

    /**
     * 获取机构下用户数据
     * @param vo
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    public Pager<GrpUser> getUser(BaseVo<GrpUser> vo) {
        Pager<GrpUser> list = super.list(vo.getPager().toPager(),new PropertyWrapper<>(GrpUser.class).eq("groupId",vo.getEntity().getGroupId()));
        if(list.getRecords().size()>0){
            List<Long> userIds = list.getRecords().stream().map(GrpUser -> GrpUser.getUserId()).collect(Collectors.toList());
            List<User> userList =userService.list(new PropertyWrapper<>(User.class).in("USERID",userIds));
            setModelData(list,userList);
        }
        return list;
    }

    /**
     * 机构，用户数据关联
     * @param pager
     * @param userList
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    private void setModelData(Pager<GrpUser> pager, List<User> userList){

        Map<Long,User> map1 = new HashMap<>();
        userList.forEach(res->{
            map1.put(res.getId(),res);
        });

        List<GroupModel> list = new ArrayList<>();
        pager.getRecords().forEach(res->{
            res.setUser(map1.get(res.getUserId()));
        });
    }

    @Override
    public void importGrpUser(List<List<String>> lists) {
        try {
            insertGrpUser(lists);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertGrpUser(List<List<String>> lists){
        int index = -1;
        for (int i = 0; i<lists.get(0).size(); i++) {
            String s = lists.get(0).get(i);
            if(s.equals("组织")) index = i;
        }
        if(index >= 0){
            List<GrpUser> list = new ArrayList<>();
            for(int i=1; i<lists.size(); i++){
                if(!StringUtils.isEmpty(lists.get(i).get(index))){
                    String groupstr = lists.get(i).get(index);
                    String[] groups = groupstr.split("-");
                    Groupinfo group;
                    if(groups.length > 1){
                        Groupinfo pGroup = groupService.get(new PropertyWrapper<>(Groupinfo.class).eq("name", groups[0]).and(w -> w.isNull("PARENTID").or().eq("parentId" , 0)));
                        group = groupService.get(new PropertyWrapper<>(Groupinfo.class).eq("name",groups[1]).eq("parentId",pGroup.getId()));
                    }else{
                        group = groupService.get(new PropertyWrapper<>(Groupinfo.class).eq("name", groups[0]).and(w -> w.isNull("PARENTID").or().eq("PARENTID",0)));
                    }
                    User user = userService.get(new PropertyWrapper<>(User.class).eq("name",lists.get(i).get(0)).eq("status",0));
                    GrpUser gu = new GrpUser();
                    gu.setUserId(user.getId());
                    gu.setGroupId(group.getId());
                    list.add(gu);
                }
            }
            if(list.size()>0){
                super.insertBatch(list);
            }
        }
    }

    public void getGroupNamesIdMap(List<Groupinfo> list,Map<String,Long> map,List<String> parentNams){
        list.forEach(groupinfo -> {
            List<String> myparentNams = parentNams.stream().collect(Collectors.toList());
            myparentNams.add(groupinfo.getName());
            map.put(String.join("-",myparentNams),groupinfo.getId());
            if(groupinfo.getChildren()!=null && !groupinfo.getChildren().isEmpty()){
                getGroupNamesIdMap(groupinfo.getChildren(),map,myparentNams);
            }
        });

    }

    /**
     * 导入互联网用户
     * @author 王树彬
     * @date 2023/1/17
     * @param userList
     */
    @Override
    public void importGrpUserNew(List<User> userList) {
        try {
            List<String> groups = userList.stream().filter(m -> m.getGroupName()!=null).map(m -> m.getGroupName().split("-")).flatMap(Arrays::stream).distinct().collect(Collectors.toList());
            if(groups.isEmpty()) return;
            List<Groupinfo> groupinfoList = groupService.list(new PropertyWrapper<>(Groupinfo.class).in("name", groups));
            Map<Long, List<Groupinfo>> groupinfoListGroupByPid = groupinfoList.stream().collect(Collectors.groupingBy(Groupinfo::getParentId));
            groupinfoList.stream().forEach(groupinfo -> {
                groupinfo.setChildren(groupinfoListGroupByPid.containsKey(groupinfo.getId())?groupinfoListGroupByPid.get(groupinfo.getId()):new ArrayList<>());
            });
            Map<String,Long> map = new HashMap<>();
            getGroupNamesIdMap(groupinfoListGroupByPid.get(0L),map,new ArrayList<>());
            List<GrpUser> list = new ArrayList<>();
            userList.forEach(user -> {
                if(map.containsKey(user.getGroupName())){
                    GrpUser gu = new GrpUser();
                    gu.setUserId(user.getId());
                    gu.setGroupId(map.get(user.getGroupName()));
                    list.add(gu);
                }
            });
            super.insertBatch(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        List<GrpUser> list = new ArrayList<>();
        for (Long id : ids) {
            GrpUser gu = new GrpUser();
            gu.setId(id);
            gu.setIsLeader(type);
            gu.setModifyUser("admin");
            gu.setModifyTime(new Date());
            list.add(gu);
        }
        return  super.updateBatchById(list);
    }

    /**
     * 根据用户获取组织
     * @return
     * @author hongshou
     * @date 2020/5/26
     */
    @Override
    public List<GrpUser> getGroupByUser(Long userId) {

        return super.list( new PropertyWrapper<>(GrpUser.class).eq("USERID",userId));
    }

    /**
     * 根据用户获取组织
     * @author 开普云
     * @date 2020/10/22
     * @return
     */
    @Override
    public boolean checkInGroup(Long userId,String code) {
        List<Groupinfo> groups = groupService.list(new PropertyWrapper<>(Groupinfo.class).eq("code", code));
        List<Long> groupIds = groups.stream().map(m -> m.getId()).collect(Collectors.toList());
        int count = super.count(new PropertyWrapper<>(GrpUser.class).in("groupId", groupIds).eq("userId", userId));
        return count>0;
    }

/*
* 通过用户id获得组织
* @pram：long id
* @author 纪凯静
* @date 2023/1/17
* @return list<long>
* */
    public List<Long> FindOrganization(Long id){
        List<Long> list = new ArrayList<>();
        List<Long> longs = new ArrayList<>();
        List<GrpUser> grpUsers = baseMapper.selectList(new QueryWrapper<GrpUser>().eq("userid",id));
        longs.addAll(grpUsers.stream().map(GrpUser::getUserId).collect(Collectors.toList()));
       /* grpUsers.forEach(s->{
        Long groupId = s.getGroupId();
        });*/
        /*ids= getService().listGrpUser(longs);*/
        grpUsers.forEach(s->{
            List<Long> selectlist = selectlist(s.getGroupId(), list);
        });
        return list;
    }

    @Override
    public User addUser(AddUserVo vo) {
        User user = userService.addUser1(vo.getEntity());
        List<GrpUser> list = new ArrayList<>();
        for (Long groupId : vo.getGroupIds()) {
            GrpUser grpUser = new GrpUser();
            grpUser.setUserId(vo.getEntity().getId());
            grpUser.setGroupId(groupId);
            list.add(grpUser);
        }
        if(!list.isEmpty()) this.insertBatch(list);//批量插入
        return user;
    }

    @Override
    public void updateUser(Long id, AddUserVo vo) {
        if(vo.getGroupIds().length > 0){
            this.delete(new PropertyWrapper<>(GrpUser.class).eq("userId",id));
            List<GrpUser> list = new ArrayList<>();
            for (Long groupId : vo.getGroupIds()) {
                GrpUser grpUser = new GrpUser();
                grpUser.setUserId(id);
                grpUser.setGroupId(groupId);
                list.add(grpUser);
            }
            boolean b = this.insertBatch(list);
        }

        userService.updateUser(id, vo.getEntity());
    }

    /**
     * 查询
     * @param id
     * @param list
     * @author 纪凯静
     * @date 2023/1/17
     * @return
     */
    public List<Long> selectlist(Long id,List<Long> list){
        Groupinfo groupinfo= groupService.listOr(id);
        if(groupinfo!=null){
            list.add(groupinfo.getId());
            Long parentId = groupinfo.getParentId();
          /*  if(parentId!=null&&!parentId.equals(null)&&parentId.longValue()!=0){
                System.out.println(111);
            }*/
            if(parentId!=null&&groupinfo.getParentId()!=0&&!groupinfo.getParentId().equals(null)){
                selectlist(groupinfo.getParentId(),list);
            }}
        return list;
    }
}
