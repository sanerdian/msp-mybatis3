package com.jnetdata.msp.metadata.pic.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.metadata.pic.model.MetadataPic;
import com.jnetdata.msp.metadata.pic.service.MetadataPicService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;
import org.thenicesys.web.vo.PageVo1;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyj
 * @since 2019-10-23
 */
@Controller
@RequestMapping("/metadata/pic")
public class MetadataPicController extends BaseController<Long, MetadataPic> {

    @Autowired
    private MetadataPicService metadataPicService;

    /**
     * 执行新增操作
     * @param entity
     * @return
     */
    @ApiOperation(value = "添加操作", notes="根据提供的实体属性添加")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody MetadataPic entity) {
        return doAdd(getService(), entity);
    }

    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @ApiOperation(value = "删除操作", notes= "根据指定id删除实体对象")
    @DeleteMapping("/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    /**
     * 执行批量删除操作
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量删除操作", notes="根据指定的多个id进行批量删除实体对象")
    @DeleteMapping("/{ids}/batch")
    @ResponseBody
    public JsonResult<Void> deleteBatchIds(@PathVariable("ids") String ids) {
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
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody MetadataPic entity) {
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
    public JsonResult<Void> doUpdateAllColumnById(@PathVariable("id") Long id, @RequestBody MetadataPic entity) {
        return doUpdateAllColumnById(getService(), id, entity);
    }


    @ApiOperation(value = "根据id查询", notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<MetadataPic> getById(@PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @ApiOperation(value = "根据实体属性查询", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<MetadataPic>> list(@ModelAttribute MetadataPic entity,@RequestBody PageVo1 pager) {
        return doList(getService(), pager, entity);
    }


    private MetadataPicService getService() {
        return this.metadataPicService;
    }

}

