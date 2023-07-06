package com.jnetdata.msp.util;

import com.alibaba.fastjson.JSONObject;
import com.baidu.ueditor.ActionEnter;
import com.jnetdata.msp.core.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ue")
public class UeditorController extends BaseController {

    @RequestMapping(value = "/exec")
    @ResponseBody
    public Object exec(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        String rootPath = request.getRealPath("/");
        String exec = new ActionEnter(request, rootPath).exec();
        JSONObject parse = JSONObject.parseObject(exec);
        return parse;
    }
}
