package com.jnetdata.msp.member.businessuser.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by TF on 2019/3/25.
 */


@Data
@ApiModel(description = "移除用户vo")
public class RemoveVo {

    @ApiModelProperty(value = "用户id")
    private List<Long> userids;

    @ApiModelProperty(value = "企业id")
    private String groupid;

}
