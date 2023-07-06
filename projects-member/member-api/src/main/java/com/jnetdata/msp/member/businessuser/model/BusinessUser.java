package com.jnetdata.msp.member.businessuser.model;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.core.model.BaseEntity;
import com.jnetdata.msp.member.user.model.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author zyj
 * @since 2020-07-02
 */
@Data
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
@TableName("j_business_user")
@ApiModel(value="BusinessUser对象", description="企业用户")
public class BusinessUser extends BaseEntity implements EntityId<Long>  {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键id")
    @JSONField(serializeUsing = org.thenicesys.fastjson.serializer.ToStringSerializer.class)
    @TableId(value = "ID", type = IdType.AUTO)
    
    private Long id;

    @TableField("DOCCHANNELID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long docchannelid;

    @ApiModelProperty(value = "删除状态")
    @TableField("DOCSTATUS")
    private Integer docstatus;

    
    @TableField("SINGLETEMPKATE")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long singletempkate;

    @ApiModelProperty(value = "站点id")
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

    @ApiModelProperty(value = "状态")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty(value = "所属机构id")
    @TableField("COMPANYID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long companyid;

    @ApiModelProperty(value = "站点id")
    @TableField("WEBSITEID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long websiteid;

    @ApiModelProperty(value = "栏目id")
    @TableField("COLUMNID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long columnid;

    @ApiModelProperty(value = "修改人")
    @TableField("MODIFY_USER")
    private String modifyUser;

    @ApiModelProperty(value = "排序")
    @TableField("SEQENCING")
    private Integer seqencing;

    @ApiModelProperty(value = "工作流id")
    @TableField("FLOW_ID")
    private String flowId;

    @ApiModelProperty(value = "工作流用户")
    @TableField("FLOW_USER")
    private String flowUser;


    @ApiModelProperty(value = "用户id")
    @TableField("USERID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userid;

    
    @ApiModelProperty(value = "组织id")
    @TableField("GROUPID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long groupid;

    

    @TableField(exist = false)
    private String andOr;

    @TableField(exist = false)
    private User user;



}
