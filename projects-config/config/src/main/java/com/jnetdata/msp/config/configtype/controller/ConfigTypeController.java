package com.jnetdata.msp.config.configtype.controller;

import com.jnetdata.msp.config.configtype.model.ConfigType;
import com.jnetdata.msp.config.configtype.service.ConfigTypeService;
import com.jnetdata.msp.config.configtype.vo.ConfigTypeVo;
import com.jnetdata.msp.config.theclient.ContentLogClient;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.core.model.util.SessionUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import java.util.Date;

/**
 * Created by TF on 2019/3/26.
 */

@Controller
@RequestMapping("/config/configType")
@ApiModel(value = "ConfigTypeController", description = "系统配置类型")
@Api(tags = "系统配置类型(ConfigTypeController)")
public class ConfigTypeController extends BaseController<Long,ConfigType>{
    private static final String BASE_URL = "/config/configType";

    @Autowired
    private ConfigTypeService service;
    @Autowired
    private ContentLogClient contentLogService;


    @ApiOperation(value = "添加", notes="添加系统配置类型信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody ConfigType entity) {
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("添加配置项");
        contentLog.setContentType("配置项管理");
        contentLog.setContent("添加配置项");
        contentLog.setCDetail("操作成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        return doAdd(getService(), entity);
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的系统配置类型信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("系统配置类型id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除系统配置类型信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") String ids) {
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("删除配置项");
        contentLog.setContentType("配置项管理");
        contentLog.setContent("删除配置项");
        contentLog.setCDetail("操作成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        return doDeleteBatchIds(getService(),ids);
    }


    @ApiOperation(value = "修改", notes="修改指定id对应的系统配置类型信息")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("id") @PathVariable("id") Long id,
                                       @RequestBody ConfigType entity) {
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("修改配置项");
        contentLog.setContentType("配置项管理");
        contentLog.setContent("修改配置项");
        contentLog.setCDetail("操作成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "根据id查询", notes="查看指定系统配置类型id对应的信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<ConfigType> getById(@ApiParam("系统配置类型信息id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @ApiOperation(value = "根据实体属性查询", notes="根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<ConfigType>> list(@RequestBody ConfigTypeVo vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }



    @ApiOperation(value = "批量上传", notes="通过excel批量上传数据")
    @PostMapping("/uploadExcel")
    @ResponseBody
    public JsonResult uploadExcel( MultipartFile file)throws Exception{
//        getService().uploadData(file);
        return JsonResult.renderSuccess(null);
    }


    @ApiOperation(value = "下载数据", notes="根据查询条件下载excel数据")
    @PostMapping("/downloadExcel")
    @ResponseBody
    public JsonResult downloadExcel() {

        return JsonResult.renderSuccess(null);

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

    private ConfigTypeService getService() {
        return service;
    }



}
