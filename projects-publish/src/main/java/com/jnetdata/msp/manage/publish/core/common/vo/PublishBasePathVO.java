package com.jnetdata.msp.manage.publish.core.common.vo;

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
 * @date 2022/8/20 15:06
 */
@ApiModel(value = "发布上下文对象")
@Accessors(chain = true)
@Getter
@Setter
@ToString
public class PublishBasePathVO {

    @ApiModelProperty(value = "发布目录")
    private String dirPub;

    @ApiModelProperty(value = "上传目录")
    private String dirUpload;

    @ApiModelProperty(value = "图片目录")
    private String dirWebpic;

    @ApiModelProperty(value = "预览目录")
    private String dirPreview;

    @ApiModelProperty(value = "上传根目录")
    private String uploadDirBase;

    @ApiModelProperty(value = "发布根目录")
    private String pubDirBase;

    @ApiModelProperty(value = "最终预览目录")
    private String finalPreviewDirPath;

    @ApiModelProperty(value = "最终发布目录")
    private String finalPubDirPath;

    @ApiModelProperty(value = "最终图片目录路径")
    private String finalWebpicPath;

    @ApiModelProperty(value = "最终文件目录路径")
    private String finalWebfilePath;
}
