package com.jnetdata.msp.visual.template_type.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.visual.template_type.model.VisualTemplateType;
import com.jnetdata.msp.visual.template_type.service.VisualTemplateTypeService;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 组件类型 前端控制器
 * </p>
 *
 * @author zyj
 * @since 2022-08-08
 */
@Controller
@RequestMapping("/visual/templatetype")
public class VisualTemplateTypeController extends BaseController<Long,VisualTemplateType> {

    @Autowired
    VisualTemplateTypeService visualTemplateTypeService;

    /**
     * 执行新增操作
     * @param entity
     * @return
     */
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "添加", notes="根据提供的实体属性添加")
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody VisualTemplateType entity) {
        getService().insert(entity);
        return renderSuccess();
    }


    @ApiOperation(value = "批量添加", notes = "批量添加")
    @PostMapping("/batch")
    @ResponseBody
    public JsonResult addbatch( @RequestBody List<VisualTemplateType> list) {
        getService().insertBatch(list);
        return renderSuccess();
    }

    @ApiOperation(value = "根据id查询", notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<VisualTemplateType> getById(@PathVariable("id") Long id) {
        return renderSuccess(getService().getById(id));
    }

    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    @ApiOperation(value="假删")
    public JsonResult<Void> deleteByIdGet(@PathVariable("id") Long id) {
        VisualTemplateType entity = new VisualTemplateType();
        entity.setId(id);
        entity.setDocstatus(1);
        getService().updateById(entity);
        return renderSuccess();
    }

    @PostMapping("/delete/batch")
    @ResponseBody
    @ApiOperation(value="批量假删")
    public JsonResult<Void> deleteBatchIdsGet(@RequestBody String[] ids) {
        List<VisualTemplateType> toDel = new ArrayList<>();
        for (String s : ids) {
            VisualTemplateType entity = new VisualTemplateType();
            entity.setId(Long.valueOf(s));
            entity.setDocstatus(1);
            toDel.add(entity);
        }
        getService().updateBatchById(toDel);
        return renderSuccess();
    }

    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @GetMapping("/real/{id}")
    @ResponseBody
    @ApiOperation(value = "删除", notes= "根据指定id删除")
    public JsonResult<Void> deleteByIdrealGet(@PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }


    /**
     * 执行批量删除操作
     * @param ids
     * @return
     */
    @GetMapping("/real/{ids}/batch")
    @ResponseBody
    @ApiOperation(value = "批量删除", notes="根据指定的多个id进行批量删除")
    public JsonResult<Void> deleteBatchIdsrealGet(@PathVariable("ids") String ids) {
        return doDeleteBatchIds(getService(), ids);
    }

    /**
     * 执行批量删除操作
     * @param ids
     * @return
     */
    @DeleteMapping("/real/{ids}/batch")
    @ResponseBody
    @ApiOperation(value = "批量删", notes="根据指定的多个id进行批量删除")
    public JsonResult<Void> deleteBatchIdsreal(@PathVariable("ids") String ids) {
        return doDeleteBatchIds(getService(), ids);
    }

    /**
     * 属性选择性更新操作
     * @param id
     * @param entity
     * @return
     */
    @ApiOperation(value = "选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PostMapping("/{id}")
    @ResponseBody
    public JsonResult<Void> updateByIdPost(@PathVariable("id") Long id, @RequestBody VisualTemplateType entity) {
        return doUpdateById(getService(), id, entity);
    }

    /**
     * 属性选择性更新操作
     * @return
     */
    @ApiOperation(value = "批量选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PostMapping("/batchUpdate")
    @ResponseBody
    public JsonResult<Void> batchUpdatePost(@RequestBody List<VisualTemplateType> list) {
        getService().updateBatchById(list);
        return renderSuccess();
    }
/*
* 获取类型列表
* @author 王树彬
* @time 2022/8/16
* */
    @ApiOperation(value = "类型列表", notes="类型列表")
    @PostMapping(value = "/typeList")
    @ResponseBody
    public JsonResult<List<VisualTemplateType>> typeList() {
        VisualTemplateType visualTemplateType = new VisualTemplateType();
        visualTemplateType.setDocstatus(0);
        List<VisualTemplateType> list = getService().list(createCondition(visualTemplateType));
        return renderSuccess(list);
    }


    @ApiOperation(value = "查询列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<Pager<VisualTemplateType>> list(@RequestBody BaseVo<VisualTemplateType> vo) {
        if(vo.getEntity() == null) vo.setEntity(new VisualTemplateType());
        vo.getEntity().setDocstatus(0);
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "查询已删除列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/delList")
    @ResponseBody
    public JsonResult<Pager<VisualTemplateType>> delListing(@RequestBody BaseVo<VisualTemplateType> vo) {
        if(vo.getEntity() == null) vo.setEntity(new VisualTemplateType());
                vo.getEntity().setDocstatus(1);
                return doList(getService(), vo.getPager(), vo.getEntity());
    }


    private VisualTemplateTypeService getService() {
        return this.visualTemplateTypeService;
    }

}

