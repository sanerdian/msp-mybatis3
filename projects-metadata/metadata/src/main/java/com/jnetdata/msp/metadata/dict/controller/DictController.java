package com.jnetdata.msp.metadata.dict.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.metadata.dict.model.Dict;
import com.jnetdata.msp.metadata.dict.service.DictService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/metadata/dict")
@ApiModel(value = "字典管理(DictController)", description = "字典管理")
public class DictController extends BaseController<Long, Dict> {

    @Autowired
    private DictService dictService;

    @Value("${spring.datasource.jdbcDialect}")
    private String jdbcDialect;

    @PostMapping(value = "/list")
    @ResponseBody
    @ApiOperation(value = "字典列表(根据json参数获取)")
    public List<Dict> getList(@RequestBody String type) {
        return dictService.list(type);
    }

    @PostMapping(value = "/list1")
    @ResponseBody
    @ApiOperation(value = "获取字典列表")
    public List<Dict> getList1(String type) {
        return dictService.list(type);
    }

    @ApiOperation(value = "添加字典")
    @PostMapping(value = "/add")
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Dict entity) {
        return doAdd(getService(), entity);
    }

    @ApiOperation(value = "删除", notes="删除字典")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("内容操作日志id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除字典")
    @PostMapping(value = "/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestBody String[] ids) {
        //return doDeleteBatchIds(getService(),Arrays.asList(ids));
        return null;
    }

    @ApiOperation(value = "修改", notes="修改字典")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("字典id") @PathVariable("id") Long id,
                                       @RequestBody Dict entity) {
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "根据id查询", notes="查看字典")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Dict> getById(@ApiParam("字典id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    private DictService getService() {
        return dictService;
    }
}
