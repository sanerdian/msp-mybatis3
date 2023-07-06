package com.jnetdata.msp.member.companyinfo.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.member.companyinfo.model.Companyinfo;
import org.thenicesys.data.api.Pager;
import org.thenicesys.data.api.util.ConditionMap;


import java.util.List;

public interface CompanyInfoService extends BaseService<Companyinfo> {

    List<Companyinfo> list(Companyinfo entity);

    /**
     * 按照权限获取树
     * @return
     * @author 开普云
     * @date 2020/11/19
     */
    List<Companyinfo> treeByPermission();

    /*
     * 获取树列表
     * return 结果集
     * @author 王树彬
     * @date 2022/7/25
     * */
    List<Companyinfo> treeList();
    /**
     * 按照权限查询列表
     * @param pager
     * @param map
     * @author 王树彬
     * @date 2022/7/25
     * @return
     */
    Pager<Companyinfo> listByPermission(Pager pager, ConditionMap map);

    void setRegisTimeIsNull(Long id);

    void addCompany(Companyinfo entity);

    void edit(Long id,Companyinfo entity);

    void delete(Long id);
}
