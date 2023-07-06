package com.jnetdata.msp.member.business.controller;
import com.jnetdata.msp.core.model.util.IUser;
import com.jnetdata.msp.core.model.util.SessionUser;
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

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import org.thenicesys.data.api.EntityId;
import com.jnetdata.msp.vo.BaseVo;
import org.springframework.stereotype.Controller;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.member.business.service.BusinessService;
import com.jnetdata.msp.member.business.model.Business;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyj
 * @since 2020-07-02
 */
@Controller
@RequestMapping("/member/business")
@ApiModel(value = "企业管理(BusinessController)" , description = "企业管理")
@Api(tags = "企业管理(BusinessController)")
public class BusinessController extends BaseController<Long,Business> {

    final private BusinessService businessService;


    @Autowired
    private FieldinfoUtilService fieldinfoUtilService;

    @Autowired
    private UserService userService;

    @Autowired
    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    

    /**
     * 执行新增操作
     * @param entity
     * @return
     */
    @ApiOperation(value = "添加操作", notes="根据提供的实体属性添加企业")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody Business entity) {
        entity.setStatus("0");
        return doAdd(getService(), entity);
    }

    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @ApiOperation(value = "删除操作", notes= "根据指定id删除企业")
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
    @ApiOperation(value = "批量删除操作", notes="按指定的多个id批量删除企业")
    @DeleteMapping("/{ids}/batch")
    @ResponseBody
    public JsonResult<Void> deleteBatchIds(@PathVariable("ids")@ApiParam("多个id用逗号隔开")String ids) {
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
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody Business entity) {
//        entity.setStatus("1");
        return doUpdateById(getService(), id, entity);
    }

    /**
     * 属性选择性更新操作
     * @param id
     * @return
     */
    @ApiOperation(value = "签署协议接口", notes="只更新entity中设置为非null的属性")
    @PutMapping("/qsxy/{id}")
    @ResponseBody
    public JsonResult<Void> qsxy(@PathVariable("id") Long id) {
        Business entity = new Business();
        entity.setStatus("4");
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
        List<Business> list = new ArrayList<>();
        for (Long id : ids) {
        Business entity = new Business();
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
    public JsonResult<Void> doUpdateAllColumnById(@PathVariable("id") Long id, @RequestBody Business entity) {
        return doUpdateAllColumnById(getService(), id, entity);
    }


    @ApiOperation(value = "根据id查询", notes = "查看指定id的企业信息")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<Business> getById(@PathVariable("id") Long id) {
        Business entity = getService().getById(id);
        return renderSuccess(entity);
    }
    
    @ApiOperation(value = "查询(*已废除接口)", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<Business>> list(@ModelAttribute Business entity,@RequestBody PageVo1 pager) {
        return doList(getService(), pager, entity);
    }


    @ApiOperation(value = "根据实体属性查询", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<Pager<Business>> list(@RequestBody BaseVo<Business> vo) {
            return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "企业子账户列表",notes="查询指定id的子账户")
    @PostMapping(value = "/qyUserList/{qyid}")
    @ResponseBody
    public JsonResult<List<User>> qyUserList(@PathVariable("qyid") Long qyid) {
        Business business = getService().getById(qyid);
        List<User> users = userService.list(new PropertyWrapper<>(User.class).eq("businessId", qyid).eq("status",0).ne("id",business.getUserid()));
        for (User user : users) {
            user.setPassWord(null);
            user.setMdPassWord(null);
        }
        return renderSuccess(users);
    }

    @ApiOperation(value = "企业账户列表",notes="查询指定id的账户")
    @GetMapping(value = "/qyUserList/{qyid}")
    @ResponseBody
    public JsonResult<List<User>> qyUserList1(@PathVariable("qyid") Long qyid) {
        List<User> users = userService.list(new PropertyWrapper<>(User.class).eq("businessId", qyid).eq("status",0));
        for (User user : users) {
            user.setPassWord(null);
            user.setMdPassWord(null);
        }
        return renderSuccess(users);
    }

    /**
     * 属性选择性更新操作
     * @return
     */
    @ApiOperation(value = "删除子账号",notes="删除指定id的子账号")
    @PutMapping("/removeUser/{userid}")
    @ResponseBody
    public JsonResult<Void> removeUser(@PathVariable("userid") Long userid) {
        IUser<Long> me = SessionUser.getCurrentUser();
        User user = userService.getById(userid);
        user.setBusinessId(null);
        user.setModifyTime(new Date());
        user.setModifyUser(me.getName());
        user.setModifyBy(me.getId());
        userService.updateAllColumnById(user);
        return renderSuccess();
    }

    @ApiOperation(value = "导出企业列表")
    @PostMapping(value = "/export")
    public void export(Business entity, ExportVo vo, HttpServletResponse response) {
        List<Business> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            datalist = getService().list(createCondition(entity));
        }
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tableid", vo.getTableId()).eq("showList", 1));
        DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle());
    }

    private BusinessService getService() {
        return this.businessService;
    }

}

