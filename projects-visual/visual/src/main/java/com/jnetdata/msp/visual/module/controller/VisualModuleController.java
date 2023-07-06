package com.jnetdata.msp.visual.module.controller;

import com.jnetdata.msp.manage.tplresource.model.JTemplateResource;
import com.jnetdata.msp.manage.tplresource.service.JTemplateResourceService;
import com.jnetdata.msp.visual.module.model.VisualModule;
import com.jnetdata.msp.visual.module.service.VisualModuleService;
import com.jnetdata.msp.visual.module_type.service.VisualModuleTypeService;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.thenicesys.data.api.Pager;
import org.springframework.stereotype.Controller;
import com.jnetdata.msp.core.controller.BaseController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyj
 * @since 2020-03-23
 */
@Controller
@RequestMapping("/visual/module")
public class VisualModuleController extends BaseController<Long, VisualModule> {

    final private VisualModuleService visualModuleService;

    @Autowired
    JTemplateResourceService jTemplateResourceService;

    @Autowired
    private VisualModuleTypeService visualModuleTypeService;

    @Autowired
    public VisualModuleController(VisualModuleService visualModuleService) {
        this.visualModuleService = visualModuleService;
    }
    

    /**
     * 执行新增操作
     * @param entity
     * @return
     */
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "添加", notes="根据提供的实体属性添加")
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody VisualModule entity) {
        return doAdd(getService(), entity);
    }

    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "删除", notes= "根据指定id删除")
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    /**
     * 执行批量删除操作
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}/batch")
    @ResponseBody
    @ApiOperation(value = "批量删除", notes="根据指定的多个id进行批量删除")
    public JsonResult<Void> deleteBatchIds(@PathVariable("ids") @ApiParam("多个id用逗号隔开")String ids) {
        return doDeleteBatchIds(getService(), ids);
    }

    /**
     * 属性选择性更新操作
     * @param id
     * @param entity
     * @return
     */
    @ApiOperation(value = "选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PutMapping("/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody VisualModule entity) {
        return doUpdateById(getService(), id, entity);
    }

    /**
     * 全部属性更新操作
     * @param id
     * @param entity
     * @return
     */
    @ApiOperation(value = "执行修改操作", notes="注意必须提供实体的所有属性否则没有提供的属性将被设置为null")
    @PutMapping("/{id}/allColumn")
    @ResponseBody
    public JsonResult<Void> doUpdateAllColumnById(@PathVariable("id") Long id, @RequestBody VisualModule entity) {
        return doUpdateAllColumnById(getService(), id, entity);
    }


    @ApiOperation(value = "根据id查询", notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<VisualModule> getById(@PathVariable("id") Long id) {
        JsonResult<VisualModule> result = doGetById(getService(), id);
        try{
            //组件类型code赋值
            Map<Long, String> map = visualModuleTypeService.getTypeMap();
            result.getObj().setTypeCode(map.get(result.getObj().getVcType()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    
    @ApiOperation(value = "根据实体属性查询", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<VisualModule>> list(@RequestBody BaseVo<VisualModule> vo) {
        JsonResult<Pager<VisualModule>> result = doList(getService(), vo.getPager(), vo.getEntity());
        try {
            Map<Long, String> map = visualModuleTypeService.getTypeMap();
            //组件类型code赋值
            for(VisualModule visualModule: result.getObj().getRecords()){
                visualModule.setTypeCode(map.get(visualModule.getVcType()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @ApiOperation(value = "查询", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/list2")
    @ResponseBody
    public JsonResult<Pager<VisualModule>> list2(@RequestBody BaseVo<VisualModule> vo) {
        JsonResult<Pager<VisualModule>> pagerJsonResult = doList(getService(), vo.getPager(), vo.getEntity());
        List<VisualModule> records = pagerJsonResult.getObj().getRecords();
        List<Long> ids = records.stream().map(m -> m.getId()).collect(Collectors.toList());
        List<JTemplateResource> templateResourceList = jTemplateResourceService.list(new PropertyWrapper<>(JTemplateResource.class).in("templateId", ids).in("type", Arrays.asList("css","js")));
        Map<Long, List<JTemplateResource>> collect = templateResourceList.stream().collect(Collectors.groupingBy(m -> m.getTemplateId()));
        for (VisualModule record : records) {
            if(collect.containsKey(record.getId())){
                String html = "";
                for (JTemplateResource jTemplateResource : collect.get(record.getId())) {
                    if(jTemplateResource.getType().equals("css")){
                        html+="<style>"+jTemplateResource.getContent()+"</style>";
                    }else {
                        html+="<script>"+jTemplateResource.getContent()+"</script>";
                    }
                }
                html += record.getHtmlCode();
                record.setHtmlCode1(html);
            }
        }
        return pagerJsonResult;
    }

    @ApiOperation(value = "查询(获取树)", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/tree")
    @ResponseBody
    public JsonResult<List<VisualModule>> tree(@RequestBody VisualModule entity) {
        return renderSuccess(getService().getTree(createCondition(entity)));
    }


    private VisualModuleService getService() {
        return this.visualModuleService;
    }

}

