package com.jnetdata.msp.metadata.viewfieldinfo.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@Data
@TableName("view_dbfieldinfo")
@ApiModel(value = "视图字段管理实体类",description = "视图字段管理")
public class ViewField implements EntityId<Long> {

	@TableId(value = "VIEWFIELDID", type = IdType.ID_WORKER)
	@ApiModelProperty(value = "id", hidden = true)
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long id;

	@ApiModelProperty(value = "视图名", example = "JMETA_XXX")
	private String viewname;

	@ApiModelProperty(value = "视图id", example = "15608474745058141")
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long viewid;

	@ApiModelProperty(value = "字段名", example = "type")
	private String fieldname;

	@ApiModelProperty(value = "中文名称", example = "类型")
	private String anothername;

	@ApiModelProperty(value = "字段类型", example = "1")
	private Integer fieldtype;

	@ApiModelProperty(value = "数据长度", example = "255")
	private String dblength;

	@ApiModelProperty(value = "默认值", example = "1")
	private String defaultvalue;

	@ApiModelProperty(value = "枚举值", example = "1")
	private String enmvalue;

	@ApiModelProperty(value = "是否允许空值", example = "0")
	private Integer notnull;

	@ApiModelProperty(value = "分类编号", example = "")
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long classid;

	@ApiModelProperty(value = "数据库字段精度", example = "")
	private Integer dbscale;

	@ApiModelProperty(value = "校验器", example = "")
	private String validator;

	@ApiModelProperty(value = "单选或多选项", example = "")
	private Integer radorchk;

	@ApiModelProperty(value = "是否不可编辑", example = "")
	private Integer notedit;

	@ApiModelProperty(value = "是否隐藏", example = "0")
	private Integer hiddenfield;

	@ApiModelProperty(value = "创建人", example = "admin")
	private String cruser;

	@ApiModelProperty(value = "创建时间", example = "2019-06-18 16:56:30")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date crtime;

	@ApiModelProperty(value = "生成java应用代码时间", example = "2019-06-18 16:56:30")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date generatetime;

	@ApiModelProperty(value = "生成状态", example = "0")
	private Long generatestate;

	@ApiModelProperty(value = "创建人ID", example = "1")
	private Long crnumber;

	@ApiModelProperty(value = "数据库类型", example = "2")
	private Integer dbtype;

	@ApiModelProperty(value = "字段顺序", example = "-1")
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long fieldorder;

	@ApiModelProperty(value = "分组id", example = "0")
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long groupid;

	@ApiModelProperty(value = "注释", example = "类型")
	private String note;

	@ApiModelProperty(value = "", example = "")
	private Integer classtype;

	@TableField(exist = false)
	private String dbTypeStr;

	@TableField(exist = false)
	private String fieldTypeStr;

	@TableField(exist = false)
	private String groupname;

	public String getFieldname(){
		if(StringUtils.isEmpty(fieldname)){
			return fieldname;
		}else {
			return fieldname.toUpperCase();
		}
	}
}
