package com.jnetdata.msp.manage.template.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.manage.template.model.TableTemplate;

import java.util.List;


public interface TableTemplateService extends BaseService<TableTemplate> {
    List<TableTemplate> getTemplates(TableTemplate entity);
}
