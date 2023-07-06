package com.jnetdata.msp.flowable.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.flowable.enums.Logic;
import com.jnetdata.msp.flowable.model.ProcessClass;
import com.jnetdata.msp.flowable.service.ProcessClassService;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/flowable/processclass")
@Api(tags = "流程分类(ProcessClassController)")
public class ProcessClassController extends BaseController<Integer, ProcessClass> {

    @Autowired
    private ProcessClassService processClassService;

    @PostMapping
    @ApiOperation(value = "新增")
    public JsonResult<EntityId<Integer>> add(@RequestBody ProcessClass entity) {
        Date now = new Date();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        entity.setOrderNumber(processClassService.getOrderNumber());
        return doAdd(processClassService, entity);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查看")
    public JsonResult<ProcessClass> getById(@PathVariable("id") Integer id) {
        return renderSuccess(processClassService.getById(id));
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public JsonResult<Void> deleteById(@PathVariable("id") Integer id) {
        ProcessClass entity = new ProcessClass();
        entity.setId(id);
        entity.setUpdateTime(new Date());
        entity.setDeleteFlag(Logic.YES.getCode());
        processClassService.updateById(entity);
        return renderSuccess();
    }

    @DeleteMapping("/batch/{ids}")
    @ApiOperation(value = "批量删除")
    public JsonResult<Void> deleteBatchIdsreal(@RequestBody List<Integer> idList) {
        List<ProcessClass> entityList = new ArrayList<>();
        Date now = new Date();
        for(Integer id: idList){
            ProcessClass entity = new ProcessClass();
            entity.setId(id);
            entity.setUpdateTime(now);
            entity.setDeleteFlag(Logic.YES.getCode());
            entityList.add(entity);
        }
        processClassService.updateBatchById(entityList);
        return JsonResult.success();
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "更新")
    public JsonResult<Void> updateById(@PathVariable("id") Integer id, @RequestBody ProcessClass entity) {
        return doUpdateById(processClassService, id, entity);
    }

    @PostMapping(value = "/listing")
    @ApiOperation(value = "查询列表")
    public JsonResult<Pager<ProcessClass>> list(@RequestBody BaseVo<ProcessClass> vo) {
        vo.getEntity().setDeleteFlag(Logic.NO.getCode());
        return doList(processClassService, vo.getPager(), vo.getEntity());
    }

    @PostMapping(value = "/tree")
    @ApiOperation(value = "树状结构")
    public JsonResult<Pager<ProcessClass>> tree(@RequestBody BaseVo<ProcessClass> vo) {
        vo.getEntity().setDeleteFlag(Logic.NO.getCode());
        JsonResult<Pager<ProcessClass>> jsonResult = doList(processClassService, vo.getPager(), vo.getEntity());
        processClassService.setProcessList(jsonResult.getObj().getRecords());
        return jsonResult;
    }

    @PostMapping("/moveUp/{id}")
    @ApiOperation(value = "上移")
    public JsonResult<Void> moveUp(@PathVariable("id") Integer id){
        return processClassService.moveUp(id);
    }


    @PostMapping("/moveDown/{id}")
    @ApiOperation(value = "下移")
    public JsonResult<Void> moveDown(@PathVariable("id") Integer id){
        return processClassService.moveDown(id);
    }


}
