package com.jnetdata.msp.visual.modulecarousel.service.impl;

import com.jnetdata.msp.base.vo.EntityIdVo;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.visual.enums.ModuleType;
import com.jnetdata.msp.visual.modulecarousel.mapper.ModuleCarouselMapper;
import com.jnetdata.msp.visual.modulecarousel.model.ModuleCarousel;
import com.jnetdata.msp.visual.modulecarousel.service.ModuleCarouselService;
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

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ModuleCarouselServiceImpl extends BaseServiceImpl<ModuleCarouselMapper, ModuleCarousel> implements ModuleCarouselService {

    @Autowired
    private RelationModuleFieldService relationModuleFieldService;

    @Autowired
    private RelationModuleTemplateService relationModuleTemplateService;

    @Override
    public PropertyWrapper<ModuleCarousel> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .getWrapper();
    }

    /**
     * 新增轮播组件
     */
    @Override
    public JsonResult<EntityId<Long>> insertEntity(ModuleCarousel entity){
        try {
            if(ObjectUtils.isEmpty(entity.getVisualTemplateId())){
                return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "可视化模板id（visualTemplateId）不能为空");
            }
            entity.setCreateTime(new Date());
            this.insert(entity);
            relationModuleFieldService.saveRelation(entity.getFieldList(), entity.getId(), ModuleType.CAROUSEL.value());
            relationModuleTemplateService.saveRelation(entity.getId(), ModuleType.CAROUSEL.value(), entity.getVisualTemplateId());
            return JsonResult.success(new EntityIdVo(entity.getId()));
        }catch (Exception e){
            log.error("新增轮播组件异常：{}", e.getMessage());
            return JsonResult.fail("新增失败");
        }
    }

    /**
     * 删除轮播组件
     */
    @Override
    public JsonResult<Void> deleteEntity(Long id){
        try {
            this.deleteById(id);
            relationModuleFieldService.deleteRelation(id, ModuleType.CAROUSEL.value());
            relationModuleTemplateService.deleteRelation(id, ModuleType.CAROUSEL.value());
            return JsonResult.success();
        }catch (Exception e){
            log.error("删除轮播组件异常：{}", e.getMessage());
            return JsonResult.fail("删除失败");
        }
    }

    /**
     * 修改轮播组件
     */
    @Override
    public JsonResult<Void> updateEntity(Long id, ModuleCarousel entity){
        try {
            entity.setId(id);
            entity.setUpdateTime(new Date());
            this.updateById(entity);
            relationModuleFieldService.deleteRelation(id, ModuleType.CAROUSEL.value());
            relationModuleFieldService.saveRelation(entity.getFieldList(), id, ModuleType.CAROUSEL.value());
            return JsonResult.success();
        }catch (Exception e){
            log.error("修改轮播组件异常：{}", e.getMessage());
            return JsonResult.fail("更新失败");
        }
    }

    /**
     * 查询轮播组件
     */
    @Override
    public JsonResult<ModuleCarousel> getEntity(Long id){
        try {
            ModuleCarousel entity = this.getById(id);
            if(ObjectUtils.isEmpty(entity)){
                return JsonResult.fail("对象不存在");
            }

            List<RelationModuleField> list = relationModuleFieldService.getRelation(id, ModuleType.CAROUSEL.value());
            entity.setFieldList(list);

            return JsonResult.success(entity);
        }catch (Exception e){
            log.error("查询轮播组件异常：{}", e.getMessage());
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
            ModuleCarousel moduleCarousel = this.getById(relation.getModuleId());

            //获取组件关联的元数据信息（接口名，字段列表）
            ModuleRelation moduleRelation = relationModuleFieldService.getModuleRelationEntity(relation);

            //获取关联的字段信息
            List<RelationModuleField> fieldList = moduleRelation.getFieldList();
            if(CollectionUtils.isEmpty(fieldList)){
                log.info("组件没有关联字段信息");
                return builder.toString();
            }
            RelationModuleField first = fieldList.get(0);
            RelationModuleField second = fieldList.size() > 1 ? fieldList.get(1) : null;

            int pageSize = ObjectUtils.isEmpty(moduleCarousel.getPageSize()) ? 10 : moduleCarousel.getPageSize();

            //生成函数
            builder.append("\n//获取轮播图数据")
                    .append("\nfunction renderCarousel").append(relation.getOrder()).append("(serviceId").append(relation.getOrder()).append(", url").append(relation.getOrder()).append(", curr, limit, entity, listView){")
                    .append("\n\tvar params = {")
                    .append("\n\t\t\"entity\": entity,")
                    .append("\n\t\t\"pager\": {")
                    .append("\n\t\t\tcurrent: curr,")
                    .append("\n\t\t\tsize: limit")
                    .append("\n\t\t}")
                    .append("\n\t};")
                    .append("\n\tajax(serviceId").append(relation.getOrder()).append(", url").append(relation.getOrder()).append(" + \"/listing\", \"post\", JSON.stringify(params)).then(function(data){")
                    .append("\n\t\tif(data.success){")
                    .append("\n\t\t\tvar arr = data.obj.records;")
                    .append("\n\t\t\tvar innerHtml = \"\";")
                    .append("\n\t\t\tfor (var i = 0; i < arr.length; i++) {")
                    .append("\n\t\t\t\tinnerHtml += \"\\n<div class=\\\"swiper-slide\\\" data-swiper-slide-index=\\\"\" + i + \"\\\">\";");
                    if(StringUtils.isEmpty(second.getEventType())){
                    builder.append("\n\t\t\t\tinnerHtml += \"\\n\\t<a href=\\\"#\\\"><img src=\\\"\" + arr[i].")
                    .append(second.getFieldname()).append(" + \"\\\"></a>\";");
                    }else {
                        builder.append("\n\t\t\t\tinnerHtml += \"\\n\\t<a ")
                                .append("onclick='")
                                .append(second.getFieldname())
                                .append(second.getEventType())
                                .append("(\" + arr[i].id+\")'")
                                .append(">")
                                .append("<img src=\\\"\" + arr[i].")
                                .append(second.getFieldname())
                                .append(" + \"\\\"></a>\";");
                    }
                    if(StringUtils.isEmpty(first.getEventType())){
                        builder.append("\n\t\t\t\tinnerHtml += \"\\n\\t<h3><a href=\\\"#\\\">\" + arr[i].")
                               .append(first.getFieldname())
                               .append(" + \"</a></h3>\";");
                    }
                    else{
                        builder.append("\n\t\t\t\tinnerHtml += \"\\n\\t<h3><a ")
                                .append("onclick='")
                                .append(first.getFieldname())
                                .append(first.getEventType())
                                .append("(\" + arr[i].id + \")")
                                .append("'>")
                                .append("\" + arr[i].")
                                .append(first.getFieldname())
                                .append(" + \"</a></h3>\";");
                    }
                    builder.append("\n\t\t\t\tinnerHtml += \"\\n\\t<div class=\\\"visual_bg\\\"></div>\";")
                    .append("\n\t\t\t\tinnerHtml += \"\\n</div>\";")
                    .append("\n\t\t\t}")
                    .append("\n\t\t\t$(\"#\" + listView + \" .swiper-wrapper\").html(innerHtml);");
                    if(moduleCarousel.getPageSize()<=1) {
                        builder.append("\n\t\t\tvar swiper = new Swiper('#")
                                .append(moduleCarousel.getDataAreaId())
                                .append(" .swiper-container', {")
                                .append("\n\t\t\t\tpagination: '#")
                                .append(moduleCarousel.getDataAreaId())
                                .append(" .swiper-pagination',")
                                .append("\n\t\t\t\tnextButton: '#")
                                .append(moduleCarousel.getDataAreaId())
                                .append(" .swiper-button-next',")
                                .append("\n\t\t\t\tprevButton: '#")
                                .append(moduleCarousel.getDataAreaId())
                                .append(" .swiper-button-prev',")
                                .append("\n\t\t\t\tslidesPerView:1,")
                                .append("\n\t\t\t\tpaginationClickable: true,")
                                .append("\n\t\t\t\tloop: true")
                                .append("\n\t\t\t});")
                                .append("\n\t\t}else{")
                                .append("\n\t\t\tconsole.log(data.msg);")
                                .append("\n\t\t}")
                                .append("\n\t});")
                                .append("\n}");

                    }else if(moduleCarousel.getPageSize()>1){
                        builder.append("\n\t\t\tvar swiper = new Swiper('#")
                                .append(moduleCarousel.getDataAreaId())
                                .append(" .swiper-container', {")
                                .append("\n\t\t\t\tpagination: '#")
                                .append(moduleCarousel.getDataAreaId())
                                .append(" .swiper-pagination',")
                                .append("\n\t\t\t\tnextButton: '#")
                                .append(moduleCarousel.getDataAreaId())
                                .append(" .swiper-button-next',")
                                .append("\n\t\t\t\tprevButton: '#")
                                .append(moduleCarousel.getDataAreaId())
                                .append(" .swiper-button-prev',")
                                .append("\n\t\t\t\tspeed: 500,")
                                .append("\n\t\t\t\tautoplay : 5000,")
                                .append("\n\t\t\t\tslidesPerView: ")
                                .append(moduleCarousel.getPageSize())
                                .append(",")
                                .append("\n\t\t\t\tspaceBetween: 20,")
                                .append("\n\t\t\t\tpaginationClickable: true,")
                                .append("\n\t\t\t\tloop: true")
                                .append("\n\t\t\t});")
                                .append("\n\t\t}else{")
                                .append("\n\t\t\tconsole.log(data.msg);")
                                .append("\n\t\t}")
                                .append("\n\t});")
                                .append("\n}");

                    }
            //生成函数调用
            builder.append("\n$(function(){")
                    .append("\n\trenderCarousel").append(relation.getOrder()).append("(\"")
                    .append(moduleRelation.getServiceName())
                    .append("\", \"/")
                    .append(moduleRelation.getEntityName())
                    .append("\", 1, ")
                    .append(moduleCarousel.getPageTotal())
                    .append(", {}, \"")
                    .append(moduleCarousel.getDataAreaId())
                    .append("\");")
                    .append("\n})");
        }catch (Exception e){
            log.error("生成对应的js代码异常：{}", e.getMessage());
        }
        return builder.toString();
    }

}