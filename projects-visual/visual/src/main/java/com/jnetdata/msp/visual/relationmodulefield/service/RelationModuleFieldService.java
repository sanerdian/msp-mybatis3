package com.jnetdata.msp.visual.relationmodulefield.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.visual.relationmodulefield.model.RelationModuleField;
import com.jnetdata.msp.visual.relationmodulefield.vo.FieldinfoVo;
import com.jnetdata.msp.visual.relationmodulefield.vo.ModuleRelation;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.thenicesys.web.JsonResult;

import java.util.List;

public interface RelationModuleFieldService extends BaseService<RelationModuleField> {

    /**
     * 保存组件与字段的关联信息
     * @param list 字段信息
     * @param moduleId 组件id
     * @param moduleType 组件类型code
     */
    void saveRelation(List<RelationModuleField> list, Long moduleId, String moduleType);

    /**
     * 删除组件与字段的关联信息
     * @param moduleId 组件id
     * @param moduleType 组件类型code
     */
    void deleteRelation(Long moduleId, String moduleType);

    /**
     * 查询组件与字段的关联信息
     * @param moduleId 组件id
     * @param moduleType 组件类型code
     */
    List<RelationModuleField> getRelation(Long moduleId, String moduleType);

    /**
     * 获取组件关联的元数据信息（字段信息、模块名、实体名）
     */
    ModuleRelation getModuleRelationEntity(RelationModuleTemplate relation);

    /**
     * 获取字段列表
     */
    JsonResult<List<FieldinfoVo>> list(FieldinfoVo entity);

}
