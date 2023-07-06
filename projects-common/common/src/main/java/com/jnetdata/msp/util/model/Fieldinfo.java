package com.jnetdata.msp.util.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@Data
@TableName("dbfieldinfo")
public class Fieldinfo implements EntityId<Long> {

	@TableId(value = "dbfieldinfoid", type = IdType.ID_WORKER)
	@ApiModelProperty(value = "id", hidden = true)
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long id;

	@ApiModelProperty(value = "数据库表名", example = "JMETA_XXX")
	private String tablename;

	@ApiModelProperty(value = "元数据表id", example = "15608474745058141")
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long tableid;

	@ApiModelProperty(value = "字段名", example = "type")
	private String fieldname;

	@ApiModelProperty(value = "中文名称", example = "类型")
	private String anothername;

	@ApiModelProperty(value = "视图生成时间", example = "")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date generatetime;

	@ApiModelProperty(value = "是否生成视图",example = "")
	private Long generatestate;

	@ApiModelProperty(value = "字段类型", example = "1")
	private Integer fieldtype;

	@ApiModelProperty(value = "字段类型", example = "1")
	@TableField(value = "match_type")
	private Integer matchType;

	@ApiModelProperty(value = "数据长度", example = "255")
	private String dblength;

	@ApiModelProperty(value = "默认值", example = "1")
	private String defaultvalue;

	@ApiModelProperty(value = "枚举值", example = "1")
	private String enmvalue;

	@ApiModelProperty(value = "是否允许空值", example = "0")
	private Integer notbenull;

	@ApiModelProperty(value = "分类编号", example = "")
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long classid;

	@ApiModelProperty(value = "父分类编号", example = "")
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long classparentid;

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

	@ApiModelProperty(value = "列表展示", example = "0")
	@TableField("SHOW_LIST")
	private Integer showList;

	@ApiModelProperty(value = "搜索展示", example = "0")
	@TableField("SHOW_SEARCH")
	private Integer showSearch;

	@ApiModelProperty(value = "详情展示", example = "0")
	@TableField("SHOW_DETAIL")
	private Integer showDetail;

	@ApiModelProperty(value = "详情展示", example = "0")
	@TableField("WIDTH")
	private Integer width;

	@ApiModelProperty(value = "是否是排序字段", example = "0")
	@TableField("ISSORT")
	private Integer issort;

	@ApiModelProperty(value = "", example = "")
	private Integer classtype;

	@TableField(exist = false)
	private String dbTypeStr;

	@TableField(exist = false)
	private String fieldTypeStr;

	@TableField(exist = false)
	private String groupname;

	@TableField(exist = false)
	private String oldname;

	public String getFieldname(){
		if(StringUtils.isEmpty(fieldname)){
			return fieldname;
		}else {
			return fieldname.toUpperCase();
		}
	}

	public String getProName(){
		return fieldToProperty(fieldname);
	}

	public static String fieldToProperty(String field) {
		if (null == field) {
			return "";
		}
		char[] chars = field.toLowerCase().toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == '_') {
				int j = i + 1;
				if (j < chars.length) {
					sb.append(String.valueOf(chars[j]).toUpperCase());
					i++;
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
