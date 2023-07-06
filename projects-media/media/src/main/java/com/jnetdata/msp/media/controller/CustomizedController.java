package com.jnetdata.msp.media.controller;

import com.jnetdata.msp.media.service.CustomizedService;
import com.jnetdata.msp.media.service.impl.UserComponent;
import com.jnetdata.msp.media.vo.XinwenDetailVo;
import com.jnetdata.msp.media.vo.XinwenSummaryVo;
import com.jnetdata.msp.media.vo.commentManage.CommentQueryVo;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.tlujy.xinwen_comment.model.XinwenComment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import java.util.List;

@RestController
@RequestMapping("/media")
@Api(description = "（基本未使用）")
public class CustomizedController {
    @Autowired
    private CustomizedService customizedService;
    @ApiOperation(value = "查询轮播图", notes="查询指定网站的轮播图")
    @GetMapping("/xinwenlunbotu/{siteid}")
    public JsonResult xinwenLunbotu(@PathVariable Long siteid){
        try{
            List<XinwenSummaryVo> xinwenSummaryVos = customizedService.xinwenLunbotu(siteid);
            return JsonResult.success(xinwenSummaryVos);
        }catch (Exception e){
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "查询新闻列表", notes="查询指定栏目下的新闻")
    @GetMapping("/xinwenbyColumn/{columnid}")
    public JsonResult xinwenByColumn(@PathVariable Long columnid){
        try{
            List<XinwenSummaryVo> xinwenSummaryVos = customizedService.xinwenByColumn(columnid);
            return JsonResult.success(xinwenSummaryVos);
        }catch (Exception e){
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "查询新闻详情", notes="查询指定的新闻，包括其的点赞数及评论")
    @GetMapping("/xinwenbyId/{docid}")
    public JsonResult xinwenById(@PathVariable Long docid){
        try{
            XinwenDetailVo vo = customizedService.xinwenById(docid);
            return JsonResult.success(vo);
        }catch (Exception e){
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "添加新闻评价", notes="对该新闻进行评价（点赞/踩），需要传入评价类型type(1=点赞，-1=踩，默认点赞)，每条新闻每人只能评价一次")
    @GetMapping("/xinwenEvaluate/{docid}")
    public JsonResult xinwenEvaluate(@PathVariable Long docid,Integer type){
        if(type==null){
            type=1;
        }
        try{
            Long id = customizedService.addXinwenEvaluate(docid, type);
            return JsonResult.success(id);
        }catch (Exception e){
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "删除新闻评价", notes="取消对该新闻的评价（点赞/踩），该方法只能取消对当前用户对指定新闻的评价")
    @DeleteMapping("/xinwenEvaluate/{docid}")
    public JsonResult xinwenEvaluate(Long docid){
        try{
            customizedService.deleteXinwenEvaluate(docid);
            return JsonResult.success();
        }catch (Exception e){
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "添加新闻评论", notes="以文字形式提出对当前新闻的意见。主要需要2个参数：新闻主键docid、用户评论正文usercomment")
    @PostMapping("/xinwenComment")
    public JsonResult xinwenComment(@RequestBody XinwenComment xinwenComment){
        try{
            Long id = customizedService.addXinwenComment(xinwenComment);
            return JsonResult.success(id);
        }catch (Exception e){
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "删除新闻评论", notes="此处只能删除当前用户自己的评论！同时删除该评论获得评价！")
    @DeleteMapping("/xinwenComment/{commentid}")
    public JsonResult xinwenComment(Long commentid){
        try{
            customizedService.deleteXinwenComment(commentid);
            return JsonResult.success();
        }catch (Exception e){
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "添加评论的评价", notes="对用户的评论内容进行进行评价（点赞/踩），需要传入评价类型type(1=点赞，-1=踩，默认点赞)，每条评论每人只能评价一次")
    @GetMapping("/commentEvaluate/{commentid}")
    public JsonResult commentEvaluate(@PathVariable Long commentid,Integer type){
        if(type==null){
            type=1;
        }
        try{
            Long id = customizedService.addCommentEvaluate(commentid, type);
            return JsonResult.success(id);
        }catch (Exception e){
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "评论取消评价", notes="取消对用户评论内容的的评价（点赞/踩），该方法只能取消当前用户对指定评论的评价")
    @DeleteMapping("/commentEvaluate/{commentid}")
    public JsonResult commentEvaluate(@PathVariable Long commentid){
        try{
            customizedService.deleteCommentEvaluate(commentid);
            return JsonResult.success();
        }catch (Exception e){
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "获取当前用户的新闻的推荐列表", notes="必须时已登录用户，用用户的id及其所在的部门id获取推荐列表")
    @GetMapping("/getPushXinwen")
    public JsonResult getPushXinwen(){
        try{
            List<XinwenSummaryVo> xinwen020List=customizedService.getPushXinwen(null);
            return JsonResult.success(xinwen020List);
        }catch (Exception e){
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "获取指定用户的新闻的推荐列表", notes="必须时已登录用户，用用户的id及其所在的部门id获取推荐列表")
    @GetMapping("/getPushXinwen/{userid}")
    public JsonResult getPushXinwen(@PathVariable Long userid){
        try{
            List<XinwenSummaryVo> xinwen020List=customizedService.getPushXinwen(userid);
            return JsonResult.success(xinwen020List);
        }catch (Exception e){
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "查询评论列表", notes="需要显示新闻标题")
    @PostMapping(value = "pageXinwenComment")
    @ResponseBody
    public JsonResult<Pager<XinwenComment>> pageXinwenComment(@RequestBody CommentQueryVo vo) {
        try{
            Pager<XinwenComment> users=customizedService.pageXinwenComment(vo);
            return JsonResult.success(users);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @Autowired
    private UserComponent userComponent;
    @GetMapping(value = "listUsers")
    public JsonResult<User> listUsers(){
        try{
            List<User> users = userComponent.listUsers();
            return JsonResult.success(users);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
}
