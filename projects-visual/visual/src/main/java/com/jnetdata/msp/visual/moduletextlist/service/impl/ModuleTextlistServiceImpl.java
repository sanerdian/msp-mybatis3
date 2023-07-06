package com.jnetdata.msp.visual.moduletextlist.service.impl;

import com.jnetdata.msp.base.vo.EntityIdVo;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.manage.column.mapper.ProgramaMapper;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.visual.enums.Logic;
import com.jnetdata.msp.visual.enums.ModuleType;
import com.jnetdata.msp.visual.moduletextlist.mapper.ModuleTextlistMapper;
import com.jnetdata.msp.visual.moduletextlist.model.ModuleTextlist;
import com.jnetdata.msp.visual.moduletextlist.service.ModuleTextlistService;
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
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ModuleTextlistServiceImpl extends BaseServiceImpl<ModuleTextlistMapper, ModuleTextlist> implements ModuleTextlistService {

    @Autowired
    private RelationModuleFieldService relationModuleFieldService;

    @Autowired
    private RelationModuleTemplateService relationModuleTemplateService;

    @Resource
    private ProgramaMapper programaMapper;

    @Override
    public PropertyWrapper<ModuleTextlist> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .getWrapper();
    }

    /**
     * 新增文字列表组件
     */
    @Override
    public JsonResult<EntityId<Long>> insertEntity(ModuleTextlist entity){
        try {
            if(ObjectUtils.isEmpty(entity.getVisualTemplateId())){
                return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "可视化模板id（visualTemplateId）不能为空");
            }
            entity.setCreateTime(new Date());
            this.insert(entity);
            relationModuleFieldService.saveRelation(entity.getFieldList(), entity.getId(), ModuleType.TEXTLIST.value());
            relationModuleTemplateService.saveRelation(entity.getId(), ModuleType.TEXTLIST.value(), entity.getVisualTemplateId());
            return JsonResult.success(new EntityIdVo(entity.getId()));
        }catch (Exception e){
            log.error("新增文字列表组件异常：{}", e.getMessage());
            return JsonResult.fail("新增失败");
        }
    }

    /**
     * 删除文字列表组件
     */
    @Override
    public JsonResult<Void> deleteEntity(Long id){
        try {
            this.deleteById(id);
            relationModuleFieldService.deleteRelation(id, ModuleType.TEXTLIST.value());
            relationModuleTemplateService.deleteRelation(id, ModuleType.TEXTLIST.value());
            return JsonResult.success();
        }catch (Exception e){
            log.error("删除文字列表组件异常：{}", e.getMessage());
            return JsonResult.fail("删除失败");
        }
    }

    /**
     * 修改文字列表组件
     */
    @Override
    public JsonResult<Void> updateEntity(Long id, ModuleTextlist entity){
        try {
            entity.setId(id);
            entity.setUpdateTime(new Date());
            this.updateById(entity);
            relationModuleFieldService.deleteRelation(id, ModuleType.TEXTLIST.value());
            relationModuleFieldService.saveRelation(entity.getFieldList(), id, ModuleType.TEXTLIST.value());
            return JsonResult.success();
        }catch (Exception e){
            log.error("修改文字列表组件异常：{}", e.getMessage());
            return JsonResult.fail("更新失败");
        }
    }

    /**
     * 查询文字列表组件
     */
    @Override
    public JsonResult<ModuleTextlist> getEntity(Long id){
        try {
            ModuleTextlist entity = this.getById(id);
            if(ObjectUtils.isEmpty(entity)){
                return JsonResult.fail("对象不存在");
            }

            List<RelationModuleField> list = relationModuleFieldService.getRelation(id, ModuleType.TEXTLIST.value());
            entity.setFieldList(list);

            return JsonResult.success(entity);
        }catch (Exception e){
            log.error("查询文字列表组件异常：{}", e.getMessage());
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
            ModuleTextlist moduleList = this.getById(relation.getModuleId());
            //组件关联的栏目信息
            Programa programa = null;
            if(!ObjectUtils.isEmpty(moduleList.getChannelId())){
                programa = programaMapper.selectById(moduleList.getChannelId());
            }

            //获取组件关联的元数据信息（接口名，字段列表）
            relation.setDbTableId(moduleList.getDbTableId());
            ModuleRelation moduleRelation = relationModuleFieldService.getModuleRelationEntity(relation);

            //获取关联的字段信息
            List<RelationModuleField> fieldList = moduleRelation.getFieldList();
            if(CollectionUtils.isEmpty(fieldList)){
                log.info("组件没有关联字段信息");
                return builder.toString();
            }

            //分页信息
            String pageFlag = moduleList.getPageSetup();
            int pageSize = ObjectUtils.isEmpty(moduleList.getPageSize()) ? 10 : moduleList.getPageSize();

            //生成url和serviceId
            builder.append("\nvar serviceId").append(relation.getOrder()).append(" = \"").append(moduleRelation.getServiceName()).append("\";")
                    .append("\nvar url").append(relation.getOrder()).append(" = \'/").append(moduleRelation.getEntityName()).append("\';");
            //函数js
            builder.append(this.getListFunction(programa, fieldList, relation.getOrder(),pageFlag,relation));
            builder.append(this.getTitleFunction(programa, relation.getOrder()));

            //函数调用js
            builder.append("\n$(function(){");
            if(!ObjectUtils.isEmpty(programa)){
                builder.append("\n\trenderTitle")
                        .append(relation.getOrder()).append("(\"")
                        .append(moduleList.getDataAreaId()).append("\");");
            }
            builder.append("\n\trenderList")
                    .append(relation.getOrder()).append("(")
                    .append("serviceId").append(relation.getOrder()).append(", ")
                    .append("url").append(relation.getOrder()).append(", 1, ")
                    .append(pageSize).append(", {}, \"")
                    .append(moduleList.getDataAreaId()).append("\");")
                    .append("\n})");

            return builder.toString();
        }catch (Exception e){
            log.error("生成对应的js代码异常：{}", e.getMessage());
        }
        return builder.toString();
    }

    /**
     * 获取文字列表标题部分的js函数
     */
    private String getTitleFunction(Programa programa, Integer order){
        if(ObjectUtils.isEmpty(programa)){return "";}
        StringBuilder titlePart = new StringBuilder();
        titlePart.append("\n//获取文字列表标题")
                .append("\nfunction renderTitle")
                .append(order).append("(listView){")
                .append("\n\tvar titleHtml = \"\\n\\t<div class=\\\"more\\\"><a href=\\\"")
                .append(programa.getListUrl()).append("\\\">更多 &gt;</a></div>\";")
                .append("\n\ttitleHtml += \"<h3><a href=\\\"")
                .append(programa.getListUrl()).append("\\\">")
                .append(programa.getName()).append("</a></h3>\";")
                .append("\n\t$(\"#\" + listView + \" .visual_title\").html(titleHtml);")
                .append("\n}");
        return titlePart.toString();
    }

    /**
     * 获取文字列表js函数
     */
    private String getListFunction(Programa programa, List<RelationModuleField> fieldList, int order, String pageFlag, RelationModuleTemplate relation){
        ModuleTextlist moduleList = this.getById(relation.getModuleId());
        //根据配置，获取文字的链接或事件
        String hrefStr = "href='#'";
        RelationModuleField first = fieldList.get(0);
        if(!StringUtils.isEmpty(first.getEventType())){
            //字段关联事件时，添加对应的函数名
            hrefStr = "onclick='" + first.getFieldname() + first.getEventType() + "(\" + arr[i].id + \")'";
        }else if(!ObjectUtils.isEmpty(programa) && !StringUtils.isEmpty(programa.getDetailUrl())){
            //组件关联栏目时，获取对应的详情页面url
            hrefStr = "href='" + programa.getDetailUrl() + "?id=\" + arr[i].id + \"'";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("\n//获取文字列表数据")
                .append("\nfunction renderList").append(order).append("(serviceId").append(order).append(", url").append(order).append(", curr, limit, entity, listView){")
                .append("\n\tvar params = {")
                .append("\n\t\t\"entity\": entity,")
                .append("\n\t\t\"pager\": {")
                .append("\n\t\t\tcurrent: curr,")
                .append("\n\t\t\tsize: limit")
                .append("\n\t\t}")
                .append("\n\t};")
                .append("\n\tajax(serviceId").append(order).append(", url").append(order).append(" + \"/listing\", \"post\", JSON.stringify(params)).then(function(data){")
                .append("\n\t\tif(data.success){")
                .append("\n\t\t\tvar arr = data.obj.records;")
                .append("\n\t\t\tvar innerHtml = \"<ul>\";")
                .append("\n\t\t\tfor (var i = 0; i < arr.length; i++) {")
                .append("\n\t\t\t\tinnerHtml += \"\\n\\t<li>\\n\\t\\t<h4><a ")
                .append(hrefStr)
                .append(">\" + arr[i].")
                .append(fieldList.get(0).getFieldname())
                .append(" + \"</a></h4>\";");

        //多字段情况
        if(fieldList.size() > 1){
            for (int i = 1; i < fieldList.size(); i++) {
                builder.append("\n\t\t\t\tinnerHtml += \"\\n\\t\\t<span>\" + arr[i].").append(fieldList.get(i).getFieldname()).append(" + \"</span>\";");
            }
        }

        builder.append("\n\t\t\t\tinnerHtml += \"\\n\\t</li>\";")
                .append("\n\t\t\t}")
                .append("\n\t\t\tinnerHtml += \"\\n</ul>\";")
                .append("\n\t\t\t$(\"#\" + listView + \" .visual_text_list_view\").html(innerHtml);");
        if(Logic.YES.getCode().equals(pageFlag)){
                builder.append("\n\t\t\tpageForTextlist(data.obj.total, curr, limit, renderList")
                        .append(order).append(", '")
                        .append(moduleList.getDataAreaId())
                        .append("Page', entity, listView,serviceId")
                        .append(order)
                        .append(",url")
                        .append(order)
                        .append(");");

        }else {
            builder.append("\n\t\t\t$('#")
                    .append(moduleList.getDataAreaId()).append(".visual_page")
                    .append("').remove();");

        }
                builder.append("\n\t\t}else{")
                .append("\n\t\t\tconsole.log(data.msg);")
                .append("\n\t\t}")
                .append("\n\t});")
                .append("\n}");
        return builder.toString();
    }



}