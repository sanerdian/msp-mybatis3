package com.jnetdata.msp.visual.moduleformelement.service.impl;

import com.jnetdata.msp.base.vo.EntityIdVo;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.visual.enums.ModuleType;
import com.jnetdata.msp.visual.modulechart.model.ModuleChart;
import com.jnetdata.msp.visual.moduleform.mapper.ModuleFormMapper;
import com.jnetdata.msp.visual.moduleform.model.ModuleForm;
import com.jnetdata.msp.visual.moduleformelement.model.ModuleFormelement;
import com.jnetdata.msp.visual.moduleformelement.mapper.ModuleFormelementMapper;
import com.jnetdata.msp.visual.moduleformelement.service.ModuleFormelementService;
import com.jnetdata.msp.visual.relationmodulefield.model.RelationModuleField;
import com.jnetdata.msp.visual.relationmodulefield.service.RelationModuleFieldService;
import com.jnetdata.msp.visual.relationmoduletemplate.service.RelationModuleTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ModuleFormelementServiceImpl extends BaseServiceImpl<ModuleFormelementMapper, ModuleFormelement> implements ModuleFormelementService {

    @Autowired
    private RelationModuleTemplateService relationModuleTemplateService;

    @Resource
    private ModuleFormMapper moduleFormMapper;

    @Autowired
    private RelationModuleFieldService relationModuleFieldService;

    @Resource
    private ModuleFormelementMapper moduleFormelementMapper;

    @Override
    public PropertyWrapper<ModuleFormelement> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .getWrapper();
    }

    /**
     * 新增表单元素组件
     */
    @Override
    public JsonResult<EntityId<Long>> insertEntity(ModuleFormelement entity){
        try {
            if(ObjectUtils.isEmpty(entity.getVisualTemplateId())){
                return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "可视化模板id（visualTemplateId）不能为空");
            }
            Long formId = this.getFormId(entity.getVisualTemplateId());
            if(ObjectUtils.isEmpty(formId)){
                return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "请先创建表单组件");
            }
            entity.setCreateTime(new Date());

            //从字段列表中获取字段id
            if(!CollectionUtils.isEmpty(entity.getFieldList())){
                RelationModuleField relation = entity.getFieldList().get(0);
                entity.setDbFieldId(relation.getFieldId());
                entity.setFieldType(relation.getFieldType());
                entity.setDataAreaId(relation.getDataAreaId());
                entity.setFileExtension(relation.getFileExtension());
                entity.setDateType(relation.getDateType());
                entity.setIsChoose(relation.getIsChoose());
            }

            entity.setFormId(formId);
            this.insert(entity);
            relationModuleFieldService.saveRelation(entity.getFieldList(), entity.getId(), ModuleType.FORMELEMENT.value());
            relationModuleTemplateService.saveRelation(entity.getId(), ModuleType.FORMELEMENT.value(), entity.getVisualTemplateId());
            return JsonResult.success(new EntityIdVo(entity.getId()));
        }catch (Exception e){
            log.error("新增表单元素组件异常：{}", e.getMessage());
            return JsonResult.fail("新增失败");
        }
    }

    /**
     * 获取所属表单id
     * @param visualTemplateId 可视化模板id
     */
    private Long getFormId(Long visualTemplateId){
        try {
            List<ModuleForm> list = moduleFormMapper.selectList(new PropertyWrapper<>(ModuleForm.class).eq("visualTemplateId", visualTemplateId).getWrapper());
            if(!CollectionUtils.isEmpty(list)){
                return list.get(0).getId();
            }
        }catch (Exception e){
            log.error("获取所属表单id异常：{}", e.getMessage());
        }
        return null;
    }

    /**
     * 删除表单元素组件
     */
    @Override
    public JsonResult<Void> deleteEntity(Long id){
        try {
            this.deleteById(id);
            relationModuleTemplateService.deleteRelation(id, ModuleType.FORMELEMENT.value());
            return JsonResult.success();
        }catch (Exception e){
            log.error("删除表单元素组件异常：{}", e.getMessage());
            return JsonResult.fail("删除失败");
        }
    }

    /**
     * 修改表单元素组件
     */
    @Override
    public JsonResult<Void> updateEntity(Long id, ModuleFormelement entity){
        try {
            entity.setId(id);
            entity.setUpdateTime(new Date());
            //从字段列表中获取字段id
            if(!CollectionUtils.isEmpty(entity.getFieldList())){
                RelationModuleField relation = entity.getFieldList().get(0);
                entity.setDbFieldId(relation.getFieldId());
                entity.setFieldType(relation.getFieldType());
                //entity.setDateAreaId(entity.getDataAreaId());
                entity.setFileExtension(relation.getFileExtension());
                entity.setDateType(relation.getDateType());
                entity.setIsChoose(relation.getIsChoose());
            }
            this.updateById(entity);
            relationModuleFieldService.deleteRelation(id, ModuleType.FORMELEMENT.value());
            relationModuleFieldService.saveRelation(entity.getFieldList(), id, ModuleType.FORMELEMENT.value());
            return JsonResult.success();
        }catch (Exception e){
            log.error("修改表单元素组件异常：{}", e.getMessage());
            return JsonResult.fail("更新失败");
        }
    }

    /**
     * 查询表单元素组件
     */
    @Override
    public JsonResult<ModuleFormelement> getEntity(Long id){
        try {
            //获取基本信息
            ModuleFormelement entity = this.getById(id);
            if(ObjectUtils.isEmpty(entity)){
                return JsonResult.fail("对象不存在");
            }
            //基本信息放到数组中
            List<RelationModuleField> fieldList =  relationModuleFieldService.getRelation(id, ModuleType.FORMELEMENT.value());
            RelationModuleField field = new RelationModuleField();
            field.setFieldId(entity.getDbFieldId());
            field.setFieldType(entity.getFieldType());
            field.setDataAreaId(entity.getDataAreaId());
            field.setFileExtension(entity.getFileExtension());
            field.setDateType(entity.getDateType());
            field.setIsChoose(entity.getIsChoose());
            fieldList.add(field);
            entity.setFieldList(fieldList);

            return JsonResult.success(entity);
        }catch (Exception e){
            log.error("查询表单元素组件异常：{}", e.getMessage());
            return JsonResult.fail();
        }
    }

}