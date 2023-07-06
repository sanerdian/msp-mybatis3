package com.jnetdata.msp.metadata.fieldinfo.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.sql.Timestamp;
import java.util.Date;

@Data
@TableName("dbfieldinfo")
@ApiModel(value = "数据库字段信息实体类",description = "数据库字段信息")
public class Fieldinfo extends BaseEntity implements EntityId<Long> {

	@TableId(value = "dbfieldinfoid", type = IdType.ID_WORKER)
	@ApiModelProperty(value = "主键id", hidden = true)
	@JSONField(serializeUsing = ToStringSerializer.class)
	@ExcelProperty("id")
	private Long id;

	@ApiModelProperty(value = "数据库表名", example = "JMETA_XXX")
	@ExcelProperty("数据库表名")
	private String tablename;

	@ApiModelProperty(value = "元数据表id", example = "15608474745058141")
	@JSONField(serializeUsing = ToStringSerializer.class)
	@ExcelProperty("元数据表id")
	private Long tableid;

	@ApiModelProperty(value = "字段名", example = "type")
	@ExcelProperty("字段名")
	private String fieldname;

	@ApiModelProperty(value = "中文名称", example = "类型")
	@ExcelProperty("中文名称")
	private String anothername;

	@ApiModelProperty(value = "视图生成时间", example = "")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@ExcelProperty("视图生成时间")
	private Date generatetime;

	@ApiModelProperty(value = "是否生成视图",example = "")
	@ExcelProperty("是否生成视图")
	private Long generatestate;

	@ApiModelProperty(value = "字段类型", example = "1")
	@ExcelProperty("字段类型")
	private Integer fieldtype;

	@ApiModelProperty(value = "匹配方式", example = "1")
	@TableField(value = "match_type")
	@ExcelProperty("匹配方式")
	private Integer matchType;

	@ApiModelProperty(value = "数据长度", example = "255")
	@ExcelProperty("数据长度")
	private String dblength;

	@ApiModelProperty(value = "默认值", example = "1")
	@ExcelProperty("默认值")
	private String defaultvalue;

	@ApiModelProperty(value = "枚举值", example = "1")
	@ExcelProperty("枚举值")
	private String enmvalue;

	@ApiModelProperty(value = "是否允许空值", example = "0")
	@ExcelProperty("是否允许空值")
	private Integer notbenull;

	@ApiModelProperty(value = "分类编号", example = "")
	@JSONField(serializeUsing = ToStringSerializer.class)
	@ExcelProperty("分类编号")
	private Long classid;

	@ApiModelProperty(value = "父分类编号", example = "")
	@JSONField(serializeUsing = ToStringSerializer.class)
	@ExcelProperty("父分类编号")
	private Long classparentid;

	@ApiModelProperty(value = "数据库字段精度", example = "")
	@ExcelProperty("数据库字段精度")
	private Integer dbscale;

	@ApiModelProperty(value = "校验器", example = "")
	@ExcelProperty("校验器")
	private String validator;

	@ApiModelProperty(value = "单选或多选项", example = "")
	@ExcelProperty("单选或多选项")
	private Integer radorchk;

	@ApiModelProperty(value = "是否不可编辑", example = "")
	@ExcelProperty("是否不可编辑")
	private Integer notedit;

	@ApiModelProperty(value = "是否隐藏", example = "0")
	@ExcelProperty("是否隐藏")
	private Integer hiddenfield;

	@ApiModelProperty(value = "数据库类型", example = "2")
	@ExcelProperty("数据库类型")
	private Integer dbtype;

	@ApiModelProperty(value = "字段顺序", example = "-1")
	@JSONField(serializeUsing = ToStringSerializer.class)
	@ExcelProperty("字段顺序")
	private Long fieldorder;

	@ApiModelProperty(value = "分组id", example = "0")
	@JSONField(serializeUsing = ToStringSerializer.class)
	@ExcelProperty("分组id")
	private Long groupid;

	@ApiModelProperty(value = "注释", example = "类型")
	@ExcelProperty("注释")
	private String note;

	@ApiModelProperty(value = "列表展示", example = "0")
	@TableField("SHOW_LIST")
	@ExcelProperty("列表展示")
	private Integer showList;

	@ApiModelProperty(value = "搜索展示", example = "0")
	@TableField("SHOW_SEARCH")
	@ExcelProperty("搜索展示")
	private Integer showSearch;

	@ApiModelProperty(value = "详情展示", example = "0")
	@TableField("SHOW_DETAIL")
	@ExcelProperty("详情展示")
	private Integer showDetail;

	@ApiModelProperty(value = "宽度", example = "0")
	@TableField("WIDTH")
	@ExcelProperty("宽度")
	private Integer width;

	@ApiModelProperty(value = "是否是排序字段", example = "0")
	@TableField("ISSORT")
	@ExcelProperty("是否是排序字段")
	private Integer issort;

	@ApiModelProperty(value = "关联表id", example = "0")
	@JSONField(serializeUsing = ToStringSerializer.class)
	@TableField("JOINTABLE")
	@ExcelIgnore
	private Long jointable;

	@ApiModelProperty(value = "关联字段id", example = "0")
	@JSONField(serializeUsing = ToStringSerializer.class)
	@TableField("JOINFIELD")
	@ExcelIgnore
	private Long joinfield;

	@ApiModelProperty(value = "校验类型", example = "0")
	@TableField("VERIFY_TYPE")
	@ExcelProperty("校验类型")
	private Integer verifyType;

	@ApiModelProperty(value = "校验长度_下限", example = "0")
	@TableField("VERIFY_LENGTH_MIN")
	@ExcelProperty("校验长度_下限")
	private Integer verifyLengthMin;

	@ApiModelProperty(value = "校验类型_上限", example = "0")
	@TableField("VERIFY_LENGTH_MAX")
	@ExcelProperty("校验类型_上限")
	private Integer verifyLengthMax;

	@ApiModelProperty(value = "是否是标题", example = "1:是,0或null:不是")
	@TableField("ISTITLE")
	@ExcelProperty("是否是标题")
	private Integer istitle;

	@ApiModelProperty(value = "正则", example = "0")
	@TableField("REGULAR_STR")
	@ExcelProperty("正则")
	private String regularStr;

	@ApiModelProperty(value = "校验失败信息", example = "0")
	@TableField("VERIFYINFO")
	@ExcelProperty("校验失败信息")
	private String verifyinfo;

	@ApiModelProperty(value = "", example = "")
	@ExcelProperty("classtype")
	private Integer classtype;

	@TableField(exist = false)
	@ExcelIgnore
	private String dbTypeStr;
	@TableField(exist = false)
	@ExcelIgnore
	private String fieldTypeStr;
	@TableField(exist = false)
	@ExcelIgnore
	private String groupname;
	@TableField(exist = false)
	@ExcelIgnore
	private String oldname;
	@ApiModelProperty(value = "搜索中文或英文名称")
	@TableField(exist = false)
	@ExcelIgnore
	private String searchName;

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
