package com.jnetdata.msp.metadata.precise.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jnetdata.msp.member.group.model.Groupinfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;
import java.util.List;

@Data
public class SimpleWorkerVo {

    private Long id;


    private String name;

    private String nickName;

    private String trueName;

    /**
     * 密码不能序列化给前端显示
     */
    private String passWord;


    @ExcelProperty("积分")
    private String crEdit;


    @ExcelProperty("地址")
    private String address;



    @ApiModelProperty(value = "电话")
    @ExcelProperty("电话")
    private String tellPhone;

    @ApiModelProperty(value = "手机")
    @ExcelProperty("手机")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    @ExcelProperty("邮箱")
    private String email;


    @ApiModelProperty(value = "身份证号")
    @ExcelProperty("身份证号")
    private String cardId;

    @ApiModelProperty(value = "职务")
    @ExcelProperty("职务")
    private String duties;

    @ApiModelProperty(value = "性别")
    @ExcelProperty("性别")
    private String sex;

    @ApiModelProperty(value = "民族")
    @ExcelProperty("民族")
    private String nation;

    @ApiModelProperty(value = "参会类型")
    @ExcelProperty("参会类型")
    private int isActor;

    @ApiModelProperty(value = "头像url")
    @ExcelProperty("头像url")
    private String headUrl;

    @ApiModelProperty(value = "职位")
    @ExcelProperty("职位")
    private String job;


    @ApiModelProperty("出生日期")
    @ExcelProperty("出生日期")
    private String birthDate;

    @ApiModelProperty("婚姻状况")
    @ExcelProperty("婚姻状况")
    private String marryState;

    @ApiModelProperty("微信")
    @ExcelProperty("微信")
    private String weixin;

    @TableField(value = "QQNUMBER")
    @ApiModelProperty("QQ号")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("QQ号")
    private Long qqNumber;

    @TableField(value = "CONTACTS")
    @ApiModelProperty("备用联系人")
    @ExcelProperty("备用联系人")
    private String contacts;

    @TableField(value = "RESERVEPHONE")
    @ApiModelProperty("备用联系方式")
    @ExcelProperty("备用联系方式")
    private String reservephone;

    @TableField(value = "NATIVEPLACE")
    @ApiModelProperty("籍贯")
    @ExcelProperty("籍贯")
    private String nativeplace;

    @TableField(value = "BIRTHADRESS")
    @ApiModelProperty("户籍地址")
    @ExcelProperty("户籍地址")
    private String birthAddress;

    @TableField(value = "NORMALADRESS")
    @ApiModelProperty("常住地址")
    @ExcelProperty("常住地址")
    private String normalAddress;

    @TableField(value = "SALER")
    @ApiModelProperty("销售")
    @ExcelProperty("销售")
    private String saler;

    @TableField(exist = false)
    @ApiModelProperty(value = "组织")
    @ExcelProperty("组织")
    private String groupName;

    /**
     * 密码不能序号化给前端显示
     */
    @TableField(value = "MDPASSWORD")
    @JSONField(serialize = false)
    @ApiModelProperty(value = "加密密码", hidden = true)
    private String mdPassWord;

    @TableField(value="REGTIME")
    @ApiModelProperty(value="注册时间")
    @JSONField(format = "yyyy-MM-dd")
    private Date regTime;

    @TableField(value="LOGINTIME")
    @ApiModelProperty(value="登陆时间")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    private Date loginTime;

    @TableField(value="LOGOUTTIME")
    @ApiModelProperty(value="登出时间")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    private Date logoutTime;

    @TableField(value = "STATUS")
    @ApiModelProperty(value = "用户状态")
    private int status;

    @TableField(value="LASTLOGINTIME")
    @ApiModelProperty(value="最后登录时间")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    private Date lastLoginTime;

    @TableField(value = "LASTLOGINIP")
    @ApiModelProperty(value = "登录ip")
    private String loginIp;

//    @TableField(value = "`desc`")
//    @ApiModelProperty(value = "描述")
//    private String desc;

    @TableField(value = "SIGN")
    @ApiModelProperty(value = "用户标识")
    private Integer sign;

    @TableField(value = "TIMES")
    @ApiModelProperty("登录次数")
    private Long times;

    @TableField(value = "IDCARDA")
    @ApiModelProperty("身份证正面")
    private String idcardA;

    @TableField(value = "IDCARDB")
    @ApiModelProperty("身份证背面")
    private String idcardB;

    @TableField(value = "DIPLOMAA")
    @ApiModelProperty("毕业证")
    private String diplomaA;

    @TableField(value = "DIPLOMAB")
    @ApiModelProperty("学位证")
    private String diplomaB;

    @TableField(value = "OTHERCARD")
    @ApiModelProperty("其他证件")
    private String otherCard;

    @TableField(value = "ISENTRY")
    @ApiModelProperty("入离职状态")
    private Integer isEntry ;

    @TableField(value = "LEADERID")
    @ApiModelProperty("直属领导")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long leaderId ;

    @TableField(value = "POWERSTATE")
    @ApiModelProperty("权限状态:永久用户：空/0，临时权限：1，权限到期2")
    private int powerState;

    @TableField(value = "STOPSTATE")
    @ApiModelProperty("停用状态:正常：空/0，已停用：1")
    private int stopState;

    @TableField(value="POWERDATE")
    @ApiModelProperty(value="权限时间")
    @JSONField(format = "yyyy-MM-dd")
    private Date powerDate;

    @TableField(value = "OPENID_WX")
    @ApiModelProperty("openid for 微信")
    private String openidWx;

    @TableField(value = "OPENID_WB")
    @ApiModelProperty("openid for 微博")
    private String openidWb;

    @TableField(value = "OPENID_QQ")
    @ApiModelProperty("openid for qq")
    private String openidQq;

    @TableField(value = "OPENID_APPLE")
    @ApiModelProperty("openid for 苹果")
    private String openidApple;

    @TableField(value = "QQNAME")
    @ApiModelProperty("qq昵称")
    private String qqname;
    @ApiModelProperty("微信昵称")
    private String wxname;

    @ApiModelProperty("微博昵称")
    private String wbname;

    @ApiModelProperty("apple昵称")
    private String applename;

    @ApiModelProperty("邀请码")
    private String invitationCode;

    @ApiModelProperty("企业id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long businessId;

    @ApiModelProperty("错误次数")
    private int locktimes;




    @TableField(exist = false)
    @ApiModelProperty(value = "是否为组长")
    private Integer isLeader;

    @TableField(exist = false)
    private List<Long> ids;

    @TableField(exist = false)
    @ApiModelProperty(value = "角色用户Id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long roleUserId;

    @TableField(exist = false)
    @ApiModelProperty(value = "组织角色Id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long grpUserId;

    @TableField(exist = false)
    @ApiModelProperty(value = "角色Id")
    private Long roleId;

    @TableField(exist = false)
    @ApiModelProperty(value = "组织id")
    private Long groupId;

    @TableField(exist = false)
    @ApiModelProperty(value = "角色")
    private String roleName;

    @TableField(exist = false)
    private String roleIds;

    @TableField(exist = false)
    private String groupIds;

    @TableField(exist = false)
    @ApiModelProperty(value = "机构")
    private String companyName;

    @TableField(exist = false)
    @ApiModelProperty(value = "组织")
    private Groupinfo groupinfo;
}
