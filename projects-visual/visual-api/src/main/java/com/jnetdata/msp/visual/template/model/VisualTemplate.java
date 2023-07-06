package com.jnetdata.msp.visual.template.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.jnetdata.msp.core.model.BaseEntity;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author zyj
 * @since 2020-03-23
 */
@Data
@TableName("visual_template")
@ApiModel(value="VisualTemplate对象", description="")
public class VisualTemplate extends BaseEntity implements EntityId<Long> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableId(value = "ID", type = IdType.AUTO)
    
    private Long id;

    @TableField("CSS_CODE")
    @ApiModelProperty("css")
    private String cssCode;


    @TableField("FREE_HTML_CODE")
    @ApiModelProperty("自定义HTML")
    private String freeHtmlCode;

    
    @TableField("EXTE_NAME")
    @ApiModelProperty("扩展名")
    private String exteName;

    @TableField("HTML_CODE")
    @ApiModelProperty("可以编辑的html代码")
    private String htmlCode;

    @TableField("HTML_CODE_PURE")
    @ApiModelProperty("用于发布的html代码")
    private String htmlCodePure;

    @TableField("IMG")
    private String img;

    
    @TableField("IND_CLASS")
    private String indClass;

    
    @TableField("JS_CODE")
    @ApiModelProperty("js")
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

    @TableField("BUSINESS_TYPE")
    @ApiModelProperty("行业分类")
    private String businessType;

    @TableField("MJ_TYPE")
    @ApiModelProperty("媒介应用")
    private String mjType;


    @TableField("STATUS")
    @ApiModelProperty("状态(0：现行，1：下架)")
    private int status;


    @TableField("TYPE")
    @ApiModelProperty("分类(0：概览，1：细览，2：嵌套)")
    private Integer type;


    @TableField("TEMPLATE_TYPE")
    @ApiModelProperty("模板类型")
    private Long templateType;

    @TableField("module_count")
    @ApiModelProperty("组件数量")
    private Long moduleCount;

    @TableField(exist = false)
    @ApiModelProperty("组件列表")
    private List<RelationModuleTemplate> moduleList;


}
