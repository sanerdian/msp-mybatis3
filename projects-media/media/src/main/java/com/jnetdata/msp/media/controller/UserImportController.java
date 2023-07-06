package com.jnetdata.msp.media.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.service.UserImportService;
import com.jnetdata.msp.media.vo.Lz_WorkerVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/jwt/sso")
@Api(description = "统计分析模块")
public class UserImportController extends BaseController<Long, JobApi> {

    @Autowired
    private UserImportService service;
    private UserImportService getService(){ return service; }

    private SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

    @ApiOperation(value = "获取session信息", notes="获取session信息")
    @GetMapping("/loginExternal")
    @ResponseBody
    public JsonResult loginExternal(@RequestParam String externalid,HttpServletRequest request, HttpServletResponse response) throws IOException {

        Lz_WorkerVo vo=service.getInfobyExternalid(externalid);

        request.getSession().removeAttribute("userinfo");
        request.getSession().setAttribute("userinfo",vo);
        return JsonResult.success();
    }


    @RequestMapping(value = "/login2")
    public JsonResult login2(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Lz_WorkerVo vo=service.getInfobyExternalid("6728328613069706166");

        System.out.println(vo.toString());

        request.getSession().setAttribute("userinfo",vo);

        return JsonResult.success(vo);
    }

    @RequestMapping(value = "/login3")
    public JsonResult login3(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Lz_WorkerVo vo=service.getInfobyUserid(SessionUser.getCurrentUser().getId());

        request.getSession().setAttribute("userinfo",vo);

        return JsonResult.success(vo);
    }



    @ApiOperation(value = "获取session信息", notes="获取session信息")
    @GetMapping("/getUserSession")
    @ResponseBody
    public JsonResult getSession(HttpServletRequest request) {

        Object obj=request.getSession().getAttribute("userinfo");

        Lz_WorkerVo user=null;

        if(obj!=null){
            user= (Lz_WorkerVo)obj ;
        }

        return JsonResult.success(user);
    }


    @ApiOperation(value = "根据用户id获取评论数量", notes="根据用户id获取评论数量")
    @GetMapping("/getCommentCount/{userid}")
    @ResponseBody
    public JsonResult getCommentCount(@PathVariable("userid")Long userid) {
        return JsonResult.success(service.getCommentCount(userid));
    }

    @ApiOperation(value = "根据用户id获取点赞数量", notes="根据用户id获取点赞数量")
    @GetMapping("/getEvaluateCount/{userid}")
    @ResponseBody
    public JsonResult getEvaluateCount(@PathVariable("userid")Long userid) {
        return JsonResult.success(service.getEvaluateCount(userid));
    }

    @ApiOperation(value = "根据用户id获取收藏数量", notes="根据用户id获取收藏数量")
    @GetMapping("/getCollectCount/{userid}")
    @ResponseBody
    public JsonResult getCollectCount(@PathVariable("userid")Long userid) {
        return JsonResult.success(service.getCollectCount(userid));
    }


    @ApiOperation(value = "根据用户id获取收藏新闻", notes="根据用户id获取收藏新闻")
    @GetMapping("/listCollectXinwen/{userid}")
    @ResponseBody
    public JsonResult listCollectXinwen(@PathVariable("userid")Long userid) {
        return JsonResult.success(service.listCollectXinwen(userid));
    }


    @ApiOperation(value = "根据用户id获取评论新闻", notes="根据用户id获取评论新闻")
    @GetMapping("/listCommentXinwen/{userid}")
    @ResponseBody
    public JsonResult listCommentXinwen(@PathVariable("userid")Long userid) {
        return JsonResult.success(service.listCommentXinwen(userid));
    }

    @ApiOperation(value = "根据用户id获取点赞新闻", notes="根据用户id获取点赞新闻")
    @GetMapping("/listEvaluateXinwen/{userid}")
    @ResponseBody
    public JsonResult listEvaluateXinwen(@PathVariable("userid")Long userid) {
        return JsonResult.success(service.listEvaluateXinwen(userid));
    }

    @ApiOperation(value = "根据用户id获取评论新闻详情", notes="根据用户id获取评论新闻详情")
    @GetMapping("/getCommentXinwen/{id}")
    @ResponseBody
    public JsonResult getCommentXinwen(@PathVariable("id")Long id) {
        return JsonResult.success(service.getCommentXinwen(id));
    }

}
