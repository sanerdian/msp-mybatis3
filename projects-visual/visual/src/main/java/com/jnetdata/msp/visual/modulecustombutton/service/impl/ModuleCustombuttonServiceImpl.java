package com.jnetdata.msp.visual.modulecustombutton.service.impl;

import com.jnetdata.msp.base.vo.EntityIdVo;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.visual.enums.ModuleType;
import com.jnetdata.msp.visual.modulebutton.mapper.ModuleButtonMapper;
import com.jnetdata.msp.visual.modulebutton.model.ModuleButton;
import com.jnetdata.msp.visual.modulecustombutton.mapper.ModuleCustombuttonMapper;
import com.jnetdata.msp.visual.modulecustombutton.model.ModuleCustombutton;
import com.jnetdata.msp.visual.modulecustombutton.service.ModuleCustombuttonService;
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

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ModuleCustombuttonServiceImpl extends BaseServiceImpl<ModuleCustombuttonMapper, ModuleCustombutton> implements ModuleCustombuttonService {

    @Autowired
    private RelationModuleTemplateService relationModuleTemplateService;

    @Resource
    private ModuleButtonMapper moduleButtonMapper;

    @Override
    public PropertyWrapper<ModuleCustombutton> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .getWrapper();
    }

    /**
     * 新增自定义按钮组件
     */
    @Override
    public JsonResult<EntityId<Long>> insertEntity(ModuleCustombutton entity){
        try {
            if(ObjectUtils.isEmpty(entity.getVisualTemplateId())){
                return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "可视化模板id（visualTemplateId）不能为空");
            }
            entity.setCreateTime(new Date());
            this.insert(entity);
            relationModuleTemplateService.saveRelation(entity.getId(), ModuleType.CUSTOMBUTTON.value(), entity.getVisualTemplateId());

            this.updateList(entity.getButtonList(), entity.getId());
            return JsonResult.success(new EntityIdVo(entity.getId()));
        }catch (Exception e){
            log.error("新增自定义按钮组件异常：{}", e.getMessage());
            return JsonResult.fail("新增失败");
        }
    }

    /**
     * 更新关联的按钮组件
     * @param buttonList 按钮组件列表
     * @param parentId 自定义按钮组件id
     */
    private void updateList(List<ModuleButton> buttonList, Long parentId){
        //如果存在关联的按钮组件，删除
        if(ObjectUtils.isEmpty(parentId)){ return; }
        List<ModuleButton> oldList = moduleButtonMapper.selectList(new PropertyWrapper<>(ModuleButton.class).eq("parentId", parentId).getWrapper());
        if(!CollectionUtils.isEmpty(oldList)){
            for(ModuleButton old: oldList){
                moduleButtonMapper.deleteById(old.getId());
            }
        }
        //新增
        if(CollectionUtils.isEmpty(buttonList)){ return; }
        for(ModuleButton moduleButton: buttonList){
            moduleButton.setParentId(parentId);
            moduleButton.setCreateTime(new Date());
            moduleButton.setVisualTemplateId(null);//存在上级组件时，可视化模板id置空，防止重复操作
            moduleButtonMapper.insert(moduleButton);
        }
    }

    /**
     * 删除自定义按钮组件
     */
    @Override
    public JsonResult<Void> deleteEntity(Long id){
        try {
            this.deleteById(id);
            relationModuleTemplateService.deleteRelation(id, ModuleType.CUSTOMBUTTON.value());
            this.updateList(null, id);
            return JsonResult.success();
        }catch (Exception e){
            log.error("删除自定义按钮组件异常：{}", e.getMessage());
            return JsonResult.fail("删除失败");
        }
    }

    /**
     * 修改自定义按钮组件
     */
    @Override
    public JsonResult<Void> updateEntity(Long id, ModuleCustombutton entity){
        try {
            ModuleCustombutton po = this.getById(id);
            if(ObjectUtils.isEmpty(po)){
                return JsonResult.fail("对象不存在");
            }

            entity.setId(id);
            entity.setUpdateTime(new Date());
            this.updateById(entity);
            this.updateList(entity.getButtonList(), id);
            return JsonResult.success();
        }catch (Exception e){
            log.error("修改自定义按钮组件异常：{}", e.getMessage());
            return JsonResult.fail("更新失败");
        }
    }

    /**
     * 查询自定义按钮组件
     */
    @Override
    public JsonResult<ModuleCustombutton> getEntity(Long id){
        try {
            ModuleCustombutton entity = this.getById(id);
            if(ObjectUtils.isEmpty(entity)){
                return JsonResult.fail("对象不存在");
            }
            List<ModuleButton> buttonList = moduleButtonMapper.selectList(new PropertyWrapper<>(ModuleButton.class).eq("parentId", id).getWrapper());
            entity.setButtonList(buttonList);

            return JsonResult.success(entity);
        }catch (Exception e){
            log.error("查询自定义按钮组件异常：{}", e.getMessage());
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
            ModuleCustombutton entity = this.getById(relation.getModuleId());
            List<ModuleButton> buttonList = moduleButtonMapper.selectList(new PropertyWrapper<>(ModuleButton.class).eq("parentId", relation.getModuleId()).getWrapper());
            entity.setButtonList(buttonList);

            //组织js代码
            builder.append("\n$(function(){")
                    .append("\n\t//按钮列表")
                    .append("\n\tvar buttonJson = [");
            for(ModuleButton button: buttonList){
                builder.append("{\n\t\t\"buttonName\":\"")
                        .append(button.getButtonName())
                        .append("\",\n\t\t\"buttonEvent\":\"")
                        .append(button.getAnotherName())
                        .append(button.getEventType())
                        .append("\"\n\t},");
            }
            builder.append("];")
                    .append("\n\t//组织html字符串")
                    .append("\n\tvar htmlStr = \"<ul>\"")
                    .append("\n\tfor (var i = 0; i < buttonJson.length; i++) {")
                    .append("\n\t\thtmlStr += \"\\n\\t<li onclick=\\\"\" + buttonJson[i].buttonEvent + \"()\\\">\";")
                    .append("\n\t\thtmlStr += buttonJson[i].buttonName + \"</li>\";")
                    .append("\n\t}")
                    .append("\n\thtmlStr += \"</ul>\";")
                    .append("\n\t//渲染")
                    .append("\n\t$(\"#")
                    .append(entity.getDataAreaId())
                    .append(" .visual_button\").html(htmlStr);")
                    .append("\n});");
            return builder.toString();
        }catch (Exception e){
            log.error("生成对应的js代码异常：{}", e.getMessage());
        }
        return builder.toString();
    }


}