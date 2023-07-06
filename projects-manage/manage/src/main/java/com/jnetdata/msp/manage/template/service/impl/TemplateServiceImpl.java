package com.jnetdata.msp.manage.template.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.manage.template.mapper.TemplateMapper;
import com.jnetdata.msp.manage.template.model.Template;
import com.jnetdata.msp.manage.template.service.TemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TemplateServiceImpl extends BaseServiceImpl<TemplateMapper,Template> implements TemplateService {
    @Override
    public PropertyWrapper<Template> makeSearchWrapper(Map<String, Object> condition) {
        PropertyWrapper<Template> p = createWrapperUtil(condition).like("tempname").eq("temptype").ge("createTime").ge("modifyTime").eq("tpltype").eq("status").eq("siteid").eq("columnid").eq("modifyUser")
                .eq("crUser").getWrapper();
        p.le(condition.get("createTimeto") != null,"createTime",condition.get("createTimeto"));
        p.le(condition.get("modifyTimeto") != null,"modifyTime",condition.get("modifyTimeto"));
        return p;
    }

    @Override
    public void updateUse(Long[] ids) {
        List<Template> list = new ArrayList<>();
        for (Long id : ids) {
            if(id == null) continue;
            Template t = new Template();
            t.setStatus(0);
            t.setColumnid(id);
            list.add(t);
        }
        super.updateBatchById(list);
    }
}
