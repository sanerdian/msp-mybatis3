package com.jnetdata.msp.member.user.vo;

import com.jnetdata.msp.core.model.util.PasswordHelper;
import com.jnetdata.msp.member.user.model.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信，QQ OpenId登录
 * @author Administrator
 */
@Data
@ApiModel(value = "(微信、QQ)OpenId登录")
public class ThirdPartyLoginVo {

    @ApiModelProperty(value="微信: wx, QQ: qq, 微博: wb, 苹果: Apple")
    private String type;

    /**
     * openId
     */
    @ApiModelProperty(value = "openId")
    private String openId;

    /**
     * 如下信息用于验证openId
     * 目前暂时不用
     */
//    @ApiModelProperty(value = "OAuth2 code")
//    private String code;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    private String personName;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private String sex;

    /**
     * 个人或企业
     */
    @ApiModelProperty(value = "用户类型(个人:3,企业:21)")
    private Integer sign;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像urlPic")
    private String urlPic;

    public static final String TYPE_OPENID_WEIXIN = "WEXIN_OPENID";
    public static final String TYPE_OPENID_QQ = "QQ_OPENID";

    public String getType(){
        return type.toUpperCase();
    }

    public User toUser(){
        User user = new User();
        user.setSex(this.sex);
        user.setHeadUrl(this.urlPic);
        user.setNickName(this.personName);
        String password = new Date().getTime() + "";
        user.setPassWord(password);
        user.setMdPassWord(PasswordHelper.doEncryptedPassword(password));
        user.setSign(this.sign==null?3:this.sign);
        user.setName(type+new Date().getTime());
        if(this.type.equals("WX")){
            user.setOpenidWx(this.openId);
            user.setWxname(this.personName);
        }else if(this.type.equals("WB")){
            user.setOpenidWb(this.openId);
            user.setWbname(this.personName);
        }else{
            user.setOpenidQq(this.openId);
            user.setQqname(this.personName);
        }
        return user;
    }

}
