package com.jnetdata.msp.member.limit.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.member.companyinfo.vo.BaseVo;
import com.jnetdata.msp.member.limit.model.LimitType;
import com.jnetdata.msp.member.limit.service.LimitTypeService;
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

/**
 * Created by WFJ on 2019/4/2.
 * @auther hongshou
 * @date 2020/5/26
 */

@Controller
@RequestMapping("/member/limit/type")
@ApiModel(value = "权限类型(LimitTypeController)", description = "权限类型")
public class LimitTypeController extends BaseController<Long, LimitType> {

    @Autowired
    private LimitTypeService service;

    @Autowired
    private ContentLogClient contentLogService;

    /**
     * 按提供的实体属性添加权限类型
     * @param entity
     * @return
     * @auther hongshou
     * @date 2020/5/26
     */
    @ApiOperation(value = "添加", notes="按提供的实体属性添加权限类型")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody LimitType entity) {

        entity.setCreateTime(new Date());
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("添加权限");
        contentLog.setContentType("权限管理");
        contentLog.setContent("权限分类管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        entity.setCrUser(user.getName());
        return doAdd(getService(), entity);
    }

    /**
     * 删除id对应的权限信息
     * @param id
     * @auther hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "删除", notes="删除指定id对应的权限信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("组件操作日志id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    /**
     * 根据id批量删除
     * @param ids
     * @auther hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "批量删除", notes="根据多个id删除权限信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") String ids) {
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("删除权限");
        contentLog.setContentType("权限管理");
        contentLog.setContent("权限分类管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        return doDeleteBatchIds(getService(), Arrays.asList(ids.split(",")));
    }

    /**
     * 修改指定id对应的权限信息
     * @param id
     * @param entity
     * @auther hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "修改", notes="修改指定id对应的权限信息")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("权限类型id") @PathVariable("id") Long id,
                                       @RequestBody LimitType entity) {
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("修改权限");
        contentLog.setContentType("权限管理");
        contentLog.setContent("权限分类管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        return doUpdateById(getService(), id, entity);
    }

    /**
     * 查看指定权限id对应的权限类型信息
     * @param id
     * @auther hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "根据id查询", notes="查看指定权限id对应的权限类型信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<LimitType> getById(@ApiParam("权限类型id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    /**
     * 自定义权限分类查询
     * @param vo
     * @auther hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "根据实体属性查询", notes="根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<LimitType>> list(@RequestBody BaseVo<LimitType> vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }
    //swagger包中的注解，描述接口的一些信息 value属性指接口说明 notes属性指接口发布说明
    @ApiOperation(value = "查询(自定义权限分类查询)", notes="根据vo查询,自定义权限分类查询")
    //请求方式为post
    @PostMapping("/all")
    //返回参数类型为json
    @ResponseBody
    //返回一个类型为LimitType的List集合 参数是后端传过来的system值
    public JsonResult<List<LimitType>> all(Integer system) {
        //先判断system是否为空
        if(system!=null){
            //不为空的话使用条件查询数据库中符合条件的数据并返回
            return renderSuccess(getService().list(new PropertyWrapper<>(LimitType.class).eq("system",system)));
        }else {
            //为空的话直接查所有 不影响原来的功能
            return renderSuccess(getService().list());
        }
    }

    /**
     * 通过Excel批量上传
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "批量上传", notes="通过excel批量上传数据")
    @PostMapping("/uploadExcel")
    @ResponseBody
    public JsonResult uploadExcel( MultipartFile file)throws Exception{
        return renderSuccess();
    }

    /**
     * 根据查询条件下载Excel数据
     * @auther hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "下载数据", notes="根据查询条件下载excel数据")
    @PostMapping("/downloadExcel")
    @ResponseBody
    public JsonResult downloadExcel() {
        return renderSuccess();

    }

    private LimitTypeService getService() {
        return service;
    }

    /**
     * 获取用户定义权限分类名
     * @auther hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "获取用户定义权限分类名", notes="获取用户定义权限分类名")
    @RequestMapping(value = "/getAll", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public JsonResult getAll() {
        return renderSuccess(getService().list(new PropertyWrapper<>(LimitType.class).eq("system",0)));
    }

    /**
     * 转换修改状态
     * @param id
     * @auther hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "转换修改状态", notes="转换修改状态")
    @PostMapping("/getStatus")
    @ResponseBody
    public JsonResult getStatus(@RequestBody Long id) {
        LimitType limitType= getService().getById(id);
        if(limitType.getStatus()==0){
            limitType.setStatus(1);
            getService().updateById(limitType);
        } else{
            limitType.setStatus(0);
            getService().updateById(limitType);
        }
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("权限开启状态修改");
        contentLog.setContentType("权限管理");
        contentLog.setContent("权限分类管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        return renderSuccess(limitType);
    }
}
