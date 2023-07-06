package com.jnetdata.msp.zsk.unit.model;

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
 * 主题词-机构名录库
 * </p>
 *
 * @author zyj
 * @since 2021-09-13
 */
@Data
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
@TableName("JMETA_UNIT")
@ApiModel(value = "Unit对象", description = "主题词-机构名录库")
public class Unit extends BaseEntity implements EntityId<Long>  {

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
    @ExcelProperty(value = "主题词")
    @ApiModelProperty(value = "主题词")
    @TableField("WORDNAME")
    private String wordname;
    @ExcelProperty(value = "BZ")
    @ApiModelProperty(value = "BZ")
    @TableField("BZ")
    private String bz;
    @ExcelProperty(value = "p_category")
    @ApiModelProperty(value = "p_category")
    @TableField("P_CATEGORY")
    private String pCategory;
    @ExcelProperty(value = "词条来源")
    @ApiModelProperty(value = "词条来源")
    @TableField("SOURCE_TERMS")
    private String sourceTerms;
    @ExcelProperty(value = "FILTE")
    @ApiModelProperty(value = "FILTE")
    @TableField("FILTE")
    private String filte;
    @ExcelProperty(value = "searchOrder")
    @ApiModelProperty(value = "searchOrder")
    @TableField("SEARCHORDER")
    private String searchorder;
    @ExcelProperty(value = "recommend")
    @ApiModelProperty(value = "recommend")
    @TableField("RECOMMEND")
    private String recommend;
    @ExcelProperty(value = "stopword")
    @ApiModelProperty(value = "stopword")
    @TableField("STOPWORD")
    private String stopword;
    @ExcelProperty(value = "cxfl")
    @ApiModelProperty(value = "cxfl")
    @TableField("CXFL")
    private String cxfl;
    @ExcelProperty(value = "主题词同义词")
    @ApiModelProperty(value = "主题词同义词")
    @TableField("TONGYICI")
    private String tongyici;
    @ExcelProperty(value = "英文同义词")
    @ApiModelProperty(value = "英文同义词")
    @TableField("ENTONGYICI")
    private String entongyici;
    @ExcelProperty(value = "英文缩写")
    @ApiModelProperty(value = "英文缩写")
    @TableField("SUOXIE")
    private String suoxie;
    @ExcelProperty(value = "主题词拼音")
    @ApiModelProperty(value = "主题词拼音")
    @TableField("PINYIN")
    private String pinyin;
    @ExcelProperty(value = "主题同音词")
    @ApiModelProperty(value = "主题同音词")
    @TableField("TONGYINCI")
    private String tongyinci;
    @ExcelProperty(value = "newtongyinci")
    @ApiModelProperty(value = "newtongyinci")
    @TableField("NEWTONGYINCI")
    private String newtongyinci;
    @ExcelProperty(value = "模块名称")
    @ApiModelProperty(value = "模块名称")
    @TableField("FENLEI")
    private String fenlei;


    @TableField(exist = false)
    private List<Long> pubIds;
    @TableField(exist = false)
    private String andOr;
    @TableField(exist = false)
    private Integer synctype;



}
