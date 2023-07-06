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

import java.util.Date;
import java.util.List;


@Data
@TableName("chnldocinfo")
@ApiModel(value = "文档关系表", description = "")
public class Chnldoc implements EntityId<Long> {

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
	@ExcelProperty(value = "唯一ID")
	@ApiModelProperty(value = "唯一ID")
	@TableField("RECID")
	private Long id;
	//栏目id
	@ApiModelProperty(value = "分发类型")
	@ExcelProperty(value = "分发类型")
	@TableField("CHNLID")
	private Long chnlId;
	//文档Id
	@ApiModelProperty(value = "文档Id")
	@TableField("DOCID")
	private Long docId;
	//文档排序
	@ApiModelProperty(value = "文档Id")
	@TableField("DOCORDER")
	private Integer docOrder;
	//文档状态
	@ApiModelProperty(value = "文档状态")
	@TableField("DOCSTATUS")
	private Integer docStatus;
	//文档发布时间
	@ApiModelProperty(value = "文档发布时间")
	@ExcelProperty(value = "文档发布时间")
	@TableField("DOCPUBTIME")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date docPubTime;
	//文档发布地址
	@ApiModelProperty(value = "文档发布地址")
	@TableField("DOCPUBURL")
	private String docPubUrl;
	//从哪个版本恢复

	@ApiModelProperty(value = "从哪个版本恢复")
	@TableField("DOCORDERPRI")
	private Integer docOrderPri;
	//失效时间
	@ApiModelProperty(value = "失效时间")
	@ExcelProperty(value = "失效时间")
	@TableField("INVALIDTIME")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date InvalidTime;
	//从哪个版本恢复
	@ApiModelProperty(value = "从哪个版本恢复")
	@TableField("MODAL")
	private Integer modal;
	//文档撰写时间
	@ApiModelProperty(value = "文档撰写时间")
	@ExcelProperty(value = "文档撰写时间")
	@TableField("DOCRELTIME")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date docRelTime;

	//栏目名
	@ApiModelProperty(value = "栏目名")
	@TableField("DOCCHANNEL")
	private String docChannel;
	//文档标记

	@ApiModelProperty(value = "文档标记")
	@TableField("DOCFLAG")
	private String docFlag;
	//站点Id

	@ApiModelProperty(value = "站点Id")
	@TableField("SITEID")
	private Long siteId;
	//元数据id
	@ApiModelProperty(value = "元数据id")
	@TableField("TABLEID")
	private Long tableId;
	//文档标题
	@ApiModelProperty(value = "文档标题")
	@TableField("DOCTITLE")
	private String docTitle;
	//12306人才招聘新增，可废弃
	@ApiModelProperty(value = "12306人才招聘新增，可废弃")
	@TableField("PUBSTATUS")
	private Integer pubStatus;
	//链接地址

	@ApiModelProperty(value = "链接地址")
	@TableField("LINKURL")
	private String linkUrl;
	//xxx

	@ApiModelProperty(value = "xxx")
	@TableField("CLASSINFOID")
	private Integer classInfoId;
	//检索文档开始时间

	@ApiModelProperty(value = "检索文档开始时间")
	@TableField("STARTTIME")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	//检索文档结束时间

	@ApiModelProperty(value = "检索文档结束时间")
	@TableField("ENDTIME")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	//检索类型

	@ApiModelProperty(value = "检索类型")
	@TableField("SEARCHTYPE")
	private Integer searchType;
	//检索关键词

	@ApiModelProperty(value = "检索关键词")
	@TableField("KEYWORD")
	private String keyWord;
	//栏目名称

	@ApiModelProperty(value = "栏目名称")
	@TableField("CHNLNAME")
	private String chnlName;
	//检索类型

	@ApiModelProperty(value = "检索类型")
	@TableField("TYPE")
	private Integer type;
	//元数据表名称

	@ApiModelProperty(value = "元数据表名称")
	@TableField("TABLENAME")
	private String tableName;

	//审核状态

	@ApiModelProperty(value = "审核状态")
	@TableField("REVIEWSTATUS")
	private Integer reviewStatus;
	//流程实例ID

	@ApiModelProperty(value = "流程实例ID")
	@TableField("PROCINSTID")
	private String procinstid;

	//流程节点
	@ApiModelProperty(value = "流程节点")
	@TableField("ACTIVITINODE")
	private String activitiNode;

	//流程modelID
	@ApiModelProperty(value = "流程modelID")
	@TableField("MODELID")
	private String modelId;

	//流程节点处理人
	@ApiModelProperty(value = "流程节点处理人")
	@TableField("NODEDEALER")
	private String nodeDealer;

	//流程历史处理人
	@ApiModelProperty(value = "流程历史处理人")
	@TableField("HISTORYDEALER")
	private String historyDealer;

	//文档预览地址
	@ApiModelProperty(value = "文档预览地址")
	@TableField("DOCPERVURL")
	private String docpervurl;

	@TableField(exist = false)
	private List<Long> pubIds;
	@TableField(exist = false)
	private String andOr;
	@TableField(exist = false)
	private Integer synctype;

}
