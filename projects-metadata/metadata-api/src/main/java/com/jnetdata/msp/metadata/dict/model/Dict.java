package com.jnetdata.msp.metadata.dict.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

@Data
@TableName("dict")
@ApiModel(value = "词典实体类",description = "词典")
public class Dict implements EntityId<Long> {

	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value = "主键id", hidden = true)
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long id;

	@ApiModelProperty(value = "类型", example = "field_type")
	private String type;

	@ApiModelProperty(value = "编号", example = "1")
	private int no;

	@ApiModelProperty(value = "值", example = "type")
	private String code;

	@ApiModelProperty(value = "名称", example = "")
	private String name;

	@ApiModelProperty(value = "排序", example = "1")
	private int sort;

}
