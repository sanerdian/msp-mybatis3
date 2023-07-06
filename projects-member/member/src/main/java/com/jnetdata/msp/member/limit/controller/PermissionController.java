package com.jnetdata.msp.member.limit.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.member.limit.model.Permission;
import com.jnetdata.msp.member.limit.service.PermissionService;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/member/permission")
@ApiModel(value = "权限管理(PermissionController)", description = "权限管理")
public class PermissionController extends BaseController<Long, Permission> {

    @Autowired
    private PermissionService service;

    @ApiOperation(value = "添加", notes="添加权限")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Permission entity) {
        return doAdd(getService(), entity);
    }

    @ApiOperation(value = "设置权限", notes="设置权限")
    @PostMapping("/setPermission")
    @ResponseBody
    public JsonResult setPermission(@RequestBody List<Permission> list){
        if(getService().setPermission(list)){
            return renderSuccess("修改成功");
        }else{
            return renderError("修改失败!");
        }
    }

    @ApiOperation(value = "设置权限", notes="设置权限")
    @PostMapping("/setPermissionNew")
    @ResponseBody
    public JsonResult setPermissionNew(@RequestBody Map<String,Boolean> map, Long ownerId, Integer ownerType){
        if(getService().setPermission(map,ownerId,ownerType)){
            return renderSuccess("修改成功");
        }else{
            return renderError("修改失败!");
        }
    }

    @ApiOperation(value = "删除权限", notes="删除权限")
    @PostMapping("/delPermission")
    @ResponseBody
    public JsonResult delPermission(@RequestBody Permission entity){
        if(entity.getOwnerId() == null || entity.getPermission() == null) return renderError("参数错误");
        getService().delete(new PropertyWrapper<>(Permission.class).eq("ownerId",entity.getOwnerId()).eq("permission",entity.getPermission()));
        return renderSuccess();
    }

    /**
     * 获取权限分类
     * @param entity
     * @return
     */
    @ApiOperation(value = "获取权限", notes="获取权限")
    @PostMapping("/all")
    @ResponseBody
    public JsonResult getPermission(@RequestBody Permission entity){
        return renderSuccess(getService().list(createCondition(entity)));
    }

    /**
     * 加载当前栏目弹框获取权限
     * @param entity
     * @return
     */
    @ApiOperation(value = "获取权限", notes="获取权限")
    @PostMapping("/allStr")
    @ResponseBody
    public JsonResult allStr(@RequestBody Permission entity){
        List<Permission> list = getService().list(createCondition(entity));
        List<String> collect = list.stream().map(m -> m.getPermission()).collect(Collectors.toList());
        return renderSuccess(collect);
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的Permission日志信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("Permission日志id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除Permission日志信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") String ids) {
        return doDeleteBatchIds(getService(),ids);
    }

    @ApiOperation(value = "修改", notes="修改指定id对应的Permission日志信息")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("Permission日志id") @PathVariable("id") Long id, @RequestBody Permission entity) {
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "查看", notes="查看指定Permission日志id对应的Permission日志信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Permission> getById(@ApiParam("Permission日志id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @ApiOperation(value = "根据实体属性查询", notes="根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<Permission>> list(@RequestBody BaseVo<Permission> vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    /**
     * 查询当前用户权限
     * @return
     */
    @ApiOperation(value = "查询当前用户权限", notes="查询当前用户权限")
    @GetMapping("/myPermission")
    @ResponseBody
    public JsonResult<List<String>> myPermission(String per) {
        var user = SessionUser.getCurrentUser();
        List<String> userPermissionStr = getService().getUserPermissionStr(user.getId(),per);//获取权限值
        return renderSuccess(userPermissionStr);
    }

    /**
     * 查询当前用户权限数据id
     * @return
     */
    @ApiOperation(value = "查询当前用户权限数据id", notes="查询当前用户权限")
    @GetMapping("/myPermissionIds")
    @ResponseBody
    public JsonResult<List<Long>> myPermissionIds(String per) {
        var user = SessionUser.getCurrentUser();
        List<Long> userPermissionStr = getService().getUserPermissionIds(user.getId(),per);//获取权限值
        return renderSuccess(userPermissionStr);
    }

    private PermissionService getService() {
        return service;
    }
}
