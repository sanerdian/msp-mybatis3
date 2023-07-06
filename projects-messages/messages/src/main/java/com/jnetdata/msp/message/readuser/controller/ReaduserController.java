package com.jnetdata.msp.message.readuser.controller;

import com.jnetdata.msp.core.controller.BaseController;
import io.swagger.annotations.ApiModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/read/user")
@ApiModel(value = "MsgController", description = "消息管理")
public class ReaduserController extends BaseController {

}
