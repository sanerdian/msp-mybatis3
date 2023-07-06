package com.jnetdata.msp.visual.relationmoduletemplate.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;

import java.util.List;

public interface RelationModuleTemplateService extends BaseService<RelationModuleTemplate> {

    /**
     * 保存组件与可视化模板的关联信息
     * @param moduleId 组件id
     * @param moduleType 组件类型code
     * @param visualTemplateId 可视化模板id
     */
    void saveRelation(Long moduleId, String moduleType, Long visualTemplateId);

    /**
     * 删除组件与可视化模板的关联信息
     * @param moduleType 组件类型code
     * @param moduleId 组件id
     */
    void deleteRelation(Long moduleId, String moduleType);

    /**
     * 获取可视化模板关联的组件列表
     * @param visualTemplateId 可视化模板id
     */
    List<RelationModuleTemplate> getModuleList(Long visualTemplateId);
}
