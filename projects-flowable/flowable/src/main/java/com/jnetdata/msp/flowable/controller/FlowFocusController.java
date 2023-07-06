package com.jnetdata.msp.flowable.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.flowable.mapper.FlowFocusMapper;
import com.jnetdata.msp.flowable.model.FlowFocus;
import com.jnetdata.msp.flowable.model.FlowMetadata;
import com.jnetdata.msp.flowable.model.MetaPub;
import com.jnetdata.msp.flowable.service.FlowFocusService;
import com.jnetdata.msp.flowable.service.FlowMetadataService;
import com.jnetdata.msp.flowable.vo.WorkVo;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/flowable/flowfocus")
@Api(tags = "流程关注(FlowFocusController)")
public class FlowFocusController {

    @Autowired
    private FlowFocusService flowFocusService;

    @PostMapping("/myFocus")
    @ApiOperation(value = "我的关注")
    public JsonResult<Pager<WorkVo>> myFocus(@RequestBody BaseVo<WorkVo> vo) {
        return flowFocusService.myFocus(vo);
    }

    @PostMapping("/addFocus")
    @ApiOperation(value = "关注流程")
    public JsonResult<String> addFocus(@RequestBody FlowFocus entity) {
        return flowFocusService.addFocus(entity);
    }

    @PostMapping("/removeFocus")
    @ApiOperation(value = "取消关注")
    public JsonResult<String> removeFocus(@RequestBody FlowFocus entity) {
        return flowFocusService.removeFocus(entity);
    }
}
