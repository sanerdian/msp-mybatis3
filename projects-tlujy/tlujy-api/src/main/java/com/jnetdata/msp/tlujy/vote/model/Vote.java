package com.jnetdata.msp.tlujy.vote.model;

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
import com.baomidou.mybatisplus.annotation.TableField;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.fastjson.serializer.ToStringSerializer;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author zyj
 * @since 2020-11-12
 */
@Data
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
@TableName("JMETA_VOTE")
@ApiModel(value="Vote对象", description="")
public class Vote extends BaseEntity implements EntityId<Long>  {

    private static final long serialVersionUID = 1L;

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
    private String status;
    @TableField("COMPANYID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long companyid;
    @TableField("WEBSITEID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long websiteid;
    @TableField("COLUMNID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long columnid;
    @TableField("MODIFY_BY")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long modifyBy;
    @TableField("MODIFY_USER")
    private String modifyUser;
    @TableField("SEQENCING")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long seqencing;
    @TableField("MODIFY_TIME")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    @TableField("TITLE")
    private String title;
    @TableField("STARTDATE")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startdate;
    @TableField("ENDDATE")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enddate;
    @TableField("LIMITTYPE")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long limittype;
    @TableField("LIMITCOUNT")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long limitcount;
    @TableField("REMARKS")
    private String remarks;
    @TableField("PHOTO")
    private String photo;
    @TableField("STATE")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long state;
    @TableField("FLOW_ID")
    private String flowId;
    @TableField("FLOW_USER")
    private String flowUser;
    @TableField("PUSHTOUSER")
    private String pushtouser;
    @TableField("PUSHTOCONDITION")
    private String pushtocondition;
    @TableField("PUSHTORANGE")
    private String pushtorange;
    @TableField("PUSHTOGROUP")
    private String pushtogroup;
    @TableField("TUIJIANZHE")
    private String tuijianzhe;
    @TableField("VOTETHEMEID")
    private String votethemeid;
    @TableField("PUSHSQL")
    private String pushsql;


    @TableField(exist = false)
    private String andOr;



}
