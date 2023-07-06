package com.jnetdata.msp.visual.module_type.controller;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.thenicesys.web.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.thenicesys.data.api.Pager;
import java.util.List;
import java.util.ArrayList;
import org.thenicesys.data.api.EntityId;
import com.jnetdata.msp.vo.BaseVo;

import org.springframework.stereotype.Controller;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.visual.module_type.service.VisualModuleTypeService;
import com.jnetdata.msp.visual.module_type.model.VisualModuleType;

/**
 * <p>
 * 组件类型 前端控制器
 * </p>
 *
 * @author zyj
 * @since 2022-08-08
 */
@Controller
@RequestMapping("/visual/moduletype")
public class VisualModuleTypeController extends BaseController<Long,VisualModuleType> {

    @Autowired
    VisualModuleTypeService visualModuleTypeService;

    /**
     * 执行新增操作
     * @param entity
     * @return
     */
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "添加", notes="根据提供的实体属性添加")
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody VisualModuleType entity) {
        getService().add(entity);
        return renderSuccess();
    }


    @ApiOperation(value = "批量添加", notes = "批量添加")
    @PostMapping("/batch")
    @ResponseBody
    public JsonResult addbatch( @RequestBody List<VisualModuleType> list) {
        getService().addBatch(list);
        return renderSuccess();
    }

    @ApiOperation(value = "根据id查询", notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<VisualModuleType> getById(@PathVariable("id") Long id) {
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
        VisualModuleType entity = new VisualModuleType();
        entity.setId(id);
        entity.setDocstatus(1);
        getService().updateById(entity);
        return renderSuccess();
    }

    @PostMapping("/delete/batch")
    @ResponseBody
    @ApiOperation(value="批量假删")
    public JsonResult<Void> deleteBatchIdsGet(@RequestBody String[] ids) {
        List<VisualModuleType> toDel = new ArrayList<>();
        for (String s : ids) {
            VisualModuleType entity = new VisualModuleType();
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
    @ApiOperation(value = "删除", notes= "根据指定id删除")
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
    @ApiOperation(value = "批量删除", notes="根据指定的多个id进行批量删除")
    public JsonResult<Void> deleteBatchIdsreal(@PathVariable("ids") @ApiParam("多个id用逗号隔开")String ids) {
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
    public JsonResult<Void> updateByIdPost(@PathVariable("id") Long id, @RequestBody VisualModuleType entity) {
        return doUpdateById(getService(), id, entity);
    }

    /**
     * 属性选择性更新操作
     * @return
     */
    @ApiOperation(value = "批量选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PostMapping("/batchUpdate")
    @ResponseBody
    public JsonResult<Void> batchUpdatePost(@RequestBody List<VisualModuleType> list) {
        getService().updateBatchById(list);
        return renderSuccess();
    }

    @ApiOperation(value = "类型列表", notes="类型列表")
    @PostMapping(value = "/typeList")
    @ResponseBody
    public JsonResult<List<VisualModuleType>> typeList() {
        VisualModuleType visualModuleType = new VisualModuleType();
        visualModuleType.setParentId(0L);
        visualModuleType.setDocstatus(0);
        List<VisualModuleType> list = getService().list(createCondition(visualModuleType));
        return renderSuccess(list);
    }

    @ApiOperation(value = "类型分类列表", notes="类型分类列表")
    @PostMapping(value = "/typeTypeList/{id}")
    @ResponseBody
    public JsonResult<List<VisualModuleType>> typeTypeList(@PathVariable Long id) {
        VisualModuleType visualModuleType = new VisualModuleType();
        visualModuleType.setParentId(id);
        visualModuleType.setDocstatus(0);
        List<VisualModuleType> list = getService().list(createCondition(visualModuleType));
        return renderSuccess(list);
    }


    @ApiOperation(value = "查询列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<Pager<VisualModuleType>> list(@RequestBody BaseVo<VisualModuleType> vo) {
        if(vo.getEntity() == null) vo.setEntity(new VisualModuleType());
        vo.getEntity().setDocstatus(0);
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "查询已删除列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/delList")
    @ResponseBody
    public JsonResult<Pager<VisualModuleType>> delListing(@RequestBody BaseVo<VisualModuleType> vo) {
        if(vo.getEntity() == null) vo.setEntity(new VisualModuleType());
                vo.getEntity().setDocstatus(1);
                return doList(getService(), vo.getPager(), vo.getEntity());
    }


    private VisualModuleTypeService getService() {
        return this.visualModuleTypeService;
    }

}

