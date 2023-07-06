package com.jnetdata.msp.visual.controller;

import com.jnetdata.msp.member.menu.model.Menu;
import com.jnetdata.msp.visual.service.VisualMenuService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

@RestController
@RequestMapping("/visual/page")
@ApiModel(value = "VisualMenuController", description = "页面管理")
public class VisualMenuController {

    @Autowired
    private VisualMenuService visualMenuService;


    @ApiOperation(value = "页面左移", notes="页面左移")
    @PostMapping(value = "/pageLeft")
    public JsonResult<String> pageLeft(Long pageId){
        return visualMenuService.pageLeft(pageId);
    }

    @ApiOperation(value = "页面右移", notes="页面右移")
    @PostMapping(value = "/pageRight")
    public JsonResult<String> pageRight(Long pageId){
        return visualMenuService.pageRight(pageId);
    }

    @ApiOperation(value = "页面上移", notes="页面上移")
    @PostMapping(value = "/pageUp")
    public JsonResult<String> pageUp(Long pageId){
        return visualMenuService.pageUp(pageId);
    }

    @ApiOperation(value = "页面下移", notes="页面下移")
    @PostMapping(value = "/pageDown")
    public JsonResult<String> pageDown(Long pageId){
        return visualMenuService.pageDown(pageId);
    }


    @ApiOperation(value = "页面复制",notes = "页面复制")
    @PostMapping(value = "/pageCopy")
    public JsonResult<String> pageCopy(Long pageId){
        return visualMenuService.pageCopy(pageId);
    }
}
