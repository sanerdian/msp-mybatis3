package com.jnetdata.msp.manage.template.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.manage.template.mapper.TableTemplateMapper;
import com.jnetdata.msp.manage.template.model.TableTemplate;
import com.jnetdata.msp.manage.template.service.TableTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TableTemplateServiceImpl extends BaseServiceImpl<TableTemplateMapper,TableTemplate> implements TableTemplateService {
    @Override
    protected PropertyWrapper<TableTemplate> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition).like("tempname").getWrapper();
    }

    @Override
    public List<TableTemplate> getTemplates(TableTemplate entity) {
        return super.list(new PropertyWrapper<>(TableTemplate.class).like("tempname",entity.getTempname()));
    }
}
