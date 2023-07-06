package com.jnetdata.msp.manage.site.service.impl;

import com.jnetdata.msp.column.model.Channel;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.manage.column.service.ProgramaService;
import com.jnetdata.msp.member.companyinfo.model.Companyinfo;
import com.jnetdata.msp.member.companyinfo.service.CompanyInfoService;
import com.jnetdata.msp.manage.site.mapper.SiteMapper;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.site.service.SiteService;
import com.jnetdata.msp.member.limit.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.mybatis.impl.PropertyWrapperUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Service
public class SiteServiceImpl extends BaseServiceImpl<SiteMapper, Site> implements SiteService {

    @Autowired
    private CompanyInfoService companyInfoService;
    @Autowired
    @Lazy
    private ProgramaService programaService;
    @Autowired
    private PermissionService permissionService;

    @Override
    protected PropertyWrapper<Site> makeSearchWrapper(Map<String, Object> condition) {
        PropertyWrapperUtil<Site> pro=createWrapperUtil(condition);
        pro.eq("status");
        pro.eq("companyId");
        pro.like("name");
        pro.eq("webClass");
        return pro.getWrapper();
    }

    /**
     * 逻辑删除(删除和取消)
     * @param ids
     * @return
     */
    @Override
    public boolean updateStatus(String ids,int status) {
        String[] str = ids.split(",");
        List<Site> list = new ArrayList<>();
        for(int i=0;i<str.length;i++){
            Site site = new Site();
            site.setId(Long.valueOf(str[i]));
            site.setStatus(status);
            list.add(site);
        }
        programaService.updateBySite(ids,status);
        return super.updateBatchById(list);
    }

    /**
     * 获取删除状态数据
     * @return
     */
    @Override
    public List<Site> getDelList() {
        return super.list(new PropertyWrapper<>(Site.class).eq("status",1));
    }


    @Override
    public List<Site> getTreeDataAsList(String webclass) {
        List<Site> tree = new ArrayList<>();
        List<Site> siteDatas = getSiteDatas(webclass);
        getTreeDataAsList1(tree,siteDatas);
        return tree;
    }

    public void getTreeDataAsList1(List<Site> tree,List<Site> siteDatas){
        siteDatas.forEach(site -> {
            if(site.getSites()!= null && !site.getSites().isEmpty()){
                getTreeDataAsList1(tree,site.getSites());
            };
            site.setSites(null);
            tree.add(site);
        });
    }

    public List<Site> getSiteDatas(String webclass){
        Long userId = SessionUser.getCurrentUser().getId();
        List<Companyinfo> clist = companyInfoService.treeByPermission();

        List<Site> sites111 = super.list(new PropertyWrapper<>(Site.class).groupBy(Arrays.asList("companyId")).select("companyId"));
        List<Long> cidss = sites111.stream().filter(m -> m!=null).map(m -> m.getCompanyId()).collect(Collectors.toList());

        List<Site> siteList = clist.stream().map(c -> {
            Site s = new Site();
            s.setName(c.getName());
            s.setId(c.getId());
            s.setIsSite(0);
            s.setCompanyId(c.getParentId());
            if(cidss.contains(c.getId())){
                s.setIsParent(true);
            }
            return s;
        }).collect(Collectors.toList());

        boolean isAdmin = SessionUser.getSubject().isPermitted("site:view");

        List<Long> cids = clist.stream().map(Companyinfo::getId).collect(Collectors.toList());
        List<Site> sites;
        PropertyWrapper<Site> pw = new PropertyWrapper<>(Site.class).in("companyId", cids).eq("status", 0).eq(webclass != null, "webclass", webclass).orderBy(Arrays.asList("siteOrder"),true);
        if(!isAdmin){
            List<Long> siteIds = permissionService.getUserPermissionIds(userId,"site:view:");
            pw.and(m -> m.in(siteIds != null && !siteIds.isEmpty(),"SITEID",siteIds).or().eq("CRNUMBER", userId));
        }
        sites = super.list(pw);
        if(!sites.isEmpty()){
            List<Long> ids = sites.stream().map(m -> m.getId()).collect(Collectors.toList());
            List<Programa> pList = programaService.list(new PropertyWrapper<>(Programa.class).eq("status", 0).in("siteId",ids).groupBy(Arrays.asList("siteId")).select("siteId"));
            List<Long> siteIdss = pList.stream().map(m -> m.getSiteId()).collect(Collectors.toList());
            sites.forEach(res -> {
                if(siteIdss.contains(res.getId())){
                    res.setIsParent(true);
                }
            });

            Map<Long, List<Site>> map = sites.stream().collect(Collectors.groupingBy(Site::getCompanyId));
            siteList.forEach(res->{
                if(map.containsKey(res.getId())) res.setSites(map.get(res.getId()));
            });
        }

        Map<Long, List<Site>> collect = siteList.stream().collect(Collectors.groupingBy(Site::getCompanyId));
        siteList.forEach(res->{
            if(collect.containsKey(res.getId())) res.getSites().addAll(collect.get(res.getId()));
        });

        List<Site> collect1 = siteList.stream().filter(m -> m.getCompanyId() == 0L).collect(Collectors.toList());
        return collect1;
    }

    @Override
    public List<Site> getTreeData(String webclass) {
        return getSiteDatas(webclass);
    }

    @Override
    public List<Site> getAllTreeData() {
        List<Companyinfo> clist = companyInfoService.list();
        List<Site> siteList = clist.stream().map(c -> {
            Site s = new Site();
            s.setName(c.getName());
            s.setCompanyId(0L);
            s.setId(c.getId());
            return s;
        }).collect(Collectors.toList());

        List<Long> cids = clist.stream().map(Companyinfo::getId).collect(Collectors.toList());
        List<Site> sites = super.list(new PropertyWrapper<>(Site.class).in("companyId", cids).eq("status", 0));

        Map<Long, List<Site>> map = sites.stream().collect(Collectors.groupingBy(Site::getCompanyId));
        siteList.forEach(res->{
            res.setSites(map.containsKey(res.getId()) ? map.get(res.getId()) : new ArrayList<>());
        });

        return siteList;
    }

    @Override
    public void updateSeq(Site entity) {
        baseMapper.updateSeq(entity);
    }

    @Override
    public List<Channel> getChannelList(Long webSiteId) {
        return baseMapper.getChannelList(webSiteId);
    }
}
