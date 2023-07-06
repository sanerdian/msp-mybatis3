package com.jnetdata.msp.visual.modulesearch.service.impl;

import com.jnetdata.msp.base.vo.EntityIdVo;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.visual.enums.ModuleType;
import com.jnetdata.msp.visual.modulebutton.model.ModuleButton;
import com.jnetdata.msp.visual.modulesearch.mapper.ModuleSearchMapper;
import com.jnetdata.msp.visual.modulesearch.model.ModuleSearch;
import com.jnetdata.msp.visual.modulesearch.service.ModuleSearchService;
import com.jnetdata.msp.visual.relationmodulefield.model.RelationModuleField;
import com.jnetdata.msp.visual.relationmodulefield.service.RelationModuleFieldService;
import com.jnetdata.msp.visual.relationmodulefield.vo.ModuleRelation;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import com.jnetdata.msp.visual.relationmoduletemplate.service.RelationModuleTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ModuleSearchServiceImpl extends BaseServiceImpl<ModuleSearchMapper, ModuleSearch> implements ModuleSearchService {

    @Autowired
    private RelationModuleFieldService relationModuleFieldService;

    @Autowired
    private RelationModuleTemplateService relationModuleTemplateService;

    @Resource
    private ModuleSearchMapper moduleSearchMapper;
    @Override
    public PropertyWrapper<ModuleSearch> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .getWrapper();
    }

    /**
     * 新增搜索组件
     */
    @Override
    public JsonResult<EntityId<Long>> insertEntity(ModuleSearch entity){
        try {
            if(ObjectUtils.isEmpty(entity.getVisualTemplateId())){
                return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "可视化模板id（visualTemplateId）不能为空");
            }
            entity.setCreateTime(new Date());
            this.insert(entity);
            relationModuleFieldService.saveRelation(entity.getFieldList(), entity.getId(), ModuleType.SEARCH.value());
            relationModuleTemplateService.saveRelation(entity.getId(), ModuleType.SEARCH.value(), entity.getVisualTemplateId());
            return JsonResult.success(new EntityIdVo(entity.getId()));
        }catch (Exception e){
            log.error("新增搜索组件异常：{}", e.getMessage());
            return JsonResult.fail("新增失败");
        }
    }

    /**
     * 删除搜索组件
     */
    @Override
    public JsonResult<Void> deleteEntity(Long id){
        try {
            this.deleteById(id);
            relationModuleFieldService.deleteRelation(id, ModuleType.SEARCH.value());
            relationModuleTemplateService.deleteRelation(id, ModuleType.SEARCH.value());
            return JsonResult.success();
        }catch (Exception e){
            log.error("删除搜索组件异常：{}", e.getMessage());
            return JsonResult.fail("删除失败");
        }
    }

    /**
     * 修改搜索组件
     */
    @Override
    public JsonResult<Void> updateEntity(Long id, ModuleSearch entity){
        try {
            entity.setId(id);
            entity.setUpdateTime(new Date());
            this.updateById(entity);
            relationModuleFieldService.deleteRelation(id, ModuleType.SEARCH.value());
            relationModuleFieldService.saveRelation(entity.getFieldList(), id, ModuleType.SEARCH.value());
            return JsonResult.success();
        }catch (Exception e){
            log.error("修改搜索组件异常：{}", e.getMessage());
            return JsonResult.fail("更新失败");
        }
    }

    /**
     * 查询搜索组件
     */
    @Override
    public JsonResult<ModuleSearch> getEntity(Long id){
        try {
            ModuleSearch entity = this.getById(id);
            if(ObjectUtils.isEmpty(entity)){
                return JsonResult.fail("对象不存在");
            }

            List<RelationModuleField> list = relationModuleFieldService.getRelation(id, ModuleType.SEARCH.value());
            entity.setFieldList(list);

            return JsonResult.success(entity);
        }catch (Exception e){
            log.error("查询搜索组件异常：{}", e.getMessage());
            return JsonResult.fail();
        }
    }

    @Override
    public String generateJs(RelationModuleTemplate relation){
        StringBuffer builder = new StringBuffer();
        //获取组件关联的元数据信息（接口名，字段列表）
        ModuleRelation moduleRelation = relationModuleFieldService.getModuleRelationEntity(relation);
        List<RelationModuleField> list = moduleRelation.getFieldList();
        ModuleSearch moduleSearch = moduleSearchMapper.selectById(relation.getModuleId());
        try {
            for(RelationModuleField relationModuleField:list){
                if(relationModuleField.getFieldname().equals("startTime")){
                    builder.append("$(function(){")
                            .append("\n\tsetDate('#")
                            .append(moduleSearch.getDataAreaId())
                            .append("Date")
                            .append(relation.getOrder())
                            .append("', 'date');")
                            .append("\n})");
                }
            }

        }
       catch (Exception e){
            log.error("生成对应的js代码异常：{}", e.getMessage());
        }
        return builder.toString();
    }
}
