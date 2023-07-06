package com.jnetdata.msp.visual.module.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
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

import java.util.List;

/**
 * 组件定义表
 */
@Data
@TableName("visual_module")
@ApiModel(value="VisualModule对象", description="")
public class VisualModule extends BaseEntity implements EntityId<Long> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @JSONField(serializeUsing = org.thenicesys.fastjson.serializer.ToStringSerializer.class)
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @TableField("CSS_CODE")
    @ApiModelProperty("css代码")
    private String cssCode;

    
    @TableField("EXTE_NAME")
    @ApiModelProperty("扩展名")
    private String exteName;

    
    @TableField("HTML_CODE")
    @ApiModelProperty("html代码")
    private String htmlCode;

    @ExcelProperty(value = "HTML_CODE1")
    @ApiModelProperty(value = "HTML_CODE1")
    @TableField(exist = false)
    private String htmlCode1;

    @TableField("HTML_CODEN")
    @ApiModelProperty("html")
    private String htmlCoden;

    @TableField("HTML_CODENUSE")
    @ApiModelProperty("实际使用的")
    private String htmlCodeUse;
    
    @TableField("IMG")
    private String img;

    
    @TableField("IND_CLASS")
    private String indClass;

    
    @TableField("JS_CODE")
    @ApiModelProperty("js代码")
    private String jsCode;

    
    @TableField("NET_TYPE")
    @ApiModelProperty("网页类型(PC,H5,APP,小程序,大屏")
    private String netType;

    
    @TableField("OUT_FILE_NAME")
    @ApiModelProperty("输出文件名")
    private String outFileName;

    
    @TableField("PRO_DES")
    @ApiModelProperty("描述")
    private String proDes;

    
    @TableField("SCREEN_TYPE")
    private String screenType;

    
    @TableField("TITLE")
    @ApiModelProperty("标题")
    private String title;

    @TableField("MODULE_TYPE")
    @ApiModelProperty("模板类型(index,list,detail)")
    private String moduleType;

    @TableField("FRAME")
    @ApiModelProperty("开发框架(layui,vue,uni-app)")
    private String frame;

    @TableField("VM_TYPE")
    @ApiModelProperty("组件类型")
    private Long vmType;

    @TableField("VC_TYPE")
    @ApiModelProperty("内容类型")
    private Long vcType;

    @TableField(exist = false)
    @ApiModelProperty("类型code")
    private String typeCode;

    
    @TableField("PID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long pid;


    @TableField("STATUS")
    @ApiModelProperty("状态(0：现行，1：下架)")
    private int status;


    @TableField(exist = false)
    private List<VisualModule> children;


    @TableField("TYPE")
    @ApiModelProperty("分类(0：概览，1：细览，2：嵌套)")
    private Integer type;
    

}
