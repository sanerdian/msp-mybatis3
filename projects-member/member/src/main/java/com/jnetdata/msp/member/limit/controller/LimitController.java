package com.jnetdata.msp.member.limit.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.member.companyinfo.vo.BaseVo;
import com.jnetdata.msp.member.limit.model.Limit;
import com.jnetdata.msp.member.limit.model.LimitType;
import com.jnetdata.msp.member.limit.service.LimitService;
import com.jnetdata.msp.member.limit.service.LimitTypeService;
import com.jnetdata.msp.member.shiro.impl.ShiroUtil;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by WFJ on 2019/4/2.
 */

@Controller
@RequestMapping("/member/limit")
@ApiModel(value = "权限管理(LimitController)", description = "权限管理")
public class LimitController extends BaseController<Long,Limit> {

    private static final String BASE_URL = "/member/limit";
    @Autowired
    private LimitService service;
    @Autowired
    private LimitTypeService limitTypeService;

    @Autowired
    private ContentLogClient contentLogService;

    /**
     *按提供的实体属性添加权限信息
     * @param entity
     * @return
     */
    @ApiOperation(value = "添加", notes="按提供的实体属性添加权限信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Limit entity) {
//        val user = SessionUser.getCurrentUser();
//        ContentLog contentLog = new ContentLog();
//        contentLog.setCrUser(user.getName());
//        contentLog.setHandleType("新建权限");
//        contentLog.setContentType("权限管理");
//        contentLog.setContent("权限操作管理");
//        contentLog.setResult("成功");
//        contentLog.setCreateTime(new Date());
//        contentLogService.insert(contentLog);
//        entity.setCrUser(user.getName());
        JsonResult result = doAdd(getService(), entity);
        ShiroUtil.clearAuth();
        return result;
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的权限信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("对象id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除权限信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") @ApiParam("多个id用逗号隔开")String ids) {
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("删除权限");
        contentLog.setContentType("权限管理");
        contentLog.setContent("权限操作管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);

        return doDeleteBatchIds(getService(), Arrays.asList(ids.split(",")));
    }


    @ApiOperation(value = "修改", notes="修改指定id对应的权限信息")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("对象id") @PathVariable("id") Long id, @RequestBody Limit entity) {
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("修改权限");
        contentLog.setContentType("权限管理");
        contentLog.setContent("权限操作管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);

        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "根据id查询", notes="查看指定对象id对应的权限信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Limit> getById(@ApiParam("对象id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }


    @ApiOperation(value = "查所有", notes="根据system=0的查询出来，自定义的权限类别查询出来")
    @GetMapping(value = "/list/custom")
    @ResponseBody
    public JsonResult<List<Limit>> getConstomList(Long id) {
        return renderSuccess(getService().list(new PropertyWrapper<>(Limit.class).eq("type",id)));
    }

    @ApiOperation(value = "查询权限类型", notes="查询权限类型")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<Limit>> list(@RequestBody BaseVo<Limit> vo) {
        JsonResult<Pager<Limit>> result =  doList(getService(), vo.getPager(), vo.getEntity());
        List<Limit> list =result.getObj().getRecords();
        list.forEach(e -> {
            if(e.getType()!=null){
                LimitType limitType = limitTypeService.getById(e.getType());
                if(limitType!=null){
                    String typename = limitType.getName();
                    e.setTypename(typename);
                }
            }
        });
        return result;
    }

    /**
     * 通过Excel批量上传
     * @param file
     * @return
     * @throws Exception
     * @auther hongshou
     * @date 2020/5/26
     */
    @ApiOperation(value = "批量上传", notes="通过excel批量上传数据")
    @PostMapping("/uploadExcel")
    @ResponseBody
    public JsonResult uploadExcel( MultipartFile file)throws Exception{
//        getService().uploadData(file);
        return JsonResult.renderSuccess(null);
    }

    /**
     * 根据查询条件下载execl数据
     * @auther hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "下载数据", notes="根据查询条件下载excel数据")
    @PostMapping("/downloadExcel")
    @ResponseBody
    public JsonResult downloadExcel() {

        return JsonResult.renderSuccess(null);

    }

    private LimitService getService() {
        return service;
    }

    /**
     * 获取用户名定义权限操作名
     * @param vo
     * @auther hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "获取用户定义权限操作名", notes="获取用户定义权限操作名")
    @PostMapping("/getAll")
    @ResponseBody
    public JsonResult getAll(@RequestBody Limit vo) {

        return renderSuccess(getService().list(vo));


    }

    /**
     * 权限开启关闭状态
     * @param id
     * @return
     * @auther hongshou
     * @date 2020/5/26
     */
    @ApiOperation(value = "开启关闭状态", notes="开启关闭状态")
    @PostMapping("/getStatus")
    @ResponseBody
    public JsonResult getStatus(@RequestBody Long id) {
        Limit limit= getService().getById(id);
        if(limit.getStatus()==0){
            limit.setStatus(1);
            getService().updateById(limit);
        } else{
            limit.setStatus(0);
            getService().updateById(limit);
        }
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("权限开启状态修改");
        contentLog.setContentType("权限管理");
        contentLog.setContent("权限操作管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);

        return JsonResult.renderSuccess(limit);


    }

}
