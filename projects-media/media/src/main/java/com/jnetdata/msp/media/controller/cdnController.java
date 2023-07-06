package com.jnetdata.msp.media.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.media.service.ExcellentService;
import com.jnetdata.msp.media.vo.*;
import com.jnetdata.msp.tlujy.xinwen_comment.model.XinwenComment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/media/cdn/")
@Api(description = "前台使用cdn的接口")
public class cdnController {

    @Autowired
    private ExcellentService service;
    private ExcellentService getService(){ return service; }


    @ApiOperation(value = "根据站点id获取栏目", notes="根据站点id获取栏目")
    @PostMapping("/getlm/{siteid}")
    @ResponseBody
    public JsonResult<Programa> getlm(@PathVariable("siteid") Long siteid) {
        return JsonResult.success(getService().getlmbySiteid(siteid,""));
    }


    @ApiOperation(value = "app获取新闻", notes="获取新闻")
    @PostMapping("/xinwenlist")
    @ResponseBody
    public JsonResult xinwenlist(@RequestBody xinwenQueryVo vo,HttpServletRequest request) {

        Lz_WorkerVo lz_workerVo = (Lz_WorkerVo) request.getSession().getAttribute("userinfo");

        Page<xinwenVo> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().xinwenlist(page,vo.getEntity(),lz_workerVo));
    }

    @ApiOperation(value = "app获取新闻详情", notes="app获取新闻详情")
    @PostMapping("/getxinwen")
    @ResponseBody
    public JsonResult getxinwen(@RequestBody xinwenVo vo) {
        return JsonResult.success(getService().getxinwen(vo));
    }

    @ApiOperation(value = "app获取评论", notes="获取评论")
    @PostMapping("/commentlist")
    @ResponseBody
    public JsonResult<Pager<CommentVo>> commentlist(@RequestBody CommentQueryVo vo) {

        Page<XinwenComment> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().commentlist(page,vo.getEntity()));
    }

    @ApiOperation(value = "app获取评论数量", notes="获取评论数量")
    @PostMapping("/commentCount")
    @ResponseBody
    public JsonResult pldjCount(@RequestBody XinwenComment vo, HttpServletRequest request) {
        return JsonResult.success(getService().commentCount(vo));
    }




}
