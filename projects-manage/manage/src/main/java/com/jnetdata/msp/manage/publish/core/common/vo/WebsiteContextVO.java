package com.jnetdata.msp.manage.publish.core.common.vo;

import com.jnetdata.msp.manage.site.model.Site;
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
@ApiModel(value = "发布站点上下文对象")
@Accessors(chain = true)
@Getter
@Setter
@ToString
public class WebsiteContextVO {

    @ApiModelProperty(value = "栏目对象")
    private Site site;

    @ApiModelProperty(value = "概览模板")
    private Template outLineTemplate;

    @ApiModelProperty(value = "其他概览对象")
    private Template otherTemplate;
}
