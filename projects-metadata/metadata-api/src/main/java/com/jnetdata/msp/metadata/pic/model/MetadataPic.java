package com.jnetdata.msp.metadata.pic.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

/**
 * <p>
 *
 * </p>
 *
 * @author zyj
 * @since 2019-10-23
 */
@Data
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
@TableName("metadata_pic")
@ApiModel(value="MetadataPic对象", description="")
public class MetadataPic extends  BaseEntity implements EntityId<Long> {

	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value = "id", hidden = true)
	@JSONField(serializeUsing = ToStringSerializer.class)
	private Long id;

	@ApiModelProperty(value = "tableid", example = "tableid")
	private long tableid;

	@ApiModelProperty(value = "tablename", example = "1")
	private String tablename;

	@ApiModelProperty(value = "dataid", example = "type")
	private long dataid;

	@ApiModelProperty(value = "picid", example = "")
	private long picid;

}
