package com.jnetdata.msp.member.role.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.member.role.model.Role;
import com.jnetdata.msp.member.role.model.RoleUser;
import com.jnetdata.msp.member.role.service.RoleService;
import com.jnetdata.msp.member.role.service.RoleUserService;
import com.jnetdata.msp.member.role.vo.RoleModelVo;
import com.jnetdata.msp.member.role.vo.RoleUserVo;
import com.jnetdata.msp.member.theclient.ContentLogClient;
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

import java.util.*;

/**
 * Created by WFJ on 2019/4/2.
 */

@Controller
@RequestMapping("/member/roleUser")
@ApiModel(value = "用户角色关联表(RoleUserController)", description = "用户角色关联表")
public class RoleUserController extends BaseController<Long,RoleUser> {
    @Autowired
    private RoleUserService service;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ContentLogClient contentLogService;


    @ApiOperation(value = "添加", notes="按提供的实体属性添加用户角色")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody RoleUser entity) {

        return doAdd(getService(), entity);
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的用户角色")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("用户角色id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除用户角色")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") @ApiParam("多个id用逗号隔开")String ids) {
        return doDeleteBatchIds(getService(),ids);
    }

    @ApiOperation(value = "批量删除(根据用户id)", notes="根据多个id删除用户角色")
    @GetMapping("/delByUserIds")
    @ResponseBody
    public JsonResult delByUserIds(@RequestParam("ids") String ids,Long roleId) {
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("用户移除角色");
        contentLog.setContentType("用户管理");
        contentLog.setContent("角色管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        return renderSuccess(getService().delete(new PropertyWrapper<>(RoleUser.class).eq("roleId",roleId).in("userId", Arrays.asList(ids.split(",")))));
    }


    @ApiOperation(value = "修改", notes="修改指定id对应的用户角色")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("用户角色id") @PathVariable("id") Long id,
                                       @RequestBody RoleUser entity) {
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "根据id查询", notes="查看指定用户角色id对应的用户角色信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<RoleUser> getById(@ApiParam("用户角色id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @ApiOperation(value = "根据实体属性查询", notes="根据vo指定条件进行查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<RoleUser>> list(@RequestBody RoleUserVo vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "批量上传", notes="通过excel批量上传数据")
    @PostMapping("/uploadExcel")
    @ResponseBody
    public JsonResult uploadExcel( MultipartFile file)throws Exception{
//        getService().uploadData(file);
        return JsonResult.renderSuccess(null);
    }


    @ApiOperation(value = "用户添加角色", notes="用户添加角色")
    @GetMapping("/addUserRole")
    @ResponseBody
    public JsonResult addUserRole(@RequestParam("userIds") String userIds,@RequestParam("roleId") String roleId){
        if(getService().addUserRole(userIds,roleId)){
            return JsonResult.renderSuccess(getService().addUserRole(userIds,roleId));
        }else {
            return JsonResult.renderError("请勿给同一用户添加相同角色");
        }

    }

    @ApiOperation(value = "获取当前角色下的用户", notes="获取当前角色下的用户")
    @PostMapping("/getUser")
    @ResponseBody
    public JsonResult<RoleModelVo> getUser(@RequestBody RoleUserVo vo){

        return JsonResult.renderSuccess(getService().getUser(vo));
    }

    @ApiOperation(value = "获取当前用户的角色", notes="获取当前用户的角色")
    @PostMapping("/getRole")
    @ResponseBody
    public JsonResult<RoleModelVo> getRole(@RequestBody String userId){
        List<RoleUser> list = getService().list(new PropertyWrapper<>(RoleUser.class).eq("userId",userId));
        List<Long> roleIds = new ArrayList<>();
        if(list!=null&&list.size()>0){
            for (RoleUser roleUser : list) {
                roleIds.add(roleUser.getRoleId());
            }
        }
        List<Role> allRoles = roleService.list();
        Map<String,Object> map = new HashMap<>();
        map.put("allRoles",allRoles);
        map.put("userRole",roleIds.toArray());
        return JsonResult.renderSuccess(map);
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
        if(!getService().updataLoader(ids,isLeader)){
            renderError("修改失败,请联系管理员!");
        }
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("设置/取消组长");
        contentLog.setContentType("用户管理");
        contentLog.setContent("角色管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        return JsonResult.renderSuccess();

    }

    private RoleUserService getService() {
        return service;
    }

}
