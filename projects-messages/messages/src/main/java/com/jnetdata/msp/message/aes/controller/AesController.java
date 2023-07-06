package com.jnetdata.msp.message.aes.controller;

import com.jnetdata.msp.util.aes.AesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.web.JsonResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: ZKJW
 * @date: 2019/9/10
 */
@Api(value = "AesController",description = "Aes加密")
@RequestMapping("/aes")
@Controller
public class AesController {


    @PostMapping("/encrypt")
    @ResponseBody
    @ApiOperation("使用AES加密")
    public JsonResult<Boolean> encrypt(@ApiParam(value = "需要加密的信息",name = "data")@RequestParam String data) {
        Map<String,Object> map = new HashMap<>();
        String encryptData = AesUtil.encrypt(data);
        map.put("原文信息",data);
        map.put("密文信息",encryptData);
        map.put("解密信息",AesUtil.decrypt(encryptData));
        return JsonResult.renderSuccess(map);
    }

    @PostMapping("/decrypt")
    @ResponseBody
    @ApiOperation("使用AES解密")
    public JsonResult<Boolean> decrypt(@ApiParam(value = "需要解密的信息",name = "data")@RequestParam String data) {
        Map<String,Object> map = new HashMap<>();
        map.put("密文信息",data);
        map.put("解密信息",AesUtil.decrypt(data));
        return JsonResult.renderSuccess(map);
    }
}
