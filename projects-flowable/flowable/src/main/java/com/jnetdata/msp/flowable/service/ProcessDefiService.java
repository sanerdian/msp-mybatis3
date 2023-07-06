package com.jnetdata.msp.flowable.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.flowable.model.ProcessDefi;
import org.flowable.ui.modeler.model.ModelRepresentation;
import org.springframework.util.MultiValueMap;

public interface ProcessDefiService extends BaseService<ProcessDefi> {

    /**
     * 新增流程定义
     */
    void insertEntity(ModelRepresentation modelRepresentation);

    /**
     * 更新流程定义
     */
    void updateEntity(String modelId, MultiValueMap<String, String> values);
}
