package com.jnetdata.msp.docs.document.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Administrator
 */
@Data
@ApiModel(value = "图片上传")
public class PicUploadVo {

    @ApiModelProperty(value = "base64图片内容")
    @NotEmpty(message = "base64图片内容")
    private String base64File;

    @ApiModelProperty(value = "名称")
    @NotEmpty(message = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
    @NotEmpty(message = "描述不能为空")
    private String description;

}
