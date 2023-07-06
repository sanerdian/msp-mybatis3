package com.jnetdata.msp.visual.relationmoduletemplate.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.visual.relationmoduletemplate.mapper.RelationModuleTemplateMapper;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import com.jnetdata.msp.visual.relationmoduletemplate.service.RelationModuleTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RelationModuleTemplateServiceImpl extends BaseServiceImpl<RelationModuleTemplateMapper, RelationModuleTemplate> implements RelationModuleTemplateService  {

    @Override
    public PropertyWrapper<RelationModuleTemplate> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .getWrapper();
    }

    /**
     * 保存组件与可视化模板的关联信息
     * @param moduleId 组件id
     * @param moduleType 组件类型code
     * @param visualTemplateId 可视化模板id
     */
    @Override
    public void saveRelation(Long moduleId, String moduleType, Long visualTemplateId){
        try {
            RelationModuleTemplate relation = new RelationModuleTemplate();
            relation.setModuleId(moduleId);
            relation.setModuleType(moduleType);
            relation.setVisualTemplateId(visualTemplateId);
            relation.setCreateTime(new Date());
            this.insert(relation);
        }catch (Exception e){
            log.error("保存组件与可视化模板的关联信息异常:{}, 组件id:{}, 组件类型code:{}, 可视化模板id:{}", e.getMessage(), moduleId, moduleType, visualTemplateId);
        }
    }

    /**
     * 删除组件与可视化模板的关联信息
     * @param moduleId 组件id
     * @param moduleType 组件类型code
     */
    @Override
    public void deleteRelation(Long moduleId, String moduleType){
        try {
            List<RelationModuleTemplate> list = this.list(new PropertyWrapper<>(RelationModuleTemplate.class)
                    .eq("moduleId", moduleId).eq("moduleType", moduleType));
            if(!CollectionUtils.isEmpty(list)){
                for(RelationModuleTemplate relation: list){
                    this.deleteById(relation.getId());
                }
            }
        }catch (Exception e){
            log.error("删除组件与可视化模板的关联信息异常:{}, 组件id:{}, 组件类型code:{}", e.getMessage(), moduleId, moduleType);
        }
    }

    /**
     * 获取可视化模板关联的组件列表
     * @param visualTemplateId 可视化模板id
     */
    @Override
    public List<RelationModuleTemplate> getModuleList(Long visualTemplateId){
        try {
            List<RelationModuleTemplate> list = this.list(new PropertyWrapper<>(RelationModuleTemplate.class).eq("visualTemplateId", visualTemplateId));
            //通过CollectionUtils工具类判断集合是否为空
            return CollectionUtils.isEmpty(list) ? null : list;
        }catch (Exception e){
            log.error("获取可视化模板关联的组件列表异常:{}, visualTemplateId:{}", e.getMessage(), visualTemplateId);
            return null;
        }
    }

}