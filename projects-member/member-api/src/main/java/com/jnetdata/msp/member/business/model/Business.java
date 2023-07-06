package com.jnetdata.msp.member.business.model;


import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.jnetdata.msp.core.model.BaseEntity;
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
@TableName("j_business")
@ApiModel(value="企业实体类", description="企业信息")
public class Business extends BaseEntity implements EntityId<Long>  {

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


    @ApiModelProperty(value = "审核状态(1:审核通过,2:审核不通过,其他:审核中)")
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

    
    @ApiModelProperty(value = "企业名称")
    @TableField("NAME")
    private String name;

    
    @ApiModelProperty(value = "法定代表人")
    @TableField("LEGAL_REPRESENTATIVE")
    private String legalRepresentative;


    @ApiModelProperty(value = "法人身份证")
    @TableField("LEGALNUMBER")
    private String legalnumber;
    
    @ApiModelProperty(value = "注册资本")
    @TableField("REGISTERED_CAPITAL")
    private String registeredCapital;

    @ApiModelProperty(value = "纳税人识别号")
    @TableField("TAXPAYERNO")
    private String taxpayerno;

    @ApiModelProperty(value = "联系人")
    @TableField("CONTACTS")
    private String contacts;

    @ApiModelProperty(value = "联系方式")
    @TableField("CONTACTINFORMAT")
    private String contactinformat;

    @ApiModelProperty(value = "营业执照")
    @TableField("BUSINESS_LICENSE")
    private String businessLicense;

    
    @ApiModelProperty(value = "成立日期")
    @TableField("DATE_OF_ESTABLISHMENT")
    private String dateOfEstablishment;

    
    @ApiModelProperty(value = "电话")
    @TableField("TEL")
    private String tel;

    
    @ApiModelProperty(value = "邮箱")
    @TableField("EMAIL")
    private String email;

    
    @ApiModelProperty(value = "营业地址")
    @TableField("ADDRESS")
    private String address;

    @ApiModelProperty(value = "注册地址")
    @TableField("REGISTEREADDRESS")
    private String registereaddress;

    
    @ApiModelProperty(value = "官网")
    @TableField("OFFICIAL_WEBSITE")
    private String officialWebsite;

    
    @ApiModelProperty(value = "曾用名")
    @TableField("FORMER_NAME")
    private String formerName;

    
    @ApiModelProperty(value = "品牌")
    @TableField("BRAND")
    private String brand;

    
    @ApiModelProperty(value = "产品")
    @TableField("PRODUCT")
    private String product;

    
    @ApiModelProperty(value = "英文名")
    @TableField("EN_NAME")
    private String enName;

    
    @ApiModelProperty(value = "人员规模")
    @TableField("STAFF_SIZE")
    private String staffSize;

    
    @ApiModelProperty(value = "组织id")
    @TableField("GROUPID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long groupid;


    @ApiModelProperty(value = "主账户用户id")
    @TableField("USERID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userid;


    @TableField(exist = false)
    private String andOr;



}
