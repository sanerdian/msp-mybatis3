package com.jnetdata.msp.message.phonemessage.vo;
import com.jnetdata.msp.limit.LimitRequestidentification;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
@ApiModel(value = "发送手机短信")
public class SendMessageVo implements LimitRequestidentification {

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "需要发送的短信 xiaox")
    private String message;

    @ApiModelProperty(value = "消息类型")
    private String kindFlg;

    @Override
    public String identification() {
        return mobile;
    }
}
