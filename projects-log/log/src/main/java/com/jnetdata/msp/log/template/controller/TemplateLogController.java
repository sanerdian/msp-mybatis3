package com.jnetdata.msp.log.template.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.util.IpUtil;
import com.jnetdata.msp.log.template.model.TemplateLog;
import com.jnetdata.msp.log.template.service.TemplateLogService;
import com.jnetdata.msp.log.template.vo.TemplateLogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by TF on 2019/3/13.
 */

@Controller
@RequestMapping("/log/template")
@ApiModel(value = "TemplateLogController", description = "模板管理日志")
@Api(tags = "模板管理日志(TemplateLogController)")
public class TemplateLogController extends BaseController<Long,TemplateLog> {
    @Autowired
    private TemplateLogService service;


    @ApiOperation(value = "添加", notes="添加sql日志信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody TemplateLog entity, HttpServletRequest request) {
        entity.setAddress(IpUtil.getRequestIp(request));
        return doAdd(getService(), entity);
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的sql日志信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("sql日志id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除sql日志信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") @ApiParam("多个id用逗号隔开")String ids) {
        return doDeleteBatchIds(getService(),ids);
    }

    @ApiOperation(value = "修改", notes="修改指定id对应的sql日志信息")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("sql日志id") @PathVariable("id") Long id,
                                       @RequestBody TemplateLog entity) {
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "根据id查询", notes="查看指定sql日志id对应的sql日志信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<TemplateLog> getById(@ApiParam("sql日志id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @ApiOperation(value = "根据实体属性查询", notes="根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<TemplateLog>> list(@RequestBody TemplateLogVo vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "批量上传", notes="通过excel批量上传数据")
    @PostMapping("/uploadExcel")
    @ResponseBody
    public JsonResult uploadExcel( MultipartFile file)throws Exception{
        return JsonResult.renderSuccess(null);
    }

    @ApiOperation(value = "下载数据", notes="根据查询条件下载excel数据")
    @PostMapping("/downloadExcel")
    @ResponseBody
    public JsonResult downloadExcel() {
        return JsonResult.renderSuccess(null);

    }

    private TemplateLogService getService() {
        return service;
    }

}
