package com.jnetdata.msp.metadata.group.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

@Data
@TableName("metadata_group")
@ApiModel(value = "元数据分组实体类",description = "元数据分组")
public class MetadataGroup extends BaseEntity implements EntityId<Long> {

	@TableId(value = "id", type = IdType.ID_WORKER)
	@ApiModelProperty(value = "主键id", hidden = true)
	@JSONField(serializeUsing = ToStringSerializer.class)
	@ExcelProperty("id")
	private Long id;

	@ApiModelProperty(value = "分组名", example = "JMETA_XXX")
	@ExcelProperty("分组名")
	private String name;

	@ApiModelProperty(value = "分组描述", example = "")
	@ExcelProperty("分组描述")
	private String groupdesc;

	@ApiModelProperty(value = "parentid" , example = "XXX")
	@JSONField(serializeUsing = ToStringSerializer.class)
	@ExcelProperty("父级id")
	private Long parentid;

}
