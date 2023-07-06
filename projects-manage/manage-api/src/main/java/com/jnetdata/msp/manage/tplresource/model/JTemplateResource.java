package com.jnetdata.msp.manage.tplresource.model;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;
import com.alibaba.excel.annotation.ExcelProperty;

import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

/**
 * <p>
 * 模板资源(js,css,img)
 * </p>
 *
 * @author zyj
 * @since 2022-07-12
 */
@Data
@TableName("j_template_resource")
@ApiModel(value = "JTemplateResource对象", description = "模板资源(js,css,img)")
public class JTemplateResource extends BaseEntity implements EntityId<Long>  {

    private static final long serialVersionUID=1L;

    @ExcelProperty(value = "名称")
    @ApiModelProperty(value = "名称")
    @TableField("TITLE")
    private String title;
    @ExcelProperty(value = "后缀")
    @ApiModelProperty(value = "后缀")
    @TableField("ext")
    private String ext;
    @ExcelProperty(value = "描述")
    @ApiModelProperty(value = "描述")
    @TableField("RDESC")
    private String rdesc;
    @ExcelProperty(value = "内容")
    @ApiModelProperty(value = "内容")
    @TableField("CONTENT")
    private String content;
    @ExcelProperty(value = "路径")
    @ApiModelProperty(value = "路径")
    @TableField("PATH")
    private String path;
    @ExcelProperty(value = "站点名称")
    @ApiModelProperty(value = "站点名称")
    @TableField("SITENAME")
    private String sitename;
    @ExcelProperty(value = "类型")
    @ApiModelProperty(value = "类型(js,css,img)")
    @TableField("TYPE")
    private String type;
    @ApiModelProperty(value = "可视化类型(1:组件,2:产品)")
    @ExcelProperty(value = "可视化类型")
    @TableField("VISUAL_TYPE")
    private Integer visualType;
    @ApiModelProperty(value = "模板id")
    @ExcelProperty(value = "模板id")
    @TableField("TEMPLATE_ID")
    private Long templateId;
    @ApiModelProperty(value = "模板名称")
    @ExcelProperty(value = "模板名称")
    @TableField("TEMPLATE_NAME")
    private String templateName;
    @ApiModelProperty(value = "完整发布路径")
    @ExcelProperty(value = "完整发布路径")
    @TableField("FULL_PATH")
    private String fullPath;
    @ApiModelProperty(value = "发布地址")
    @ExcelProperty(value = "发布地址")
    @TableField("PUB_URL")
    private String pubUrl;

    @ExcelProperty(value = "主键id")
    @ApiModelProperty(value = "主键id")
    @JSONField(serializeUsing = org.thenicesys.fastjson.serializer.ToStringSerializer.class)
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "删除状态")
    @TableField("DOCSTATUS")
    private Integer docstatus;
    @ApiModelProperty(value = "站点id")
    @TableField("SITEID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long siteid;
    @ApiModelProperty(value = "发布时间")
    @TableField("DOCPUBTIME")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date docpubtime;
    @ApiModelProperty(value = "状态(0:未发布,1:已发布)")
    @TableField("STATUS")
    private Integer status;

    @TableField(exist = false)
    private List<Long> pubIds;
    @TableField(exist = false)
    private String andOr;
    @TableField(exist = false)
    private Integer synctype;

    public void putVisualTypeByString(String visualType){
        if(visualType == null) return;
        this.visualType = visualType.equals("module")?1:2;
    };


}
