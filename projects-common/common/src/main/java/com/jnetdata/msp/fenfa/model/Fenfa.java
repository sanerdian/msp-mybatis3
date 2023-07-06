package com.jnetdata.msp.fenfa.model;

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
import org.springframework.format.annotation.DateTimeFormat;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

/**
 * <p>
 * 分发
 * </p>
 *
 * @author zyj
 * @since 2021-03-16
 */
@Data
@TableName("j_fenfa")
@ApiModel(value = "Fenfa对象", description = "分发")
public class Fenfa extends BaseEntity implements EntityId<Long>  {

    private static final long serialVersionUID=1L;

    @ExcelProperty(value = "主键id")
    @ApiModelProperty(value = "主键id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;
    @TableField("SEQENCING")
    private Integer seqencing;

    @TableField("COLUMNID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long columnid;
    @ExcelProperty(value = "栏目名称")
    @ApiModelProperty(value = "栏目名称")
    @TableField("COLUMNNAME")
    private String columnname;
    @ExcelProperty(value = "同步栏目")
    @ApiModelProperty(value = "同步栏目")
    @TableField("FROMIDS")
    private String fromids;
    @ExcelProperty(value = "同步栏目名称")
    @ApiModelProperty(value = "同步栏目名称")
    @TableField("FROMNAMES")
    private String fromnames;
    @ExcelProperty(value = "开始时间")
    @ApiModelProperty(value = "开始时间")
    @TableField("STARTTIME")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date starttime;
    @TableField(exist = false)
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date starttimeBT2;
    @ExcelProperty(value = "结束时间")
    @ApiModelProperty(value = "结束时间")
    @TableField("ENDTIME")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endtime;
    @TableField(exist = false)
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endtimeBT2;
    @ExcelProperty(value = "文档创建时间")
    @ApiModelProperty(value = "结束时间")
    @TableField("FILETIME")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date filetime;
    @TableField(exist = false)
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date filetimeBT2;
    @ExcelProperty(value = "同步时机(0:新建时,1:修改时,2:发布后同步并且不发布,3:发布后同步且发布)")
    @ApiModelProperty(value = "同步时机(0:新建时,1:修改时,2:发布后同步并且不发布,3:发布后同步且发布)")
    @TableField("SYNCWHILE")
    private String syncwhile;
    @ExcelProperty(value = "sql")
    @ApiModelProperty(value = "sql")
    @TableField("SQLSTR")
    private String sqlstr;
    @ExcelProperty(value = "同步模式(0:复制,1:引用,2:镜像)")
    @ApiModelProperty(value = "同步模式(0:复制,1:引用,2:镜像)")
    @TableField("SYNCTYPE")
    private Integer synctype;
}
