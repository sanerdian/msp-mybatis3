package com.jnetdata.msp.member.group.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.member.companyinfo.vo.BaseVo;
import com.jnetdata.msp.member.group.model.Groupinfo;
import com.jnetdata.msp.member.group.model.GrpUser;
import com.jnetdata.msp.member.group.service.GroupService;
import com.jnetdata.msp.member.group.service.GrpUserService;
import com.jnetdata.msp.member.group.vo.GrpUserVo;
import com.jnetdata.msp.member.role.service.impl.RoleServiceImpl;
import com.jnetdata.msp.member.theclient.ContentLogClient;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.service.UserService;
import com.jnetdata.msp.core.model.util.SessionUser;
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
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by WFJ on 2019/4/1.
 */

@Controller
@RequestMapping("/member/grpUser")
@ApiModel(value = "组织用户管理(GrpUserController)", description = "组织用户管理")
public class GrpUserController extends BaseController<Long, GrpUser> {
    private static final String BASE_URL = "/member/msgConfig";
    @Autowired
    private GrpUserService service;
    @Autowired
    private ContentLogClient contentLogService;
    @Autowired
    RoleServiceImpl roleService;
    @Autowired
    private GroupService groupService;

    @ApiOperation(value = "添加", notes="添加组织用户")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody GrpUser entity) {
        return doAdd(getService(), entity);
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的组织用户")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("组织用户id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除组件操作日志信息")
    @PostMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestBody @ApiParam("多个id用逗号隔开")String ids) {
        String[] s = ids.split(",");
//        for(int i = 0;i <s.length;i++){
//            GrpUser grpUser = service.getById(s[i]);
//            User user = userService.getById(grpUser.getUserId());
//            user.setIsEntry(0);
//            userService.updateById(user);
//        }
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("用户移除角色");
        contentLog.setContentType("用户管理");
        contentLog.setContent("组织管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        return doDeleteBatchIds(getService(),ids);
    }


    @ApiOperation(value = "修改", notes="修改指定id对应的组件操作日志信息")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("组件操作日志id") @PathVariable("id") Long id,
                                       @RequestBody GrpUser entity) {
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "根据id查询", notes="查看指定组件操作日志id对应的组件操作日志信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<GrpUser> getById(@ApiParam("组件操作日志id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @ApiOperation(value = "根据实体属性查询", notes="根据vo指定条件查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<GrpUser>> list(@RequestBody GrpUserVo vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "用户设置机构", notes="用户设置机构")
    @GetMapping("/addUserOrgan")
    @ResponseBody
    public JsonResult addUserOrgan(@RequestParam("userIds") String userIds,@RequestParam("organId") String organId ){
        String[] s = userIds.split(",");
//        for(int i = 0;i<s.length;i++){
//            User user = userService.getById(s[i]);
//            user.setIsEntry(1);
//            userService.updateById(user);
//        }
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("机构引入用户");
        contentLog.setContentType("用户管理");
        contentLog.setContent("组织管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        if(getService().addUserGroup(userIds,organId)){
            return JsonResult.renderSuccess(getService().addUserGroup(userIds,organId));
        }else{
            return JsonResult.renderError("请勿给同一用户重复添加相同组织");
        }
    }

    @ApiOperation(value = "用户添加组织", notes="用户添加组织")
    @GetMapping("/userAddGroup")
    @ResponseBody
    public JsonResult userAddGroup(@RequestParam("userId") String userId,@RequestParam("grpIds") String grpIds ){
        String[] s = grpIds.split(",");

        val user1 = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user1.getName());
        contentLog.setHandleType("用户添加组织");
        contentLog.setContentType("编辑成员信息");
        contentLog.setContent("所属组织");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);

        //将用户添加到组织表
        //根据用户id查询该用户所在组织列表
        List<GrpUser> grpUsers = getService().list(new PropertyWrapper<>(GrpUser.class).eq("userId",userId));
        //遍历grpIds,若grpId == user.getGroupId
        for (String grpId : s) {
            final Boolean[] flag = {false};
            grpUsers.forEach(grpUser -> {
                if(grpId.equals(grpUser.getGroupId().toString())){
                    System.out.println(111);
                    flag[0] = true;
                    return;
                }
            });
            if(!flag[0]){
                GrpUser grpUser = new GrpUser();
                grpUser.setGroupId(Long.valueOf(grpId));
                grpUser.setUserId(Long.valueOf(userId));
                getService().insert(grpUser);
            }
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "获取当前机构下的用户", notes="获取当前机构下的用户")
    @PostMapping("/getUser")
    @ResponseBody
    public JsonResult getUser(@RequestBody BaseVo<GrpUser> vo){
        return JsonResult.renderSuccess(getService().getUser(vo));
    }

    @ApiOperation(value = "设置组长", notes="设置组长")
    @GetMapping("/updateLoader")
    @ResponseBody
    public JsonResult updateLoader(@RequestParam("ids") Long[] ids,@RequestParam("type") int type){
        return  JsonResult.renderSuccess(getService().updataLoader(ids,type));
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

    @ApiOperation(value = "设置/取消组长", notes="设置/取消组长")
    @PostMapping("/changeLeader")
    @ResponseBody
    public JsonResult changeLeader(Long[] ids, int isLeader) {
        if(ids == null || ids.length <=0){
            return  renderError("请选择用户!");
        }

//根据ids获取gruser的信息 通过gruser获取他的所在的组织编号
//        如果gruser的islea是1 直接返回只能设置一个组长  如果为0通过 继续
//        返回布尔值
// 根据组织编号获取groupinfo对象 从而获取isleader
//        if(!getService().updataLoader(ids,isLeader)){
//            renderError("修改失败,请联系管理员!");
//        }
        /*1.根据ids获取gruser信息*/
        Long groupId = doGetById(getService(), ids[0]).getObj().getGroupId();
        Groupinfo groupinfo1 = groupService.get(new PropertyWrapper<>(Groupinfo.class).eq("groupId", groupId));
        if(isLeader==1){
            /*1.根据ids获取gruser信息*/
            if(groupinfo1!=null){
//                如果这组有组长 直接返回
                if(groupinfo1.getIsleader()==1){
                    return renderError("该组已有组长，不能重复设置！");
                }
//                如果没有组长 设置这组有组长
                    groupinfo1.setIsleader(1);
//                然后更新操作
                    groupService.updateById(groupinfo1);
//                    改变grpuser状态
                    service.updataLoader(ids, isLeader);
                    val user = SessionUser.getCurrentUser();
                    ContentLog contentLog = new ContentLog();
                    contentLog.setCrUser(user.getName());
                    contentLog.setHandleType("设置/取消组长");
                    contentLog.setContentType("用户管理");
                    contentLog.setContent("组织管理");
                    contentLog.setResult("成功");
                    contentLog.setCreateTime(new Date());
                    //添加修改人和修改时间
                    contentLog.setModifyUser("admin");
                    contentLog.setModifyTime(new Date());
                    contentLogService.insert(contentLog);
                    return JsonResult.renderSuccess();
            }
        }
//        如果isleader==0 要设置为取消组长
                groupinfo1.setIsleader(0);
                groupService.updateById(groupinfo1);
                service.updataLoader(ids, isLeader);
                val user = SessionUser.getCurrentUser();
                ContentLog contentLog = new ContentLog();
                contentLog.setCrUser(user.getName());
                contentLog.setHandleType("设置/取消组长");
                contentLog.setContentType("用户管理");
                contentLog.setContent("组织管理");
                contentLog.setResult("成功");
                contentLog.setCreateTime(new Date());
                //添加修改人和修改时间
                contentLog.setModifyUser("admin");
                contentLog.setModifyTime(new Date());
                contentLogService.insert(contentLog);
                return JsonResult.renderSuccess();
    }

    @ApiOperation(value = "设置职位", notes="设置职位")
    @GetMapping("/updateJob")
    @ResponseBody
    public JsonResult updateJob(@RequestParam("id") Long id,@RequestParam("job") String job){
        GrpUser grpUser = new GrpUser();
        grpUser.setId(id);
        grpUser.setJob(job);
        return  JsonResult.renderSuccess(getService().updateById(grpUser));
    }

    @ApiOperation(value = "获取用户组织信息", notes="获取用户组织信息")
    @GetMapping("/getGrpUserInfo")
    @ResponseBody
    public JsonResult getGrpUserInfo(@RequestParam("userId") Long userId){
       GrpUser grpUser = getService().get(new PropertyWrapper<>(GrpUser.class).eq("userId",userId));
        return  JsonResult.renderSuccess(grpUser);
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

    private GrpUserService getService() {
        return service;
    }


}
