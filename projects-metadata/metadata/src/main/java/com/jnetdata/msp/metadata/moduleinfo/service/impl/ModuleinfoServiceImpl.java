package com.jnetdata.msp.metadata.moduleinfo.service.impl;

import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.core.util.MapUtil;
import com.jnetdata.msp.member.limit.service.PermissionService;
import com.jnetdata.msp.metadata.moduleinfo.mapper.ModuleinfoMapper;
import com.jnetdata.msp.metadata.moduleinfo.model.Moduleinfo;
import com.jnetdata.msp.metadata.moduleinfo.service.ModuleinfoService;
import com.jnetdata.msp.metadata.vo.BaseVo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.data.api.ConditionContainer;
import org.thenicesys.data.api.Pager;
import org.thenicesys.data.api.util.ConditionMap;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ModuleinfoServiceImpl extends BaseServiceImpl<ModuleinfoMapper , Moduleinfo> implements ModuleinfoService {
    @Autowired
    private PermissionService permissionService;

    @Override
    protected PropertyWrapper<Moduleinfo> makeSearchWrapper(Map<String, Object> condition) {
        PropertyWrapper<Moduleinfo> wrapper = createWrapperUtil(condition).like("englishname").like("modulename").like("cruser").ge("crtime").getWrapper();
        wrapper.le(condition.get("crtimeTo") != null, "crtimeTo", condition.get("crtimeTo"));
        if (condition.get("searchName") != null) {
            wrapper.getWrapper().and(i -> i.like("ENGLISHNAME", condition.get("searchName") + "").or().like("MODULENAME", condition.get("searchName") + ""));
        }
        return wrapper;
    }

    @Override
    public void updateorder(Long id, int sort) {
        Moduleinfo m = super.getById(id);
        List<Moduleinfo> list = super.list(new PropertyWrapper<>(Moduleinfo.class)
                .ge("moduleorder", sort)
                .ne("id", id));
        for (Moduleinfo moduleinfo : list) {
            moduleinfo.setModuleorder(moduleinfo.getModuleorder()+1);
        }
        super.updateBatchById(list);
        m.setModuleorder(sort);
        super.updateById(m);
    }

    @Override
    public Integer getmaxorder() {
        return baseMapper.getmaxorder();
    }

    @Override
    public Pager<Moduleinfo> pageByPermission(BaseVo<Moduleinfo> vo) {
        PropertyWrapper<Moduleinfo> pw = makeSearchWrapper((Map)ConditionMap.of(vo.getEntity()));
        if(!SecurityUtils.getSubject().isPermitted("metadataModule:view")){
            Long userId = SessionUser.getCurrentUser().getId();
            List<Long> ids = permissionService.getUserPermissionIds(userId, "metadataModule:view");
            pw.and(m -> m.in(!ids.isEmpty(),"MODULEINFOID",ids).or(!ids.isEmpty()).eq("CRNUMBER", userId));
        }
        return list(vo.getPager().toPager(), pw);
    }

    @Override
    public List<Long> allByMyPermission(String permissionType) {
        List<Moduleinfo> list;
        PropertyWrapper<Moduleinfo> pw = new PropertyWrapper<>(Moduleinfo.class).select("MODULEINFOID ID");
        if(!SecurityUtils.getSubject().isPermitted("metadataModule:view")){
            Long userId = SessionUser.getCurrentUser().getId();
            List<Long> ids = permissionService.getUserPermissionIds(userId, "metadataModule:"+permissionType);
            pw.in(!ids.isEmpty(), "MODULEINFOID", ids).or().eq("CRNUMBER", userId);
        }
        list = list(pw);
        List<Long> ids = list.stream().map(m -> m.getId()).collect(Collectors.toList());
        return ids;
    }

    @Override
    public List<Long> metdataByMyPermission() {
        return allByMyPermission("metadata");
    }

}
