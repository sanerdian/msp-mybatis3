package com.jnetdata.msp.manage.template.model;

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

import java.util.Date;

@Data
@TableName("tabletemplate")
@ApiModel(value = "元数据模板实体类",description = "元数据模板")
public class TableTemplate extends BaseEntity implements EntityId<Long> {
    @ApiModelProperty(value = "主键ID", hidden = true)
    @TableId(value = "TEMPID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "TEMPNAME")
    @ApiModelProperty(value = "模板名称")
    private String tempname;

    @TableField(value = "TEMPDESC")
    @ApiModelProperty(value = "模板描述")
    private String tempdesc;

    @TableField(value = "TEMPEXT")
    @ApiModelProperty(value = "默认扩展名")
    private String tempext;

    @TableField(value = "TEMPTYPE")
    @ApiModelProperty(value = "模板类型")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long temptype;

    @TableField(value = "OUTPUTFILENAME")
    @ApiModelProperty(value = "输出文件名")
    private String outputfilename;

    @TableField(value = "LASTMODIFIEDUSER")
    @ApiModelProperty(value = "最后操作用户")
    private String lastmodifieduser;

    @TableField(value = "LASTMODIFIEDTIME")
    @ApiModelProperty(value = "最后操作时间")
    private Date lastmodifiedtime;

    @TableField(value = "CHANGEHISTORY")
    @ApiModelProperty(value = "修改历史")
    private String changehistory;

    @TableField(value = "TEMPHTML")
    @ApiModelProperty(value = "模板正文")
    private String temphtml;

    @TableField(value = "PAGESIZE")
    @ApiModelProperty(value = "页码大小")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long pagesize;

    @TableField(value = "DATANUM")
    @ApiModelProperty(value = "要取出的数据量")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long datanum;

    @TableField(value = "STARTPOSITION")
    @ApiModelProperty(value = "从何处取数据")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long startposition;

    @TableField(value = "RIGHTCONTENT")
    @ApiModelProperty(value = "可视化右侧页面HTML代码")
    private String rightcontent;

    @TableField(value = "THEMECSSNAME")
    @ApiModelProperty(value = "主题CSS文件名字")
    private String themecssname;

    @TableField(value = "THEMECSSCONTENT")
    @ApiModelProperty(value = "主题CSS代码")
    private String themecsscontent;

    @TableField(value = "BASESTYLECSSNAME")
    @ApiModelProperty(value = "风格组件CSS名字")
    private String basestylecssname;

    @TableField(value = "BASESTYLECSSCONTENT")
    @ApiModelProperty(value = "风格组件CSS代码")
    private String basestylecsscontent;

    @TableField(value = "BASESTYLEJSNAME")
    @ApiModelProperty(value = "风格组件JS名字")
    private String basestylejsname;

    @TableField(value = "BASESTYLEJSCONTENT")
    @ApiModelProperty(value = "风格组件JS代码")
    private String basestylejscontent;

    @TableField(value = "BASECONTACTCSSNAME")
    @ApiModelProperty(value = "交互组件CSS名字")
    private String basecontactcssname;

    @TableField(value = "BASECONTACTCSSCONTENT")
    @ApiModelProperty(value = "交互组件CSS代码")
    private String basecontactcsscontent;

    @TableField(value = "BASECONTACTJSNAME")
    @ApiModelProperty(value = "交互组件JS名字")
    private String basecontactjsname;

    @TableField(value = "BASECONTACTJSCONTENT")
    @ApiModelProperty(value = "交互组件JS代码")
    private String basecontactjscontent;

    @TableField(value = "PORTALTYPE")
    @ApiModelProperty(value = "是否是可视化模板 0或null 为不是  1为是")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long portaltype;

    @TableField(value = "STATUS")
    @ApiModelProperty(value = "是可用还是处于回收站或者已被删")
    private int status;

    @TableField(value = "CRUSER")
    @ApiModelProperty(value = "创建人")
    private String cruser;

    @TableField(value = "CRTIME")
    @ApiModelProperty(value = "创建时间")
    private String crtime;

    @TableField(value = "CRNUMBER")
    @ApiModelProperty(value = "创建人ID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long crnumber;

}
