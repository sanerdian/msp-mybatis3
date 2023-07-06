package com.jnetdata.msp.manage.template.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@Data
@TableName("template")
@ApiModel(value = "模板实体类",description = "模板")
public class Template extends BaseEntity implements EntityId<Long> {
    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "TEMPID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("ID")
    private Long id;

    @TableField(value = "SITEID")
    @ApiModelProperty(value = "站点ID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("站点ID")
    private Long siteid;

    @TableField(value = "COLUMNID")
    @ApiModelProperty(value = "栏目ID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("栏目ID")
    private Long columnid;

    @TableField(value = "TEMPNAME")
    @ApiModelProperty(value = "模板名称")
    @ExcelProperty("模板名称")
    private String tempname;

    @TableField(value = "TEMPDESC")
    @ApiModelProperty(value = "模板描述")
    @ExcelProperty("模板描述")
    private String tempdesc;

    @TableField(value = "TEMPEXT")
    @ApiModelProperty(value = "默认扩展名")
    @ExcelProperty("默认扩展名")
    private String tempext;

    @TableField(value = "TEMPTYPE")
    @ApiModelProperty(value = "网页类型")
    @ExcelProperty("网页类型")
    private Integer temptype;

    @TableField(value = "TPLTYPE")
    @ApiModelProperty(value = "模板类型")
    @ExcelProperty("模板类型")
    private Integer tpltype;

    @TableField(value = "OUTPUTFILENAME")
    @ApiModelProperty(value = "输出文件名")
    @ExcelProperty("输出文件名")
    private String outputfilename;

    @TableField(value = "LASTMODIFIEDUSER")
    @ApiModelProperty(value = "最后操作用户")
    @ExcelProperty("最后操作用户")
    private String lastmodifieduser;

    @TableField(value = "LASTMODIFIEDTIME")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    @ApiModelProperty(value = "最后操作时间")
    @ExcelProperty("最后操作时间")
    private Date lastmodifiedtime;

    @TableField(value = "CHANGEHISTORY")
    @ApiModelProperty(value = "修改历史")
    @ExcelProperty("修改历史")
    private String changehistory;

    @TableField(value = "TEMPHTML")
    @ApiModelProperty(value = "模板正文")
    @ExcelProperty("模板正文")
    private String temphtml;

    @TableField(value = "PAGESIZE")
    @ApiModelProperty(value = "页码大小")
    @ExcelProperty("页码大小")
    private Integer pagesize;

    @TableField(value = "DATANUM")
    @ApiModelProperty(value = "要取出的数据量")
    @ExcelProperty("要取出的数据量")
    private Integer datanum;

    @TableField(value = "STARTPOSITION")
    @ApiModelProperty(value = "从何处取数据")
    @ExcelProperty("从何处取数据")
    private Integer startposition;

    @TableField(value = "RIGHTCONTENT")
    @ApiModelProperty(value = "可视化右侧页面HTML代码")
    @ExcelProperty("可视化右侧页面HTML代码")
    private String rightcontent;

    @TableField(value = "THEMECSSNAME")
    @ApiModelProperty(value = "主题CSS文件名字")
    @ExcelProperty("主题CSS文件名字")
    private String themecssname;

    @TableField(value = "THEMECSSCONTENT")
    @ApiModelProperty(value = "主题CSS代码")
    @ExcelProperty("主题CSS代码")
    private String themecsscontent;

    @TableField(value = "BASESTYLECSSNAME")
    @ApiModelProperty(value = "风格组件CSS名字")
    @ExcelProperty("风格组件CSS名字")
    private String basestylecssname;

    @TableField(value = "BASESTYLECSSCONTENT")
    @ApiModelProperty(value = "风格组件CSS代码")
    @ExcelProperty("风格组件CSS代码")
    private String basestylecsscontent;

    @TableField(value = "BASESTYLEJSNAME")
    @ApiModelProperty(value = "风格组件JS名字")
    @ExcelProperty("风格组件JS名字")
    private String basestylejsname;

    @TableField(value = "BASESTYLEJSCONTENT")
    @ApiModelProperty(value = "风格组件JS代码")
    @ExcelProperty("风格组件JS代码")
    private String basestylejscontent;

    @TableField(value = "BASECONTACTCSSNAME")
    @ApiModelProperty(value = "交互组件CSS名字")
    @ExcelProperty("交互组件CSS名字")
    private String basecontactcssname;

    @TableField(value = "BASECONTACTCSSCONTENT")
    @ApiModelProperty(value = "交互组件CSS代码")
    @ExcelProperty("交互组件CSS代码")
    private String basecontactcsscontent;

    @TableField(value = "BASECONTACTJSNAME")
    @ApiModelProperty(value = "交互组件JS名字")
    @ExcelProperty("交互组件JS名字")
    private String basecontactjsname;

    @TableField(value = "BASECONTACTJSCONTENT")
    @ApiModelProperty(value = "交互组件JS代码")
    @ExcelProperty("交互组件JS代码")
    private String basecontactjscontent;

    @TableField(value = "PORTALTYPE")
    @ApiModelProperty(value = "是否是可视化模板 0或null 为不是  1为是")
    @ExcelProperty("是否是可视化模板")
    private Integer portaltype;

    @TableField(value = "STATUS")
    @ApiModelProperty(value = "是可用还是处于回收站或者已被删")
    @ExcelProperty("状态")
    private Integer status;

    @TableField(value = "THUMBNAIL")
    @ApiModelProperty(value = "缩略图")
    @ExcelProperty("缩略图")
    private String thumbnail;

    @TableField(value = "VISUAL_TEMPLATE_ID")
    @ApiModelProperty(value = "可视化产品ID")
    @ExcelProperty("可视化产品ID")
    private String visualTemplateId;


    @TableField(exist = false)
    @ExcelIgnore
    private Date lastmodifiedtimeto;

    @TableField(exist = false)
    @ExcelIgnore
    private Date modifyTimeto;

    @TableField(exist = false)
    @ExcelIgnore
    private Date createTimeto;

}
