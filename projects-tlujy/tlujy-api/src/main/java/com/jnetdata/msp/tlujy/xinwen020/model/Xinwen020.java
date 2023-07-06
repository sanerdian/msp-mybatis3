package com.jnetdata.msp.tlujy.xinwen020.model;

import java.beans.Transient;
import java.util.Date;
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
 * @since 2020-12-11
 */
@Data
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
@TableName("JMETA_XINWEN020")
@ApiModel(value="Xinwen020对象", description="")
public class Xinwen020 extends BaseEntity implements EntityId<Long>  {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    @JSONField(serializeUsing = ToStringSerializer.class)
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
    @TableField("FLOW_ID")
    private String flowId;
    @TableField("FLOW_USER")
    private String flowUser;
    @TableField("TITLE")
    private String title;
    @TableField("PUSER")
    private String puser;
    @TableField("PTIME")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ptime;

    @TableField("ZHENGWEN")
    private String zhengwen;
    @TableField("SUMMARY")
    private String summary;
    @TableField("HOMETITLE")
    private String hometitle;
    @TableField("SUBTITLE")
    private String subtitle;
    @TableField("DOCSOURCE")
    private String docsource;
    @TableField("DOCAUTHOR")
    private String docauthor;
    @TableField("ISPROHIBITPUBLISH")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long isprohibitpublish;
    @TableField("COPYFLAG")
    private String copyflag;
    @TableField("ISROTATION")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long isrotation;
    @TableField("STATE")
    private String state;
    @TableField("DIANPINGSHU")
    private String dianpingshu;
    @TableField("TUIJIANZHE")
    private String tuijianzhe;
    @TableField("LOGOTU")
    private String logotu;
    @TableField("KEYWORDS")
    private String keywords;
    @TableField("SCSTATE")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long scstate;
    @TableField("PUSHTORANGE")
    private String pushtorange;
    @TableField("COMMENTALLOWED")
    private Integer commentallowed;
    @TableField("COMMENTCHECKED")
    private Integer commentchecked;
    @TableField("COLUMNNAME")
    private String columnname;
    @TableField("PUSHTOGROUP")
    private String pushtogroup;
    @TableField("PUSHTOUSER")
    private String pushtouser;
    @TableField("PUSHTOCONDITION")
    private String pushtocondition;
    @TableField("FROZENTYPE")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long frozentype;
    @TableField("WRITINGTIME")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date writingtime;
    @TableField(exist = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date writingtimeBT2;
    @TableField("SOURCEURL")
    private String sourceurl;
    @TableField("CDNTYPE")
    private String cdntype;
    @TableField("PUSHSQL")
    private String pushsql;
    @TableField("TOPPINGFLAG")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long toppingflag;
    @TableField("TOPPINGORDER")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long toppingorder;
    @TableField("ROTATIONORDER")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long rotationorder;

    @TableField(exist = false)
    private String andOr;



}
