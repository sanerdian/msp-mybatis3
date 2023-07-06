package com.jnetdata.msp.util.gmapnetcache.model;

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
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

/**
 * <p>
 * Gmapnetcache
 * </p>
 *
 * @author zyj
 * @since 2023-04-12
 */
@Data
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
@TableName("jmeta_gmapnetcache")
@ApiModel(value = "Gmapnetcache对象", description = "Gmapnetcache")
public class Gmapnetcache extends BaseEntity implements EntityId<Long>  {

    private static final long serialVersionUID=1L;


    @ExcelProperty(value = "Type")
    @ApiModelProperty(value = "Type")
    @TableField("TYPE")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long type;
    @ExcelProperty(value = "Zoom")
    @ApiModelProperty(value = "Zoom")
    @TableField("ZOOM")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long zoom;
    @ExcelProperty(value = "X")
    @ApiModelProperty(value = "X")
    @TableField("X")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long x;
    @ExcelProperty(value = "Y")
    @ApiModelProperty(value = "Y")
    @TableField("Y")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long y;
    @ExcelProperty(value = "Tile")
    @ApiModelProperty(value = "Tile")
    @TableField("TILE")
    private byte[] tile;
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
