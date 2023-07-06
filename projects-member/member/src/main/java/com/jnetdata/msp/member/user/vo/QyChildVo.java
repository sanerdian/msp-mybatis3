package com.jnetdata.msp.member.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QyChildVo {
    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "岗位")
    private String job;

    @ApiModelProperty(value = "企业id")
    private Long qyid;
}
