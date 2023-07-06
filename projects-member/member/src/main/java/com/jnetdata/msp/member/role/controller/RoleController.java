package com.jnetdata.msp.member.role.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.log.j_log.service.JLogService;
import com.jnetdata.msp.member.companyinfo.service.CompanyInfoService;
import com.jnetdata.msp.member.role.model.Role;
import com.jnetdata.msp.member.role.service.RoleService;
import com.jnetdata.msp.member.role.vo.RoleVo;
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
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by WFJ on 2019/4/2.
 */

@Controller
@RequestMapping("/member/role")
@ApiModel(value = "角色管理(RoleController)", description = "角色管理")
public class RoleController extends BaseController<Long,Role>{
    private static final String BASE_URL = "/member/role";
    @Autowired
    private RoleService service;

    @Autowired
    private ContentLogClient contentLogService;
    @Autowired
    private JLogService jLogService;

    @ApiOperation(value = "添加", notes="按提供的实体属性添加角色")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Role entity, HttpServletRequest request) {
        if(entity.getParentId()!=null){
            if(entity.getParentId()!=0){
                Role role = service.getById(entity.getParentId());
                Long newCompanyId = role.getCompanyId();
                entity.setCompanyId(newCompanyId);
            }
        }
        JsonResult<EntityId<Long>> result = doAdd(getService(), entity);
        jLogService.addLog(request,"添加角色","添加",entity.getId(),entity.getName(),"角色");
        return result;
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的角色")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("角色id") @PathVariable("id") Long id, HttpServletRequest request) {
        jLogService.addLog(request,"删除角色","删除",id,null,"角色");
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除角色")
    @PostMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestBody Long[] ids, HttpServletRequest request) {
        jLogService.addLogs(request,"删除角色","删除",ids,null,"角色");
        getService().deleteBatchIds(Arrays.asList(ids));
        return renderSuccess();
    }


    @ApiOperation(value = "修改", notes="修改指定id对应的角色")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("组件操作日志id") @PathVariable("id") Long id,
                                       @RequestBody Role entity, HttpServletRequest request) {
        jLogService.addLog(request,"修改角色","修改",id,null,"角色");
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "根据id查询", notes="查看指定角色id对应的信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Role> getById(@ApiParam("组件操作日志id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @Autowired
    private CompanyInfoService companyInfoService;

    @ApiOperation(value = "根据实体属性查询", notes="根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<Role>> list(@RequestBody RoleVo vo) {
        JsonResult<Pager<Role>> result = doList(getService(), vo.getPager(), vo.getEntity());
        List<Role> list = result.getObj().getRecords();
        list.forEach(e ->{
            if(companyInfoService.getById(e.getCompanyId())!=null){
                String name =companyInfoService.getById(e.getCompanyId()).getName();
                e.setCompanyName(name);
            }

        });
        return result;
    }

    @ApiOperation(value = "获取树对象")
    @PostMapping("/tree")
    @ResponseBody
    public JsonResult<List<Role>> tree() {
        return renderSuccess(getService().tree());
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


    @ApiOperation(value = "获取角色树", notes="获取角色树")
    @GetMapping("/getRoleTree")
    @ResponseBody
    public JsonResult<List<Role>> getRoleTree(){

        return JsonResult.renderSuccess(getService().getRoleTree());
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

    private RoleService getService() {
        return service;
    }
}
