package com.jnetdata.msp.resources.picture.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import javax.validation.constraints.NotEmpty;

/**
 * Created by Administrator on 2019/9/26.
 */
@Data
@TableName("watermarkinfo")
@ApiModel(value = "水印配置实体类",description = "水印配置")
public class Watermark extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "ID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "COMPRESSRESO")
    @NotEmpty(message = "压缩比例")
    @ApiModelProperty(value = "压缩比例")
    private String compress;

    @TableField(value = "CSIZE")
    @NotEmpty(message = "压缩尺寸")
    @ApiModelProperty(value = "压缩尺寸")
    private String csize;

    @TableField(value = "INFOSIGN")
    @NotEmpty(message = "是否抽取信息标识")
    @ApiModelProperty(value = "是否抽取信息标识")
    private int infoSign ;

    @TableField(value = "SUPFORM")
    @NotEmpty(message = "支持格式")
    @ApiModelProperty(value = "支持格式")
    private String supForm;

    @TableField(value = "UPSIZE")
    @NotEmpty(message = "上传大小")
    @ApiModelProperty(value = "上传大小")
    private double upsize;

    @TableField(value = "COPYRIGHT")
    @NotEmpty(message = "默认版权")
    @ApiModelProperty(value = "默认版权")
    private String copyRight;

    @TableField(value = "SUMMARYSIGN")
    @NotEmpty(message = "是否汇总子栏目")
    @ApiModelProperty(value = "是否汇总子栏目")
    private int summarySign;

    @TableField(value = "WATERMARKSIGN")
    @NotEmpty(message = "是否添加水印")
    @ApiModelProperty(value = "是否添加水印")
    private int watermarkSign;

    @TableField(value = "CONTENT")
    @NotEmpty(message = "内容")
    @ApiModelProperty(value = "内容")
    private String content;

    @TableField(value = "RED")
    @NotEmpty(message = "红")
    @ApiModelProperty(value = "红")
    private int red;

    @TableField(value = "GREEN")
    @NotEmpty(message = "绿")
    @ApiModelProperty(value = "绿")
    private int green;

    @TableField(value = "BLUE")
    @NotEmpty(message = "蓝")
    @ApiModelProperty(value = "蓝")
    private int blue;

    @TableField(value = "LOCATION")
    @NotEmpty(message = "位置")
    @ApiModelProperty(value = "位置")
    private int location;

    @TableField(value = "ALPHA")
    @NotEmpty(message = "透明度")
    @ApiModelProperty(value = "透明度")
    private int alpha;

    @TableField(value = "PICALPHA")
    @NotEmpty(message = "图片透明度")
    @ApiModelProperty(value = "图片透明度")
    private float picalpha;

    @TableField(value = "FONTSIZE")
    @NotEmpty(message = "字体大小")
    @ApiModelProperty(value = "字体大小")
    private int fontSize;

    @TableField(value = "INPUTFONT")
    @NotEmpty(message = "字体")
    @ApiModelProperty(value = "字体")
    private String inputFont;

    @TableField(value = "WMKPATH")
    @NotEmpty(message = "水印图片路径")
    @ApiModelProperty(value = "水印图片路径")
    private String wmkPath;

    @TableField(value = "WMKSIGN")
    @NotEmpty(message = "水印类型")
    @ApiModelProperty(value = "水印类型")
    private String wmkSign;

    @TableField(value = "NAME")
    @NotEmpty(message = "名")
    @ApiModelProperty(value = "名")
    private String name;

    @TableField(value = "ISCOMPRESS")
    @NotEmpty(message = "压缩比例设置保存")
    @ApiModelProperty("压缩比例设置保存")
    private Integer isCompress;

    @TableField(value = "CPSW")
    @NotEmpty(message = "压缩宽")
    @ApiModelProperty("压缩宽")
    private Integer cpsW;

    @TableField(value = "CPSH")
    @NotEmpty(message = "压缩高")
    @ApiModelProperty("压缩高")
    private Integer cpsH;

}
