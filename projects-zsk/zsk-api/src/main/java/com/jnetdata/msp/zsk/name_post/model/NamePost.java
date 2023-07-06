package com.jnetdata.msp.zsk.name_post.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.fastjson.serializer.ToStringSerializer;
import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

/**
 * <p>
 * 人名职务库
 * </p>
 *
 * @author zyj
 * @since 2021-09-16
 */
@Data
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
@TableName("JMETA_NAME_POST")
@ApiModel(value = "NamePost对象", description = "人名职务库")
public class NamePost extends BaseEntity implements EntityId<Long>  {

    private static final long serialVersionUID=1L;

    @JSONField(serializeUsing = org.thenicesys.fastjson.serializer.ToStringSerializer.class)
    @TableId(value = "ID", type = IdType.AUTO)

    private Long id;
    @TableField("DOCCHANNELID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long docchannelid;
    @TableField("DOCSTATUS")
    private Integer docstatus;
    @TableField("SINGLETEMPKATE")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long singletempkate;
    @TableField("SITEID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long siteid;
    @TableField("DOCVALID")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date docvalid;
    @TableField("DOCPUBTIME")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date docpubtime;
    @TableField("OPERUSER")
    private String operuser;
    @TableField("OPERTIME")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date opertime;
    @TableField("DOCTITLE")
    private String doctitle;
    @TableField("DOCRELTIME")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date docreltime;
    @TableField("DOCPUBURL")
    private String docpuburl;
    @TableField("LINKURL")
    private String linkurl;
    @TableField("CLASSINFOID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long classinfoid;
    @TableField("STATUS")
    private Integer status;
    @TableField("COMPANYID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long companyid;
    @TableField("WEBSITEID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long websiteid;
    @TableField("COLUMNID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long columnid;
    @TableField("SEQENCING")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long seqencing;
    @TableField("FLOW_ID")
    private String flowId;
    @TableField("FLOW_USER")
    private String flowUser;
    @TableField("QUOTAINFO")
    private String quotainfo;
    @TableField("COPYFROMID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long copyfromid;
    @ExcelProperty(value = "人名")
    @ApiModelProperty(value = "人名")
    @TableField("NAME")
    private String name;
    @ExcelProperty(value = "职务")
    @ApiModelProperty(value = "职务")
    @TableField("POST")
    private String post;
    @ExcelProperty(value = "在职单位")
    @ApiModelProperty(value = "在职单位")
    @TableField("COMPANY")
    private String company;
    @ExcelProperty(value = "入库时间")
    @ApiModelProperty(value = "入库时间")
    @TableField("WAREHOUSINGTIME")
    private String warehousingtime;
    @ExcelProperty(value = "文章ID")
    @ApiModelProperty(value = "文章ID")
    @TableField("ARTICLEID")
    private String articleid;
    @ExcelProperty(value = "文章所属系统")
    @ApiModelProperty(value = "文章所属系统")
    @TableField("SYSTEM")
    private String system;


    @TableField(exist = false)
    private List<Long> pubIds;
    @TableField(exist = false)
    private String andOr;
    @TableField(exist = false)
    private Integer synctype;



}
