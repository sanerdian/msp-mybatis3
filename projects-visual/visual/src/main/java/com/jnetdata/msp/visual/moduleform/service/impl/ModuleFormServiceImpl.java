package com.jnetdata.msp.visual.moduleform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.base.vo.EntityIdVo;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.visual.enums.ModuleType;
import com.jnetdata.msp.visual.moduleform.mapper.ModuleFormMapper;
import com.jnetdata.msp.visual.moduleform.model.ModuleForm;
import com.jnetdata.msp.visual.moduleform.service.ModuleFormService;
import com.jnetdata.msp.visual.moduleformelement.mapper.ModuleFormelementMapper;
import com.jnetdata.msp.visual.moduleformelement.model.ModuleFormelement;
import com.jnetdata.msp.visual.moduleformelement.service.ModuleFormelementService;
import com.jnetdata.msp.visual.modulelogin.model.ModuleLogin;
import com.jnetdata.msp.visual.moduleobject.model.ModuleObject;
import com.jnetdata.msp.visual.relationmodulefield.model.RelationModuleField;
import com.jnetdata.msp.visual.relationmodulefield.service.RelationModuleFieldService;
import com.jnetdata.msp.visual.relationmodulefield.vo.ModuleRelation;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import com.jnetdata.msp.visual.relationmoduletemplate.service.RelationModuleTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class ModuleFormServiceImpl extends BaseServiceImpl<ModuleFormMapper, ModuleForm> implements ModuleFormService {

    @Autowired
    private RelationModuleFieldService relationModuleFieldService;
    @Autowired
    private RelationModuleTemplateService relationModuleTemplateService;
    @Autowired
    private ModuleFormelementService moduleFormelementService;
    @Resource
    private ModuleFormelementMapper moduleFormelementMapper;

    @Override
    public PropertyWrapper<ModuleForm> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .eq("formClassId")
                .getWrapper();
    }

    /**
     * 新增表单组件
     */
    @Override
    public JsonResult<EntityId<Long>> insertEntity(ModuleForm entity){
        try {
            if(ObjectUtils.isEmpty(entity.getVisualTemplateId())){
                return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "可视化模板id（visualTemplateId）不能为空");
            }
            entity.setCreateTime(new Date());
            this.insert(entity);
            relationModuleTemplateService.saveRelation(entity.getId(), ModuleType.FORM.value(), entity.getVisualTemplateId());
            return JsonResult.success(new EntityIdVo(entity.getId()));
        }catch (Exception e){
            log.error("新增表单组件异常：{}", e.getMessage());
            return JsonResult.fail("新增失败");
        }
    }


    /**
     * 删除表单组件
     */
    @Override
    public JsonResult<Void> deleteEntity(Long id){
        try {
            this.deleteById(id);
            relationModuleTemplateService.deleteRelation(id, ModuleType.FORM.value());
            return JsonResult.success();
        }catch (Exception e){
            log.error("删除表单组件异常：{}", e.getMessage());
            return JsonResult.fail("删除失败");
        }
    }

    /**
     * 修改表单组件
     */
    @Override
    public JsonResult<Void> updateEntity(Long id, ModuleForm entity){
        try {
            entity.setId(id);
            entity.setUpdateTime(new Date());
            this.updateById(entity);
            return JsonResult.success();
        }catch (Exception e){
            log.error("修改表单组件异常：{}", e.getMessage());
            return JsonResult.fail("更新失败");
        }
    }

    /**
     * 查询表单组件
     */
    @Override
    public JsonResult<ModuleForm> getEntity(Long id){
        try {
            ModuleForm entity = this.getById(id);
            if(ObjectUtils.isEmpty(entity)){
                return JsonResult.fail("对象不存在");
            }
            return JsonResult.success(entity);
        }catch (Exception e){
            log.error("查询表单组件异常：{}", e.getMessage());
            return JsonResult.fail();
        }
    }

    /**
     * 生成对应的js代码
     */
    @Override
    public String generateJs(RelationModuleTemplate relation){
        StringBuilder builder = new StringBuilder();
        try {
            //获取组件详细信息
            ModuleForm moduleForm = this.getById(relation.getModuleId());
            relation.setDbTableId(moduleForm.getDbTableId());

            List<ModuleFormelement> fieldList =  moduleFormelementMapper.selectList(new PropertyWrapper<>(ModuleFormelement.class).eq("formId",moduleForm.getId()).getWrapper());

            //获取组件关联的元数据信息（接口名，字段列表）
            ModuleRelation moduleRelation = relationModuleFieldService.getModuleRelationEntity(relation);


             builder.append("\nfunction formSubmit(){")
                    .append("\n\t//服务id")
                    .append("\n\tvar serviceId = \"")
                    .append(moduleRelation.getServiceName()).append("\";")
                    .append("\n\t//接口地址")
                    .append("\n\tvar url = '/")
                    .append(moduleRelation.getEntityName()).append("';")
                    .append("\n//浏览器地址")
                    .append("\n\tvar linkUrl = window.location.href;")
                    .append("\n\tif(linkUrl.indexOf('?id=') != -1){")
                    .append("\n\t\tvar id = linkUrl.split('?')[1].split('=')[1];")
                    .append("\n\t}")
                     .append("\nif(id){")
                     .append("\n\tajax(serviceId, url + '/' + id, \"get\", {}).then(function (data) {")
                     .append("\n\t\tif (data.success) {")
                     .append("\n\t\t\tform.val('formFilter', data.obj);")
                     .append("\n\t\t\t//编辑提交")
                     .append("\n\t\t\tform.on('submit(visual_submit)', function(data){")
                     .append("\n\t\t\t\tajax(serviceId, url + '/' + id, \"put\", JSON.stringify(data.field)).then(function(data){")
                     .append("\n\t\t\t\t\tif(data.success){")
                     .append("\n\t\t\t\t\t\tlayer.msg('编辑成功');")
                     .append("\n\t\t\t\t\t}else{")
                     .append("\n\t\t\t\t\t\tconsole.log(data.msg);")
                     .append("\n\t\t\t\t\t}")
                     .append("\n\t\t\t\t});")
                     .append("\n\t\t\t\treturn false;")
                     .append("\n\t\t\t});")
                     .append("\n\t\t} else {")
                     .append("\n\t\t\t console.log(data.msg);")
                     .append("\n\t\t}")
                     .append("\n\t });")
                     .append("\n}else{")
                    .append("\n\t//提交事件")
                    .append("\n\tform.on('submit(visual_submit)', function(data){")
                    .append("\n\t\tajax(serviceId, url, \"post\", JSON.stringify(data.field)).then(function(data){")
                    .append("\n\t\t\tif(data.success){")
                    .append("\n\t\t\t\tlayer.msg('新建成功');")
                    .append("\n\t\t\t}else{")
                    .append("\n\t\t\t\tconsole.log(data.msg);")
                    .append("\n\t\t\t}")
                    .append("\n\t\t});")
                    .append("\n\t\treturn false;")
                    .append("\n\t});")
                    .append("\n}")
                    .append("\n}")
                    .append("\n$(function(){")
                    .append("\n\tformSubmit();");

            for(ModuleFormelement moduleFormelement:fieldList) {
                if ("14".equals(moduleFormelement.getFieldType())
                        || "15".equals(moduleFormelement.getFieldType())
                        || "16".equals(moduleFormelement.getFieldType())
                        || "17".equals(moduleFormelement.getFieldType())) {
                     builder.append("\n\tfileUpload('#")
                            .append(moduleFormelement.getDataAreaId())
                            .append("', 'member', '/user/importHead', '");
                    if ("14".equals(moduleFormelement.getFieldType())
                            || "16".equals(moduleFormelement.getFieldType())
                            || "17".equals(moduleFormelement.getFieldType())) {
                        builder.append("file");
                    }
                    if ("15".equals(moduleFormelement.getFieldType())) {
                        builder.append("images");
                    }
                    builder.append("', '")
                            .append(moduleFormelement.getFileExtension())
                            .append("');");
                }else if("4".equals(moduleFormelement.getFieldType())) {
                    builder.append("\n\tsetDate('#")
                            .append(moduleFormelement.getDataAreaId())
                            .append("Date")
                            .append("', '")
                            .append(moduleFormelement.getDateType())
                            .append("');");
                }
            }

            builder .append("\n})");

        }catch (Exception e){
            log.error("生成对应的js代码异常：{}", e.getMessage());
        }
        return builder.toString();
    }

    @Override
    public String generateCss(RelationModuleTemplate relation){
        StringBuilder builder = new StringBuilder();
        ModuleForm moduleForm = this.getById(relation.getModuleId());
        List<ModuleFormelement> fieldList =  moduleFormelementMapper.selectList(new PropertyWrapper<>(ModuleFormelement.class).eq("formId",moduleForm.getId()).getWrapper());
        for(ModuleFormelement moduleFormelement:fieldList) {
            ModuleObject module = null;
            module = moduleFormelementService.getById(moduleFormelement.getId());
            builder.append(this.getCssCode(module));
        }
        return builder.toString();
    }




    /**
     * 根据表单元素属性组织css代码
     * @param module
     * @author chunping
     * @date 2023/1/4
     */
    private String getCssCode(ModuleObject module){
        if(ObjectUtils.isEmpty(module) || StringUtils.isEmpty(module.getStyleName())){
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(".").append(module.getStyleName()).append(",\n")
                .append(".").append(module.getStyleName()).append(" a,\n")
                .append(".").append(module.getStyleName()).append(" span,\n")
                .append(".").append(module.getStyleName()).append(" th,\n")
                .append(".").append(module.getStyleName()).append(" td,\n")
                .append(".").append(module.getStyleName()).append(" select,\n")
                .append(".").append(module.getStyleName()).append(" input,\n")
                .append(".").append(module.getStyleName()).append(" button,\n")
                .append(".").append(module.getStyleName()).append(" ul li a,\n")
                .append(".").append(module.getStyleName()).append(" .layui-laypage .layui-laypage-skip{\n");
        if(!StringUtils.isEmpty(module.getFontFamily())){
            builder.append("\tfont-family:\"").append(module.getFontFamily()).append("\";\n");
        }
        if(!StringUtils.isEmpty(module.getFontSize())){
            builder.append("\tfont-size:").append(module.getFontSize()).append("px;\n");
        }
        if(!StringUtils.isEmpty(module.getFontColor())){
            builder.append("\tcolor:").append(module.getFontColor()).append(";\n");
        }
        builder.append("}\n");
        return builder.toString();
    }
}