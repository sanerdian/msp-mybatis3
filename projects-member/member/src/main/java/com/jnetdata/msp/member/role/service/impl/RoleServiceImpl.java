package com.jnetdata.msp.member.role.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.member.companyinfo.model.Companyinfo;
import com.jnetdata.msp.member.companyinfo.service.CompanyInfoService;
import com.jnetdata.msp.member.role.mapper.RoleMapper;
import com.jnetdata.msp.member.role.model.Role;
import com.jnetdata.msp.member.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by WFJ on 2019/4/2.
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper,Role> implements RoleService {

    @Autowired
    CompanyInfoService companyInfoService;

    @Override
    protected PropertyWrapper<Role> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("name")
                .like("code")
                .like("crUser")
                .like("roleType")
                .eq("companyId")
                .getWrapper();
    }


    /**
     * 获取角色树形数据
     * @return
     */
    @Override
    public List<Role> getRoleTree() {
        //排序用集合
        List<String> orderList = new ArrayList<>();

        orderList.add("order");
        List<Role> allList = super.list(new PropertyWrapper<>(Role.class).orderBy(orderList,true));

        //获取一级菜单
        List<Role> firstList = new ArrayList<>();
        allList.forEach(res->{
            if(Long.valueOf(0).equals(res.getParentId())){
                firstList.add(res);
            }
        });

        firstList.forEach(res->{
            res = getChild(res,allList);
        });



        return firstList;
    }

    /*
    * 获取树对象
    * */
    @Override
    public List<Role> tree() {
        List<Role> list = list();
        List<Companyinfo> companys = companyInfoService.list();
        List<Role> gs = companys.stream().map(c -> {
            Role p = new Role();
            p.setId(c.getId());
            p.setName(c.getName());
            p.setParentId(0L);
            return p;
        }).collect(Collectors.toList());

        for (Role item : list) {
            if(item.getParentId() == null || item.getParentId() == 0){
                item.setParentId(item.getCompanyId());
            }
        }
        list.addAll(gs);
        return list;
    }

    /**
     *
     * @param role
     * @param list
     * @return
     */
    private Role getChild(Role role,List<Role> list){
        List<Role> childList = new ArrayList<>();
        list.forEach(res->{
            if(role.getId().equals(res.getParentId())){
                childList.add(res);
            }
        });
        role.setChildren(childList);

        //递归出口
        if(childList.size()==0){
            return role;
        }
        //递归调用
        childList.forEach(res->{
            getChild(res,list);
        });

        return role;

    }

}
