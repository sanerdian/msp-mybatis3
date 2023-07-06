

package com.jnetdata.msp.member.companyinfo.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.log.j_log.service.JLogService;
import com.jnetdata.msp.member.companyinfo.model.Companyinfo;
import com.jnetdata.msp.member.companyinfo.service.CompanyInfoService;
import com.jnetdata.msp.member.companyinfo.vo.BaseVo;
import com.jnetdata.msp.member.theclient.ContentLogClient;
import com.jnetdata.msp.core.model.util.SessionUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Controller
@RequestMapping("/member/company")
@ApiModel(value = "机构管理(CompanyinfoController)", description = "机构管理")
public class CompanyController extends BaseController<Long,Companyinfo> {

    @Autowired
    private CompanyInfoService companyInfoService;
    @Autowired
    private JLogService jLogService;

    private CompanyInfoService getService(){
        return companyInfoService;
    }

    @ApiOperation(value = "添加",notes = "按提供的实体属性添加机构")
    @PostMapping(value = "/add")
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Companyinfo entity){
        getService().addCompany(entity);
        return renderSuccess();
    }

    @ApiOperation(value = "根据Id查询",notes = "按指定id查询")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Companyinfo> getCompanyInfoById(@PathVariable Long  id){
        return doGetById(getService(),id);
    }

    @ApiOperation(value = "查询所有",notes = "根据vo指定条件进行查询")
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<Companyinfo>> getList(@RequestBody BaseVo<Companyinfo> vo){
        return doList(getService(),vo.getPager(),vo.getEntity());
    }

    @ApiOperation(value = "获取树列表",notes = "")
    @GetMapping(value = "/treeList")
    @ResponseBody
    public JsonResult<List<Companyinfo>> treeList(){
        return renderSuccess(getService().treeList());
    }

    @ApiOperation(value = "查询所有(条件查询)")
    @PostMapping(value = "/listByPermission")
    @ResponseBody
    public JsonResult<Pager<Companyinfo>> listByPermission(@RequestBody BaseVo<Companyinfo> vo){
        return renderSuccess(getService().listByPermission(vo.getPager().toPager(),createCondition(vo.getEntity())));
    }

    @ApiOperation(value = "修改",notes = "修改指定id的属性")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> update(@PathVariable(value = "id") Long id,@RequestBody Companyinfo entity){
        getService().edit(id,entity);
        return renderSuccess();
    }

    @ApiOperation(value = "删除",notes = "按指定id删除机构")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> delete(@PathVariable(value = "id") Long id,HttpServletRequest request){
        getService().delete(id);
        return renderSuccess();
    }

    @ApiOperation(value = "批量删除",notes = "按指定的多个id批量删除")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteByIds(@RequestParam("ids") @ApiParam("多个id用逗号隔开") String ids, HttpServletRequest request){
        jLogService.jLog(request,"机构","删除",ids,null);
        return doDeleteBatchIds(getService(), ids);
    }

}
