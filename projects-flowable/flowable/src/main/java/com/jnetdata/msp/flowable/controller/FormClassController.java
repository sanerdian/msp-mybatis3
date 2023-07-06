package com.jnetdata.msp.flowable.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.flowable.enums.Logic;
import com.jnetdata.msp.flowable.model.FormClass;
import com.jnetdata.msp.flowable.service.FormClassService;
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
@RequestMapping("/flowable/formclass")
@Api(tags = "表单分类(FormClassController)")
public class FormClassController extends BaseController<Integer, FormClass> {

    @Autowired
    private FormClassService formClassService;

    @PostMapping
    @ApiOperation(value = "新增")
    public JsonResult<EntityId<Integer>> add(@RequestBody FormClass entity) {
        Date now = new Date();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        entity.setOrderNumber(formClassService.getOrderNumber());
        return doAdd(formClassService, entity);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查看")
    public JsonResult<FormClass> getById(@PathVariable("id") Integer id) {
        return renderSuccess(formClassService.getById(id));
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public JsonResult<Void> deleteById(@PathVariable("id") Integer id) {
        FormClass entity = new FormClass();
        entity.setId(id);
        entity.setUpdateTime(new Date());
        entity.setDeleteFlag(Logic.YES.getCode());
        formClassService.updateById(entity);
        return renderSuccess();
    }

    @DeleteMapping("/batch/{ids}")
    @ApiOperation(value = "批量删除")
    public JsonResult<Void> deleteBatchIdsreal(@RequestBody List<Integer> idList) {
        List<FormClass> entityList = new ArrayList<>();
        Date now = new Date();
        for(Integer id: idList){
            FormClass entity = new FormClass();
            entity.setId(id);
            entity.setUpdateTime(now);
            entity.setDeleteFlag(Logic.YES.getCode());
            entityList.add(entity);
        }
        formClassService.updateBatchById(entityList);
        return JsonResult.success();
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "更新")
    public JsonResult<Void> updateById(@PathVariable("id") Integer id, @RequestBody FormClass entity) {
        return doUpdateById(formClassService, id, entity);
    }

    @PostMapping(value = "/listing")
    @ApiOperation(value = "查询列表")
    public JsonResult<Pager<FormClass>> listing(@RequestBody BaseVo<FormClass> vo) {
        vo.getEntity().setDeleteFlag(Logic.NO.getCode());
        JsonResult<Pager<FormClass>> jsonResult = doList(formClassService, vo.getPager(), vo.getEntity());
        formClassService.setProcessList(jsonResult.getObj().getRecords());
        return jsonResult;
    }

    @PostMapping("/moveUp/{id}")
    @ApiOperation(value = "上移")
    public JsonResult<Void> moveUp(@PathVariable("id") Integer id){
        return formClassService.moveUp(id);
    }


    @PostMapping("/moveDown/{id}")
    @ApiOperation(value = "下移")
    public JsonResult<Void> moveDown(@PathVariable("id") Integer id){
        return formClassService.moveDown(id);
    }


}
