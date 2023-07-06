package com.jnetdata.msp.member.businessuser.controller;
import com.jnetdata.msp.member.businessuser.vo.RemoveVo;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.service.UserService;
import com.jnetdata.msp.util.service.FieldinfoUtilService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnetdata.msp.util.DownMetadataUtil;
import com.jnetdata.msp.util.model.Fieldinfo;
import com.jnetdata.msp.vo.ExportVo;
import javax.servlet.http.HttpServletResponse;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.thenicesys.web.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.vo.PageVo1;

import java.util.List;
import java.util.ArrayList;
import org.thenicesys.data.api.EntityId;
import com.jnetdata.msp.vo.BaseVo;
import org.springframework.stereotype.Controller;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.member.businessuser.service.BusinessUserService;
import com.jnetdata.msp.member.businessuser.model.BusinessUser;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyj
 * @since 2020-07-02
 */
@Controller
@RequestMapping("/member/businessuser")
@Api(value = "企业用户管理(BusinessUserController)" , description = "企业用户管理")
public class BusinessUserController extends BaseController<Long, BusinessUser> {

    final private BusinessUserService businessUserService;


    @Autowired
    private FieldinfoUtilService fieldinfoUtilService;
    @Autowired
    private UserService userService;

    @Autowired
    public BusinessUserController(BusinessUserService businessUserService) {
        this.businessUserService = businessUserService;
    }

    @ApiOperation(value = "添加组织", notes = "给指定id添加组织")
    @GetMapping("/addUserOrgan")
    @ResponseBody
    public JsonResult addUserOrgan(@RequestParam("userIds") String userIds,@RequestParam("organId") String organId ){
        if(getService().addUserGroup(userIds,organId)){
            return renderSuccess();
        }else{
            return renderError("请勿给同一用户重复添加相同组织");
        }
    }

    @ApiOperation(value = "获取当前企业下的用户", notes="获取当前企业下的用户")
    @PostMapping("/getUser")
    @ResponseBody
    public JsonResult<Pager<User>> getUser(@RequestBody BaseVo<BusinessUser> vo){
        return renderSuccess(getService().getUser(vo));
    }

    /**
     * 执行新增操作
     * @param entity
     * @return
     */
    @ApiOperation(value = "添加企业用户", notes = "按提供的实体属性添加企业用户")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody BusinessUser entity) {
        return doAdd(getService(), entity);
    }

    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除指定id的实体对象")
    @DeleteMapping("/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    /**
     * 执行批量删除操作
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量删除", notes = "按指定的多个id批量删除")
    @DeleteMapping("/{ids}/batch")
    @ResponseBody
    public JsonResult<Void> deleteBatchIds(@PathVariable("ids") @ApiParam("多个id用逗号隔开") String ids) {
        return doDeleteBatchIds(getService(), ids);
    }

    /**
     * 属性选择性更新操作
     * @param id
     * @param entity
     * @return
     */
    @ApiOperation(value = "选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PutMapping("/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody BusinessUser entity) {
        return doUpdateById(getService(), id, entity);
    }

    /**
     * 属性选择性批量更新操作
     * @param ids
     * @return
     */
    @ApiOperation(value = "选择性批量更新操作（放入回收站）", notes="只更新entity中设置为非null的属性")
    @PostMapping("/updateDocStatus")
    @ResponseBody
    public JsonResult<Void> updateBatchById(@RequestBody Long[] ids, Integer docstatus) {
        List<BusinessUser> list = new ArrayList<>();
        for (Long id : ids) {
        BusinessUser entity = new BusinessUser();
        entity.setId(id);
        entity.setDocstatus(docstatus);
        list.add(entity);
        }
        getService().updateBatchById(list);
        return renderSuccess();
    }

    /**
     * 全部属性更新操作
     * @param id
     * @param entity
     * @return
     */
    @ApiOperation(value = "执行修改操作", notes="注意必须提供实体的所有属性否则没有提供的属性将被设置为null")
    @PutMapping("/{id}/allColumn")
    @ResponseBody
    public JsonResult<Void> doUpdateAllColumnById(@PathVariable("id") Long id, @RequestBody BusinessUser entity) {
        return doUpdateAllColumnById(getService(), id, entity);
    }


    @ApiOperation(value = "根据id查询", notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<BusinessUser> getById(@PathVariable("id") Long id) {
        BusinessUser entity = getService().getById(id);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                getService().updateById(entity);
        return renderSuccess(entity);
    }
    
    @ApiOperation(value = "查询(*已废除接口)", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<BusinessUser>> list(@ModelAttribute BusinessUser entity, @RequestBody PageVo1 pager) {
        return doList(getService(), pager, entity);
    }

    @ApiOperation(value = "移除用户",notes="按提供的实体属性删除用户")
    @PostMapping(value = "/removeUser")
    @ResponseBody
    public JsonResult<Pager<BusinessUser>> removeUser(@RequestBody RemoveVo entity) {
        if(entity.getGroupid() == null){
            return renderError("企业不能为空!");
        }
        if(entity.getUserids() == null || entity.getUserids().size() == 0){
            return renderError("用户不能为空!");
        }
        getService().delete(new PropertyWrapper<>(BusinessUser.class).eq("groupid",entity.getGroupid()).in("userid",entity.getUserids()));
        return renderSuccess();
    }


    @ApiOperation(value = "根据指定实体属性查询", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<Pager<BusinessUser>> list(@RequestBody BaseVo<BusinessUser> vo) {
            return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "导出用户列表")
    @PostMapping(value = "/export")
    public void export(BusinessUser entity, ExportVo vo, HttpServletResponse response) {
        List<BusinessUser> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            datalist = getService().list(createCondition(entity));
        }
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tableid", vo.getTableId()).eq("showList", 1));
        DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle());
    }

    private BusinessUserService getService() {
        return this.businessUserService;
    }

}

