package com.jnetdata.msp.manage.publish.core.common.vo;

import com.jnetdata.msp.column.model.Channel;
import com.jnetdata.msp.manage.template.model.Template;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/8/9 13:01
 */
@ApiModel(value = "发布栏目上下文对象")
@Accessors(chain = true)
@Getter
@Setter
@ToString
public class ChannelContextVO {

    @ApiModelProperty(value = "栏目对象")
    private Channel channel;

    @ApiModelProperty(value = "概览模板")
    private Template outLineTemplate;

    @ApiModelProperty(value = "细览对象")
    private Template detailTemplate;

    @ApiModelProperty(value = "其他概览对象")
    private Template otherTemplate;
}
