package com.jnetdata.msp.cs1.middle.model;

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
 * 中间表
 * </p>
 *
 * @author zyj
 * @since 2023-05-31
 */
@Data
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
@TableName("jmeta_middle")
@ApiModel(value = "Middle对象", description = "中间表")
public class Middle extends BaseEntity implements EntityId<Long>  {

    private static final long serialVersionUID=1L;


    @ExcelProperty(value = "标题")
    @ApiModelProperty(value = "标题")
    @TableField("TITLE")
    private String title;
    @ExcelProperty(value = "新闻内容")
    @ApiModelProperty(value = "新闻内容")
    @TableField("XINWENNEIRONG2")
    private String xinwenneirong2;
    @ExcelProperty(value = "主题词")
    @ApiModelProperty(value = "主题词")
    @TableField("KEY_WORDS")
    private String keyWords;
    @ExcelProperty(value = "URL")
    @ApiModelProperty(value = "URL")
    @TableField("URL")
    private String url;
    @ExcelProperty(value = "新闻图片")
    @ApiModelProperty(value = "新闻图片")
    @TableField("IMAGE_URL")
    private String imageUrl;
    @ExcelProperty(value = "所属板块")
    @ApiModelProperty(value = "所属板块")
    @TableField("FLLJ")
    private String fllj;
    @ExcelProperty(value = "属于来源网站")
    @ApiModelProperty(value = "属于来源网站")
    @TableField("DATA_SOURCE")
    private String dataSource;
    @ExcelProperty(value = "新闻发布时间")
    @ApiModelProperty(value = "新闻发布时间")
    @TableField("PUBLISH_TIME")
    private String publishTime;
    @ExcelProperty(value = "入库时间")
    @ApiModelProperty(value = "入库时间")
    @TableField("CRAWLER_TIME")
    private String crawlerTime;
    @ExcelProperty(value = "阅读数")
    @ApiModelProperty(value = "阅读数")
    @TableField("READ_NUM")
    private String readNum;
    @ExcelProperty(value = "评论数")
    @ApiModelProperty(value = "评论数")
    @TableField("COMMENTS_NUM")
    private String commentsNum;
    @ExcelProperty(value = "带页面格式的内容")
    @ApiModelProperty(value = "带页面格式的内容")
    @TableField("NEIRONG_HTML2")
    private String neirongHtml2;
    @ExcelProperty(value = "摘要")
    @ApiModelProperty(value = "摘要")
    @TableField("ABSTRACT2")
    private String abstract2;
    @ExcelProperty(value = "发布时间原始")
    @ApiModelProperty(value = "发布时间原始")
    @TableField("PUBLISH_TIME_OLD")
    private String publishTimeOld;
    @ExcelProperty(value = "PDF地址")
    @ApiModelProperty(value = "PDF地址")
    @TableField("PDFURL")
    private String pdfurl;
    @ExcelProperty(value = "PDF名字")
    @ApiModelProperty(value = "PDF名字")
    @TableField("PDFNAME")
    private String pdfname;
    @ExcelProperty(value = "语种")
    @ApiModelProperty(value = "语种")
    @TableField("YUZHONG")
    private String yuzhong;
    @ExcelProperty(value = "标签")
    @ApiModelProperty(value = "标签")
    @TableField("TAG")
    private String tag;
    @ExcelProperty(value = "带页面格式的内容")
    @ApiModelProperty(value = "带页面格式的内容")
    @TableField("NEIRONG")
    private String neirong;
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
