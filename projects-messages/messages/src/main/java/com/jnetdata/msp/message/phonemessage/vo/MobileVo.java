package com.jnetdata.msp.message.phonemessage.vo;

import com.jnetdata.msp.limit.LimitRequestidentification;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "手机号码")
public class MobileVo implements LimitRequestidentification {

    @ApiModelProperty(value = "手机号码")
    private String mobile;


    @Override
    public String identification() {
        return mobile;
    }
}
