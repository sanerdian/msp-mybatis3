package com.jnetdata.msp.visual.modulenavigation.service.impl;

import com.jnetdata.msp.base.vo.EntityIdVo;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.visual.enums.ModuleType;
import com.jnetdata.msp.visual.modulenavigation.mapper.ModuleNavigationMapper;
import com.jnetdata.msp.visual.modulenavigation.model.ModuleNavigation;
import com.jnetdata.msp.visual.modulenavigation.service.ModuleNavigationService;
import com.jnetdata.msp.visual.relationmoduletemplate.service.RelationModuleTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class ModuleNavigationServiceImpl extends BaseServiceImpl<ModuleNavigationMapper, ModuleNavigation> implements ModuleNavigationService {

    @Autowired
    private RelationModuleTemplateService relationModuleTemplateService;

    @Override
    public PropertyWrapper<ModuleNavigation> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .getWrapper();
    }

    /**
     * 新增导航组件
     */
    @Override
    public JsonResult<EntityId<Long>> insertEntity(ModuleNavigation entity){
        try {
            if(ObjectUtils.isEmpty(entity.getVisualTemplateId())){
                return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "可视化模板id（visualTemplateId）不能为空");
            }
            entity.setCreateTime(new Date());
            this.insert(entity);
            relationModuleTemplateService.saveRelation(entity.getId(), ModuleType.NAVIGATION.value(), entity.getVisualTemplateId());
            return JsonResult.success(new EntityIdVo(entity.getId()));
        }catch (Exception e){
            log.error("新增导航组件异常：{}", e.getMessage());
            return JsonResult.fail("新增失败");
        }
    }

    /**
     * 删除导航组件
     */
    @Override
    public JsonResult<Void> deleteEntity(Long id){
        try {
            this.deleteById(id);
            relationModuleTemplateService.deleteRelation(id, ModuleType.NAVIGATION.value());
            return JsonResult.success();
        }catch (Exception e){
            log.error("删除导航组件异常：{}", e.getMessage());
            return JsonResult.fail("删除失败");
        }
    }
}
