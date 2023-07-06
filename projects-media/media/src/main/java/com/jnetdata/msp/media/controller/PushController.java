package com.jnetdata.msp.media.controller;

import com.jnetdata.msp.media.service.PushService;
import com.jnetdata.msp.media.vo.PushSettingVo;
import com.jnetdata.msp.media.vo.push.SimpleWorkerVo;
import com.jnetdata.msp.media.vo.push.TreeNodeVo;
import com.jnetdata.msp.media.vo.push.UserTagQueryVo;
import com.jnetdata.msp.member.group.model.Groupinfo;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/media/push")
@Api(description = "推送")
public class PushController {
    @Autowired
    private PushService pushService;

    @ApiOperation("保存新闻的推送设置信息")
    @PostMapping("/xinwen")
    public JsonResult saveXinwenPush(@RequestBody PushSettingVo vo){
        try {
            pushService.saveXinwenPush(vo);
            return JsonResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }

//    @ApiOperation("获取机构数组，该数组中的每一项都是当前用户所在部门或其子部门")
//    @GetMapping("/groupTree")
//    public JsonResult groupTree4CurrentUser(){
//        try {
//            List<Groupinfo> groupinfos=pushService.groupTree4CurrentUser();
//            return JsonResult.success(groupinfos);
//        }catch (Exception e){
//            e.printStackTrace();
//            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
//        }
//    }
    @ApiOperation(value = "保存问卷推送")
    @PostMapping("/survey")
    public JsonResult saveSurveyPush(@RequestBody PushSettingVo vo){
        try{
            pushService.saveSurveyPush(vo);
            return JsonResult.success("问卷推送保存成功！");
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "加载单位机构树")
    @GetMapping("/loadDwTree")
    public JsonResult loadDwTree(String id,String pid,String name){
        try{
            List<TreeNodeVo> treeNodeVos;
            if(ObjectUtils.isEmpty(name)){
                if(ObjectUtils.isEmpty(pid)){//查询根节点
                    treeNodeVos =pushService.loadDwRoot(id);
                }else {//根据父节点加载子节点
                    treeNodeVos =pushService.loadDwByParent(pid);
                }
            }else {//按名称模糊查询，注意只能是在以用户所在部门为根节点的树中查询
                treeNodeVos =pushService.loadDwByName(id, name);//按名字加载，注意：
            }

            return JsonResult.success(treeNodeVos);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }

    }
    @ApiOperation(value="加载人员")
    @PostMapping("/pageWorker")
    public JsonResult pageWorker(@RequestBody BaseVo<SimpleWorkerVo> vo){
        try{
            Pager<SimpleWorkerVo> vos=pushService.pageWorker(vo);
            return JsonResult.success(vos);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value="获取人员的标签，如：性别、政治面貌、学历等")
    @PostMapping("/userTags")
    public JsonResult queryUserTags(@RequestBody UserTagQueryVo vo){
        try{
            Map<String,List<String>> tags=pushService.queryUserTags(vo);
            return JsonResult.success(tags);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
}
