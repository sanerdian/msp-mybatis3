package com.jnetdata.msp.zdff.publishdistribution.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;
import java.util.List;


@Data
@TableName("jmeta_publishdistribution")
@ApiModel(value = "站点分发实体", description = "")
public class PublishdistriBution implements EntityId<Long> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**注意
	 * id				主键
	 * ,crTime			创建时间
	 * ,crUser			创建人
	 * ,crNumber		创建人id
	 * ,operTime		操作时间
	 * ,operUser		操作人
	 * 继承自DataEntity
	 * 此外 int,long等类型一律使用其包装类，Integer,Long,以避免默认值0的问题
	 * 注意,以上的字段由于是继承,均不需要再次定义
	 * 	id字段对应的是本表的主键,也毋需定义(单字段主键的时候)
	 *
	 */

//	public PublishdistriBution(){
//		super();
//	}
//
//	//有参构造方法是为了不自动生成id
//	public PublishdistriBution(boolean s){
//		super(s);
//	}
	@TableField("COLUMNID")
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long columnid;

	@TableField("DOCSTATUS")
	private Integer docstatus;

	@TableField("QUOTAINFO")
	private String quotainfo;

	//站点ID
	@TableField("DOCCHANNELID")
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long docchannelid;

	@ExcelProperty(value = "唯一ID")
	@ApiModelProperty(value = "唯一ID")
	@TableField("ID")
	private Long id;

	@ExcelProperty(value = "站点ID")
	@ApiModelProperty(value = "站点ID")
	@TableField("SITEID")
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long siteid;
//	//分发类型
//	@ApiModelProperty(value = "分发类型描述")
//	@ExcelProperty(value = "分发类型描述")
//	@TableField("TARGETTYPEDESC")
//	private String targetTypeDesc;
	//分发类型
	@ApiModelProperty(value = "分发类型")
	@ExcelProperty(value = "分发类型")
	@TableField("TARGETTYPE")
	private Integer targetType;
	//分发服务器地址
	@ApiModelProperty(value = "分发服务器地址")
	@ExcelProperty(value = "分发服务器地址")
	@TableField("TARGETSERVER")
	private String targetServer;
	//登陆用户名
	@ApiModelProperty(value = "登陆用户名")
	@ExcelProperty(value = "登陆用户名")
	@TableField("LOGINUSER")
	private String loginUser;
	//登陆密码
	@ApiModelProperty(value = "登陆密码")
	@ExcelProperty(value = "登陆密码")
	@TableField("LOGINPWD")
	private String loginPwd;
	//存放路径
	@ApiModelProperty(value = "存放路径")
	@ExcelProperty(value = "存放路径")
	@TableField("DATAPATH")
	private String dataPath;
	//站点状态 默认1 0禁用 1启用
	@ApiModelProperty(value = "站点状态 默认1 0禁用 1启用")
	@ExcelProperty(value = "站点状态 默认1 0禁用 1启用")
	@TableField("STATUS")
	private Integer status;
	//匿名FTP  模式 0不是 1是
	@ApiModelProperty(value = "匿名FTP  模式 0不是 1是")
	@ExcelProperty(value = "匿名FTP  模式 0不是 1是")
	@TableField("ANONYMFTP")
	private Integer anonymFTP;
	//端口号	   默认0
	@ApiModelProperty(value = "端口号   默认0")
	@ExcelProperty(value = "端口号   默认0")
	@TableField("TARGETPORT")
	private Integer targetPort;
	//被动模式 默认1 0禁用 1启用

	@ApiModelProperty(value = "被动模式 默认1 0禁用 1启用")
	@ExcelProperty(value = "被动模式 默认1 0禁用 1启用")
	@TableField("PASSIVEMODE")
	private Integer passiveMode;

//	@ApiModelProperty(value = "创建时间", required = false,hidden =true)
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	@ApiModelProperty(value = "创建时间")
	@ExcelProperty(value = "创建时间")
	@TableField("CRTIME")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date crTime;
//	@ApiModelProperty(value = "创建人用户名", required = false,hidden =true)
	@ApiModelProperty(value = "创建人用户名")
	@ExcelProperty(value = "创建人用户名")
	@TableField("CRUSER")
	private String crUser;

	//修改时间
	@ApiModelProperty(value = "修改时间")
	@ExcelProperty(value = "修改时间")
	@TableField("OPERTIME")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date operTime;

	//修改人用户名
	@ApiModelProperty(value = "修改人用户名")
	@ExcelProperty(value = "修改人用户名")
	@TableField("OPERUSER")
	private String operUser;


	@TableField(exist = false)
	private List<Long> pubIds;
	@TableField(exist = false)
	private String andOr;
	@TableField(exist = false)
	private Integer synctype;

	@TableField(exist = false)
	private String ids;
}
