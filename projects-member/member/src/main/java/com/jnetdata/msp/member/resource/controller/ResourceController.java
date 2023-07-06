package com.jnetdata.msp.member.resource.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.member.resource.model.Resource;
import com.jnetdata.msp.member.resource.service.ResourceService;
import io.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.thenicesys.restfulweb.controller.BaseController;

@Controller
@RequestMapping("/member/resource")
@ApiModel(value = "ResourceController", description = "授权资源管理")
public class ResourceController extends BaseController<Long, Resource> {

    @Autowired
    private ResourceService service;


    protected ResourceService getService() {
        return service;
    }

}
