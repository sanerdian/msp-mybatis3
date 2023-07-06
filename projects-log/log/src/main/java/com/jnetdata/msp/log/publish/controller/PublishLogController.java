package com.jnetdata.msp.log.publish.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.publish.model.PublishLog;
import com.jnetdata.msp.log.publish.service.PublishLogService;
import com.jnetdata.msp.log.publish.vo.PublishLogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by TF on 2019/3/13.
 */

@Controller
@RequestMapping("/log/publish")
@ApiModel(value = "PublishLogController", description = "发布日志信息")
@Api(tags = "发布日志信息(PublishLogController)")
public class PublishLogController extends BaseController<Long,PublishLog> {

    private static final String BASE_URL = "/log/publish";
    @Autowired
    private PublishLogService service;


    @ApiOperation(value = "添加信息", notes="添加发布日志信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody PublishLog entity, HttpServletRequest request) {
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
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        entity.setAddress(ipAddress);
        return doAdd(getService(), entity);
    }


    @ApiOperation(value = "根据实体属性查询", notes="根据vo指定条件进行查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<PublishLog>> list(@RequestBody PublishLogVo vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
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

    private PublishLogService getService() {
        return service;
    }

}
