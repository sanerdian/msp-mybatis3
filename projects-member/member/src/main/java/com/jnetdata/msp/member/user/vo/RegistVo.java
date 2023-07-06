package com.jnetdata.msp.member.user.vo;

import com.jnetdata.msp.core.model.util.PasswordHelper;
import com.jnetdata.msp.member.user.model.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by TF on 2019/3/25.
 */


@Data
@ApiModel(description = "用户")
public class RegistVo {
    @NotNull(message = "手机号码不能为Null")
    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "邮箱号码")
    private String email;

    @NotNull(message = "手机收到系统发送的验证码不能为Null")
    @ApiModelProperty(value = "手机收到系统发送的验证码")
    private String verifyCode;

    @ApiModelProperty(value = "记住密码")
    private Boolean rememberMe;

    @ApiModelProperty(value = "密码")
    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotNull(message = "姓名不能为Null")
    @ApiModelProperty(value = "姓名")
    private String name;

    @NotNull(message = "身份证不能为Null")
    @ApiModelProperty(value = "身份证")
    private String idcard;

    @NotNull(message = "身份证不能为Null")
    @ApiModelProperty(value = "邀请码")
    private String invitationCode;

    @ApiModelProperty(value = "标志")
    private Integer sign;

    public User toUser(){
        User user = new User();
        user.setPassWord(this.password);
        user.setMobile(this.mobile);
        user.setName(this.name == null?"用户"+new Date().getTime():this.name);
        user.setCardId(this.idcard);
        user.setSign(this.sign==null?3:this.sign);
        user.setTrueName(user.getName());
        return user;
    }

}
