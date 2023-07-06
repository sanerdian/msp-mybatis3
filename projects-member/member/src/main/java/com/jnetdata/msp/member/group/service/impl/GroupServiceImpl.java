package com.jnetdata.msp.member.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.member.companyinfo.model.Companyinfo;
import com.jnetdata.msp.member.companyinfo.service.CompanyInfoService;
import com.jnetdata.msp.member.group.mapper.GroupMapper;
import com.jnetdata.msp.member.group.model.Groupinfo;
import com.jnetdata.msp.member.group.service.GroupService;
import com.jnetdata.msp.member.limit.model.Permission;
import com.jnetdata.msp.member.limit.service.PermissionService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pair;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by WFJ on 2019/4/1.
 */
@Service
public class GroupServiceImpl extends BaseServiceImpl<GroupMapper, Groupinfo> implements GroupService {

    @Autowired
    @Lazy
    CompanyInfoService companyInfoService;
    @Autowired
    @Lazy
    PermissionService permissionService;

    @Override
    protected PropertyWrapper<Groupinfo> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("name")
                .like("code")
                .eq("parentId")
                .getWrapper();
    }

    @Override
    public Groupinfo  listOr(Long id) {
        Groupinfo id1 = baseMapper.selectOne(new QueryWrapper<Groupinfo>().eq("GROUPID", id));
        if(id1==null){
            return null;
        }
        return id1 ;
    }

    /*
    * 获取组织树形结构
    * @author hongshou
    * @date 2020/5/26
    * */
    @Override
    public List<Groupinfo> getGroupTree() {
        //获取所有数据
        List<String> orderList = new ArrayList<>();

        orderList.add("groupOrder");
        List<Groupinfo> allList = super.list(new PropertyWrapper<>(Groupinfo.class).orderBy(orderList,true));

        //获取一级菜单
        List<Groupinfo> firstList = new ArrayList<>();
        allList.forEach(res->{
            if(Long.valueOf(0).equals(res.getParentId())){
                firstList.add(res);
            }
        });

        firstList.forEach(res->{
            res = (getChild(res,allList));
        });

        return firstList;
    }

    @Override
    public List<Groupinfo> tree() {
        return tree((Long)null);
    }

    @Override
    public List<Groupinfo> tree(Long id) {
        Long userId = SessionUser.getCurrentUser().getId();
        List<Pair<String, Boolean>> orderList = new ArrayList<>();
        orderList.add(new Pair("groupOrder",true));
        orderList.add(new Pair("id",true));

        PropertyWrapper<Groupinfo> pw = new PropertyWrapper<>(Groupinfo.class).eq(id!=null,"companyId",id).orderBy(orderList);

        if(!SessionUser.getSubject().isPermitted("group:peer")){
            List<Long> ids = permissionService.getUserPermissionIds(userId, "group:peer:");
            List<Long> finalIds = ids;
            pw.and(m-> m.in("id", finalIds).or().eq("CRNUMBER", userId));
        }

        List<Groupinfo> list = super.list(pw);

        if(id != null){
            Companyinfo c = companyInfoService.getById(id);
            Groupinfo p = new Groupinfo();
            p.setId(c.getId());
            p.setName(c.getName());
            p.setParentId(c.getParentId());
            p.setTreeType(0);
            list.add(p);
        }else{
            List<Companyinfo> companys = companyInfoService.treeByPermission();
            List<Groupinfo> gs = companys.stream().map(c -> {
                Groupinfo p = new Groupinfo();
                p.setId(c.getId());
                p.setName(c.getName());
                p.setParentId(c.getParentId());
                p.setTreeType(0);
                return p;
            }).collect(Collectors.toList());
            list.addAll(gs);
        }

        for (Groupinfo item : list) {
            if(item.getParentId() == null || item.getParentId() == 0){
                item.setParentId(item.getCompanyId());
            }
        }

        return list;
    }

    /*
    * 获取菜单数据
    * @param ids
    * @author 王树彬
    * @date 2023/3/22
    * return
    * */
    @Override
    public List<Groupinfo> tree(List<Long> ids) {
        List<Pair<String, Boolean>> orderList = new ArrayList<>();
        orderList.add(new Pair("groupOrder",true));
        orderList.add(new Pair("id",true));

        List<Groupinfo> list;
        if(ids != null){
            List<Groupinfo> gsd = super.list(new PropertyWrapper<>(Groupinfo.class).eq("createBy", SessionUser.getCurrentUser().getId()));
            List<Long> ids1 = gsd.stream().map(m -> m.getId()).collect(Collectors.toList());
            ids.addAll(ids1);
            if(ids.isEmpty()) list = new ArrayList<>();
            else list = list(new PropertyWrapper<>(Groupinfo.class).in("id",ids).orderBy(orderList));
        }else{
            list = list(new PropertyWrapper<>(Groupinfo.class).orderBy(orderList));
        }
        List<Companyinfo> companys = companyInfoService.treeByPermission();
        List<Groupinfo> gs = companys.stream().map(c -> {
            Groupinfo p = new Groupinfo();
            p.setId(c.getId());
            p.setName(c.getName());
            p.setParentId(c.getParentId());
            p.setTreeType(0);
            return p;
        }).collect(Collectors.toList());

        for (Groupinfo item : list) {
            if(item.getParentId() == null || item.getParentId() == 0){
                item.setParentId(item.getCompanyId());
            }
        }
        list.addAll(gs);
        return list;
    }

    @Override
    public List<Groupinfo> tree(Long id, List<Long> ids) {
        return null;
    }

    @Override
    public List<Groupinfo> list(Groupinfo obj) {
        return list(createWrapper().eq("parentId",obj.getParentId()));
    }

    /**
     *获取子集菜单
     * @param list
     * @return
     * @author hongshou
     * @date 2020/5/26
     */
    private Groupinfo getChild(Groupinfo group, List<Groupinfo> list){
        List<Groupinfo> childList = new ArrayList<>();
        list.forEach(res->{
            if(group.getId().equals(res.getParentId())){
                childList.add(res);
            }
        });
        group.setChildren(childList);



        if(childList.size()==0){
            return group;
        }

        //递归调用
        childList.forEach(res->{
            getChild(res,list);
        });
        //递归出口


        return group;

    }



}
