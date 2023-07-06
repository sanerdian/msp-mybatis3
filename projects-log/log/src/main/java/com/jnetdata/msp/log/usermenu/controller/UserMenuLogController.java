package com.jnetdata.msp.log.usermenu.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.log.usermenu.model.UserMenuLog;
import com.jnetdata.msp.log.usermenu.service.UserMenuLogService;
import com.jnetdata.msp.log.usermenu.vo.UserMenuLogVo;
import io.swagger.annotations.Api;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * 用户菜单操作日志
 */
@Controller
@RequestMapping("/log/userMenu")
@ApiModel(value = "UserMenuLogController", description = "用户菜单操作日志")
@Api(tags = "用户菜单操作日志(UserMenuLogController)")
public class UserMenuLogController extends BaseController<Long,UserMenuLog> {
    private static final String BASE_URL = "/log/usermenu";

    @Autowired
    private UserMenuLogService service;



    @ApiOperation(value = "添加信息", notes="添加用户菜单操作日志信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody UserMenuLog entity, HttpServletRequest request) {
        val user = SessionUser.getCurrentUser();
        String ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        //"***.***.***.***".length() = 15
        if(ipAddress!=null && ipAddress.length()>15){
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        if(entity.getCrUser()==null) {
            String name = user.getName();
            entity.setCrUser(name);
        }
        String s = getService().selectSql(entity.getContent());
        entity.setRemark(s);
        entity.setAddress(ipAddress);
        entity.setCreateTime(new Date());
        return doAdd(getService(), entity);
    }
/*
* 用户菜单操作日志，删除指定id对应的用户菜单操作日志信息
* */
    @ApiOperation(value = "删除", notes="删除指定id对应的用户菜单操作日志信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("用户菜单操作日志id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除用户菜单操作日志信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") @ApiParam("多个id用逗号隔开")String ids) {
        return doDeleteBatchIds(getService(),ids);
    }


    @ApiOperation(value = "修改", notes="修改指定id对应的用户菜单操作日志信息")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("用户菜单操作日志id") @PathVariable("id") Long id,
                                       @RequestBody UserMenuLog entity) {
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "根据id查询", notes="查看指定用户菜单操作日志id对应的用户菜单操作日志信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<UserMenuLog> getById(@ApiParam("用户菜单操作日志id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @ApiOperation(value = "根据实体属性查询", notes="根据vo指定条件进行查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<UserMenuLog>> list(@RequestBody UserMenuLogVo vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
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

    private UserMenuLogService getService() {
        return service;
    }

}
