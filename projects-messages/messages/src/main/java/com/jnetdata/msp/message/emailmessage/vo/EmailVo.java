package com.jnetdata.msp.message.emailmessage.vo;

import com.jnetdata.msp.limit.LimitRequestidentification;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "邮箱号码")
public class EmailVo implements LimitRequestidentification {

    @ApiModelProperty(value = "邮箱号码")
    private String email;


    @Override
    public String identification() {
        return email;
    }
}
