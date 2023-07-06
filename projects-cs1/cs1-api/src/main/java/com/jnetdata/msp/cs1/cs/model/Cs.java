package com.jnetdata.msp.cs1.cs.model;

import com.baomidou.mybatisplus.annotation.IdType;
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
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

/**
 * <p>
 * 测试
 * </p>
 *
 * @author zyj
 * @since 2023-05-31
 */
@Data
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
@TableName("jmeta_cs")
@ApiModel(value = "Cs对象", description = "测试")
public class Cs extends BaseEntity implements EntityId<Long>  {

    private static final long serialVersionUID=1L;


    @ExcelProperty(value = "测试1")
    @ApiModelProperty(value = "测试1")
    @TableField("CS1")
    private String cs1;
    @ExcelProperty(value = "cstext")
    @ApiModelProperty(value = "cstext")
    @TableField("CSTEXT")
    private String cstext;
    @ExcelProperty(value = "csdate")
    @ApiModelProperty(value = "csdate")
    @TableField("CSDATE")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date csdate;
    @TableField(exist = false)
    @ExcelIgnore
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date csdateBT2;
    @ExcelProperty(value = "csclass")
    @ApiModelProperty(value = "csclass")
    @TableField("CSCLASS")
    private String csclass;
    @ExcelProperty(value = "cseditor")
    @ApiModelProperty(value = "cseditor")
    @TableField("CSEDITOR")
    private String cseditor;
    @ExcelProperty(value = "csdouble")
    @ApiModelProperty(value = "csdouble")
    @TableField("CSDOUBLE")
    private Double csdouble;
    @TableField(exist = false)
    @ExcelIgnore
    private Double csdoubleBT2;
    @ExcelProperty(value = "wj")
    @ApiModelProperty(value = "wj")
    @TableField("WJ")
    private String wj;
    @ExcelProperty(value = "testtp")
    @ApiModelProperty(value = "testtp")
    @TableField("TESTTP")
    private String testtp;
    @ExcelProperty(value = "testclass")
    @ApiModelProperty(value = "testclass")
    @TableField("TESTCLASS")
    private String testclass;
    @ExcelProperty(value = "主键id")
    @ApiModelProperty(value = "主键id")
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
    private Integer seqencing;
    @TableField("FLOW_ID")
    private String flowId;
    @TableField("FLOW_USER")
    private String flowUser;
    @TableField("QUOTAINFO")
    private String quotainfo;
    @TableField("COPYFROMID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long copyfromid;


    @TableField(exist = false)
    @ExcelIgnore
    private List<Long> pubIds;
    @TableField(exist = false)
    @ExcelIgnore
    private String andOr;
    @TableField(exist = false)
    @ExcelIgnore
    private Integer synctype;



}
