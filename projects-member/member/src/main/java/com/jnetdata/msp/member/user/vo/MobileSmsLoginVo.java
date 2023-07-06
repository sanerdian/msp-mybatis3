package com.jnetdata.msp.member.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Administrator
 */
@ApiModel(value = "手机短信登录")
@Data
public class MobileSmsLoginVo {

    @NotNull(message = "手机号码不能为Null")
    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @NotNull(message = "手机收到系统发送的验证码不能为Null")
    @ApiModelProperty(value = "手机收到系统发送的验证码")
    private String verifyCode;

    @ApiModelProperty(value = "是否启用rememberMe")
    private Boolean rememberMe;
}
