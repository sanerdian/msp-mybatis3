package com.jnetdata.msp.cs1.test_cy.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.cs1.test_cy.TestCy;
import com.jnetdata.msp.cs1.test_cy.service.TestCyService;
import com.jnetdata.msp.util.Test;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.IQueryService;
import org.thenicesys.data.api.IQueryWrapper;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import java.util.List;

/**
 * @ClassName TestCyController
 * @Description 陈闫测试控制器
 * @Author chenyan
 * @Date 2023/6/3 13:02
 */
@RestController
@RequestMapping("/cs1/testCy")
public class TestCyController extends BaseController<Long, TestCy> {

    final private TestCyService testCyService;

    @Autowired
    public TestCyController(TestCyService testCyService){
        this.testCyService=testCyService;
    }

    /**
     * 执行新增操作
     * @param entity
     * @return
     */
    @PostMapping
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody TestCy entity){
        JsonResult<EntityId<Long>> result = doAdd(getService(), entity);
        return result;
    }

    private TestCyService getService(){
        return this.testCyService;
    }


    @ApiOperation(value = "批量添加",notes = "批量添加")
    @PostMapping("/batch")
    public JsonResult addBatch(@RequestBody List<TestCy> list){
        getService().insertBatch(list);
        return renderSuccess();
    }

    @ApiOperation(value = "根据id查询",notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    public JsonResult<TestCy> getById(@PathVariable("id") Long id){
        return renderSuccess(getService().getById(id));
    }

    @ApiOperation(value = "查询列表",notes = "根据指定条件进行查询")
    @PostMapping("/listing")
    public JsonResult<Pager<TestCy>> getByMsg(@RequestBody BaseVo<TestCy> vo){
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "删除",notes = "根据id删除")
    @DeleteMapping("/{id}")
    public JsonResult<TestCy> deleteById(@PathVariable("id") Long id){
        getService().deleteById(id);
        return renderSuccess();
    }

    @ApiOperation(value = "批量删除",notes = "根据id批量删除")
    @PostMapping("/{ids}/batch")
    public JsonResult<Void> deleteBatchById(@RequestBody List<Long> ids){
        getService().deleteBatchIds(ids);
        return renderSuccess();
    }

    @ApiOperation(value = "更新",notes = "根据id更新")
    @PostMapping("/update")
    public JsonResult<Void> updateById(@RequestBody TestCy testCy){
        getService().updateById(testCy);
        return renderSuccess();
    }

    @ApiOperation(value = "批量更新",notes = "根据id批量更新操作")
    @PostMapping("/updateBatch")
    public JsonResult<Void> updateBatchById(@RequestBody List<TestCy> list){
        getService().updateBatchById(list);
        return renderSuccess();
    }
}
