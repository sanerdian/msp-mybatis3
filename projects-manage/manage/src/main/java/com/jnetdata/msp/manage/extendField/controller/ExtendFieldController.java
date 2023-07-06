package com.jnetdata.msp.manage.extendField.controller;


import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.manage.extendField.model.ExtendField;
import com.jnetdata.msp.manage.extendField.service.ExtendFieldService;
import com.jnetdata.msp.manage.extendField.vo.ExtendFieldVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

@Controller
@RequestMapping("/manage/extendField")
@ApiModel(value = "扩展字段(ExtendFieldController)", description = "扩展字段")
@Api(tags = "扩展字段(ExtendFieldController)")
public class ExtendFieldController extends BaseController<Long,ExtendField> {
    private static final String BASE_URL = "/manage/extendField";

    @Autowired
    private ExtendFieldService service;


    @ApiOperation(value = "添加", notes="添加扩展字段信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody ExtendField entity) {
        return doAdd(getService(), entity);
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的扩展字段信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("用户id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除扩展字段信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") @ApiParam("多个id用逗号隔开")String ids) {
        return doDeleteBatchIds(getService(),ids);
    }


    @ApiOperation(value = "修改", notes="修改指定id对应的扩展字段信息(只需要填扩展字段中的id)")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("用户id") @PathVariable("id") Long id,
                                       @RequestBody ExtendField entity) {
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "根据id查询", notes="查看指定扩展字段id对应的扩展字段信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<ExtendField> getById(@ApiParam("扩展字段信息id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @ApiOperation(value = "根据实体属性查询", notes="根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<ExtendField>> list(@RequestBody ExtendFieldVo vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }
    /**
     *
     * @return
     */
    @GetMapping("/index")
    public String toList() {
        return pageFilePath("/list");
    }


    private String pageFilePath(String path) {
        return super.webPath(BASE_URL, path);
    }

    private ExtendFieldService getService() {
        return service;
    }




}
