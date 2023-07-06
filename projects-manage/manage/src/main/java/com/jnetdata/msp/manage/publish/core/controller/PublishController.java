package com.jnetdata.msp.manage.publish.core.controller;

import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.manage.column.service.ProgramaService;
import com.jnetdata.msp.manage.publish.core.common.groups.ChannelGroup;
import com.jnetdata.msp.manage.publish.core.common.groups.DocumentGroup;
import com.jnetdata.msp.manage.publish.core.common.groups.MetadataGroup;
import com.jnetdata.msp.manage.publish.core.common.groups.WebsiteGroup;
import com.jnetdata.msp.manage.publish.core.common.utils.ResponseDataUtil;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishVO;
import com.jnetdata.msp.manage.publish.core.service.PublishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/8/7 18:29
 */
@RestController
@RequestMapping("/manage/publish")
@Api(tags = "发布")
public class PublishController {

    @Resource
    private PublishService publishService;
    @Resource
    private ProgramaService programaService;

    @ApiOperation(value = "文档发布", notes = "文档发布")
    @PostMapping("/document")
    public JsonResult<?> documentPublish(
            @Validated({DocumentGroup.class}) @RequestBody PublishVO publishVO
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(ResponseDataUtil.buildBindingResult(bindingResult.getAllErrors()));
        } else {
            return publishService.documentPublish(publishVO);
        }
    }

    @ApiOperation(value = "元数据文档发布", notes = "元数据文档发布")
    @PostMapping("/metadata")
    public JsonResult<?> metadataPublish(
            @Validated({MetadataGroup.class}) @RequestBody PublishVO publishVO
            , BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return JsonResult.fail(ResponseDataUtil.buildBindingResult(bindingResult.getAllErrors()));
//        } else {
//            return publishService.metadataPublish(publishVO);
//        }

        Programa column = programaService.getById(publishVO.getChnlId());
        publishVO.setSiteId(column.getSiteId());
        publishVO.setService("all");
        return publishService.channelPublish(publishVO);
    }

    @ApiOperation(value = "栏目发布", notes = "栏目发布")
    @PostMapping("/channel")
    public JsonResult<?> channelPublish(
            @Validated({ChannelGroup.class}) @RequestBody PublishVO publishVO
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(ResponseDataUtil.buildBindingResult(bindingResult.getAllErrors()));
        } else {
            return publishService.channelPublish(publishVO);
        }
    }

    @ApiOperation(value = "站点发布", notes = "站点发布")
    @PostMapping("/website")
    public JsonResult<?> websitePublish(
            @Validated({WebsiteGroup.class}) @RequestBody PublishVO publishVO
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(ResponseDataUtil.buildBindingResult(bindingResult.getAllErrors()));
        } else {
            return publishService.websitePublish(publishVO);
        }
    }
}
