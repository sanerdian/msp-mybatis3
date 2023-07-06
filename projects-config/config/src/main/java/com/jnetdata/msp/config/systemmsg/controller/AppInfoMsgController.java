package com.jnetdata.msp.config.systemmsg.controller;

import com.jnetdata.msp.config.systemmsg.model.AppInfoMsg;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.log.content.service.ContentLogService;
import com.jnetdata.msp.config.systemmsg.service.AppInfoMsgService;
import com.jnetdata.msp.core.model.util.SessionUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019/9/12.
 */
@Controller
@RequestMapping("manage/systemmsg")
@ApiModel(value = "AppInfoMsgController",description = "应用信息")
@Api(tags = "应用信息(AppInfoMsgController)")
public class AppInfoMsgController extends BaseController<Long, AppInfoMsg>{

    @Autowired
    private ContentLogService contentLogService;
    @Autowired
    private AppInfoMsgService service;

    private AppInfoMsgService getService(){
        return service;
    }

    /**
     * 添加应用信息
     * @param entity
     * @param request
     * @return
     */
    @ApiOperation(value = "添加", notes="添加应用信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody AppInfoMsg entity, HttpServletRequest request) {
        contentLogService.addLog("添加信息","系统信息","系统信息",true);
        return doAdd(getService(), entity);
    }

    /**
     * 修改指定id对应的应用信息
     * @param id
     * @param entity
     * @return
     */
    @ApiOperation(value = "修改", notes="修改指定id对应的应用信息")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("应用信息id") @PathVariable("id") Long id, @RequestBody AppInfoMsg entity) {
        contentLogService.addLog("修改应用信息","系统信息","系统信息",true);
        return doUpdateById(getService(), id, entity);
    }

    /**
     * 查看应用信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询", notes="查看应用信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<AppInfoMsg> getById(@ApiParam("应用信息id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    /**
     * 查看应用信息
     * @return
     */
    @ApiOperation(value = "查看应用信息", notes="查看应用信息")
    @GetMapping(value = "/getinfo")
    @ResponseBody
    public JsonResult<AppInfoMsg> getfirst() {
        List<AppInfoMsg> list = getService().list();
        return renderSuccess(list.get(0));
    }
}
