package com.jnetdata.msp.member.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by TF on 2019/3/25.
 */


@Data
@ApiModel(description = "用户")
public class LoginVo{

    @ApiModelProperty(value = "用户名称")
    private String name;

    @ApiModelProperty(value = "真实密码")
    private String passWord;

    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "真实密码")
    private Boolean rememberMe;

}
