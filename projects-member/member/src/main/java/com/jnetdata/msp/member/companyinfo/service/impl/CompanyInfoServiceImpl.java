package com.jnetdata.msp.member.companyinfo.service.impl;


import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.log.j_log.service.JLogService;
import com.jnetdata.msp.member.companyinfo.mapper.CompanyInfoMapper;
import com.jnetdata.msp.member.companyinfo.model.Companyinfo;
import com.jnetdata.msp.member.companyinfo.service.CompanyInfoService;
import com.jnetdata.msp.member.limit.service.PermissionService;
import com.jnetdata.msp.util.WebUtil;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
import org.thenicesys.data.api.util.ConditionMap;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CompanyInfoServiceImpl extends BaseServiceImpl<CompanyInfoMapper,Companyinfo> implements CompanyInfoService {
    @Autowired
    PermissionService permissionService;
    @Autowired
    JLogService jLogService;

    /**
     * 模糊查询
     * @param condition
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    protected PropertyWrapper<Companyinfo> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition).like("name").eq("regtime").eq("parentId").getWrapper();
    }

    /**
     * 查询
     * @param entity
     * @return
     * @author hongshou
     * @date 2020/5/26
     */
    @Override
    public List<Companyinfo> list(Companyinfo entity) {
        return super.list(new PropertyWrapper<>(Companyinfo.class).like("name",entity.getName()));
    }

    /**
     * 按照权限获取树
     * @return
     * @author 开普云
     * @date 2020/11/19
     */
    @Override
    public List<Companyinfo> treeByPermission() {
        val user = SessionUser.getCurrentUser();
        Long userId = user.getId();
        List<Companyinfo> clist;
        if (!SessionUser.getSubject().isPermitted("company:view")) {
            List<Long> ids = permissionService.getUserPermissionIds(userId,"company:view:");
            if(ids.isEmpty()) return new ArrayList<>();
            clist = super.list(new PropertyWrapper<>(Companyinfo.class).in("id",ids).or().eq("createBy",userId));
        }else{
            clist = super.list();
        }
        List<Long> ids = clist.stream().map(m -> m.getId()).collect(Collectors.toList());
        List<Companyinfo> result = clist.stream().filter(m -> m.getParentId()==null || m.getParentId().equals(0L) || ids.contains(m.getParentId())).collect(Collectors.toList());
        return result;
    }

    /*
    * 获取树列表
    * return 结果集
    * @author 王树彬
    * @date 2022/7/25
    * */
    @Override
    public List<Companyinfo> treeList() {
        List<Companyinfo> list = super.list();
        List<Long> ids = list.stream().map(m -> m.getId()).collect(Collectors.toList());
        List<Companyinfo> result = list.stream().filter(m -> m.getParentId()==null || m.getParentId().equals(0L) || ids.contains(m.getParentId())).collect(Collectors.toList());
        return result;
    }

    /**
     * 按照权限查询列表
     * @param pager
     * @param map
     * @author 王树彬
     * @date 2022/7/25
     * @return
     */
    @Override
    public Pager<Companyinfo> listByPermission(Pager pager, ConditionMap map) {
        PropertyWrapper<Companyinfo> propertyWrapper = this.makeSearchWrapper((Map<String, Object>)map);
        val user = SessionUser.getCurrentUser();
        Long userId = user.getId();
        if (!SessionUser.getSubject().isPermitted("company:view")) {
            List<Long> ids = permissionService.getUserPermissionIds(userId,"company:view:");
            if(ids.isEmpty()) ids.add(0L);
            propertyWrapper.in("id",ids).or().eq("createBy",userId);
        }
        return super.list(pager,propertyWrapper);
    }

    @Override
    public void setRegisTimeIsNull(Long id) {
        baseMapper.setRegisTimeIsNull(id);
    }

    @Override
    public void addCompany(Companyinfo entity) {
        if(entity.getParentId() == null){
            entity.setParentId(0L);
            entity.setParentName("根机构");
        }
        permissionService.checkPermitted("company:add:"+entity.getParentId());
        super.insert(entity);
        jLogService.jLog(WebUtil.getRequest(),"机构","添加",entity.getId(),entity.getName());
    }

    @Override
    public void edit(Long id, Companyinfo entity) {
        Companyinfo base = super.getById(id);
        //判断前端传来的是否为空
        if(entity.getRegtime()==null){
            this.setRegisTimeIsNull(id);
        }
        permissionService.checkPermittedAndCruser("company:edit:"+entity.getId(),base.getCreateBy());
        entity.setId(id);
        super.updateById(entity);
        jLogService.jLog(WebUtil.getRequest(),"机构","修改",entity.getId(),entity.getName());
    }

    @Override
    public void delete(Long id) {
        Companyinfo byId = super.getById(id);
        permissionService.checkPermittedAndCruser("company:delete:"+byId.getParentId(),byId.getCreateBy());
        jLogService.jLog(WebUtil.getRequest(),"机构","删除",id,byId.getName());
        super.deleteById(id);
    }

}
