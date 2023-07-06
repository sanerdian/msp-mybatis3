package com.jnetdata.msp.visual.moduledetail.service.impl;

import com.jnetdata.msp.base.vo.EntityIdVo;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.visual.enums.ModuleType;
import com.jnetdata.msp.visual.moduledetail.mapper.ModuleDetailMapper;
import com.jnetdata.msp.visual.moduledetail.model.ModuleDetail;
import com.jnetdata.msp.visual.moduledetail.service.ModuleDetailService;
import com.jnetdata.msp.visual.relationmodulefield.model.RelationModuleField;
import com.jnetdata.msp.visual.relationmodulefield.service.RelationModuleFieldService;
import com.jnetdata.msp.visual.relationmodulefield.vo.ModuleRelation;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
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

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ModuleDetailServiceImpl extends BaseServiceImpl<ModuleDetailMapper, ModuleDetail> implements ModuleDetailService {

    @Autowired
    private RelationModuleFieldService relationModuleFieldService;

    @Autowired
    private RelationModuleTemplateService relationModuleTemplateService;

    @Override
    public PropertyWrapper<ModuleDetail> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .getWrapper();
    }

    /**
     * 新增详情组件
     */
    @Override
    public JsonResult<EntityId<Long>> insertEntity(ModuleDetail entity){
        try {
            if(ObjectUtils.isEmpty(entity.getVisualTemplateId())){
                return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "可视化模板id（visualTemplateId）不能为空");
            }
            entity.setCreateTime(new Date());
            this.insert(entity);
            relationModuleFieldService.saveRelation(entity.getFieldList(), entity.getId(), ModuleType.DETAIL.value());
            relationModuleTemplateService.saveRelation(entity.getId(), ModuleType.DETAIL.value(), entity.getVisualTemplateId());
            return JsonResult.success(new EntityIdVo(entity.getId()));
        }catch (Exception e){
            log.error("新增详情组件异常：{}", e.getMessage());
            return JsonResult.fail("新增失败");
        }
    }

    /**
     * 删除详情组件
     */
    @Override
    public JsonResult<Void> deleteEntity(Long id){
        try {
            this.deleteById(id);
            relationModuleFieldService.deleteRelation(id, ModuleType.DETAIL.value());
            relationModuleTemplateService.deleteRelation(id, ModuleType.DETAIL.value());
            return JsonResult.success();
        }catch (Exception e){
            log.error("删除详情组件异常：{}", e.getMessage());
            return JsonResult.fail("删除失败");
        }
    }

    /**
     * 修改详情组件
     */
    @Override
    public JsonResult<Void> updateEntity(Long id, ModuleDetail entity){
        try {
            entity.setUpdateTime(new Date());
            entity.setId(id);
            this.updateById(entity);
            relationModuleFieldService.deleteRelation(id, ModuleType.DETAIL.value());
            relationModuleFieldService.saveRelation(entity.getFieldList(), id, ModuleType.DETAIL.value());
            return JsonResult.success();
        }catch (Exception e){
            log.error("修改详情组件异常：{}", e.getMessage());
            return JsonResult.fail("更新失败");
        }
    }

    /**
     * 查询详情组件
     */
    @Override
    public JsonResult<ModuleDetail> getEntity(Long id){
        try {
            ModuleDetail entity = this.getById(id);
            if(ObjectUtils.isEmpty(entity)){
                return JsonResult.fail("对象不存在");
            }

            List<RelationModuleField> list = relationModuleFieldService.getRelation(id, ModuleType.DETAIL.value());
            entity.setFieldList(list);

            return JsonResult.success(entity);
        }catch (Exception e){
            log.error("查询详情组件异常：{}", e.getMessage());
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
            //获取id的函数定义
            builder.append("\n//从url中获取id")
                    .append("\nfunction getId(){")
                    .append("\n\tvar url = location.search;")
                    .append("\n\tif(url.indexOf(\"?\") != -1){")
                    .append("\n\t\tvar params = url.substr(1).split(\"&\");")
                    .append("\n\t\tfor(var i=0; i<params.length; i++){")
                    .append("\n\t\t\tif(params[i].split(\"=\")[0] == \"id\"){")
                    .append("\n\t\t\t\treturn params[i].split(\"=\")[1];")
                    .append("\n\t\t\t}")
                    .append("\n\t\t}")
                    .append("\n\t}")
                    .append("\n\treturn \"\";")
                    .append("\n}");

            //获取组件关联的元数据信息（字段信息、模块名、实体名）
            ModuleRelation moduleRelation = relationModuleFieldService.getModuleRelationEntity(relation);
            if(!ObjectUtils.isEmpty(moduleRelation) && !CollectionUtils.isEmpty(moduleRelation.getFieldList())){
                //详情的标题
                builder.append("\nvar id = getId();")
                        .append("\najax(\"\",\"/")
                        .append(moduleRelation.getServiceName()).append("/").append(moduleRelation.getEntityName()).append("/")
                        .append("\" + id, \"get\", {}).then(function(data){")
                        .append("\n\tif(data.success){")
                        .append("\n\t\t//标题")
                        .append("\n\t\tvar titleHtml = \"<h2>\" + data.obj.")
                        .append(moduleRelation.getFieldList().get(0).getFieldname())
                        .append(" + \"</h2>\";")
                        .append("\n\t\t$(\"#visual_title\").html(titleHtml);")
                        .append("\n\t\t//字段信息")
                        .append("\n\t\tvar infoHtml = \"<ul>\";");
                //循环获取字段信息
                for (RelationModuleField field: moduleRelation.getFieldList()) {
                    builder.append("\n\t\tinfoHtml += \"<li><span>")
                            .append(field.getAnothername())
                            .append("：</span><p>\" + data.obj.")
                            .append(field.getFieldname())
                            .append(" + \"</p></li>\";");
                }
                //渲染页面
                builder.append("\n\t\tinfoHtml += \"</ul>\";")
                        .append("\n\t\t$(\"#visual_info\").html(infoHtml);")
                        .append("\n\t}")
                        .append("\n});");
            }
        }catch (Exception e){
            log.error("生成对应的js代码异常：{}", e.getMessage());
        }
        return builder.toString();
    }
}
