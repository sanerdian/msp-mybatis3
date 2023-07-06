package com.jnetdata.msp.manage.extendField.model;

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

@Data
@TableName("extfield")
@ApiModel(value="扩展字段实体类",description = "扩展字段")
public class ExtendField extends BaseEntity implements EntityId<Long> {

    @ApiModelProperty(value = "主键ID", hidden = true)
    @TableId(value = "EXTFIELDID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "TABLENAME")
    @ApiModelProperty(value = "默认扩展字段只作用于DOCUMENTINFO表")
    private String tablename;

    @TableField(value = "FIELDNAME")
    @ApiModelProperty(value = "中文名称")
    private String fieldname;

    @TableField(value = "FIELDTYPE")
    @ApiModelProperty(value = "数据库里数据类型")
    private String fieldtype;

    @TableField(value = "FIELDDEFAULT")
    @ApiModelProperty(value = "默认值")
    private String fielddefault;

    @TableField(value = "FIELDMAXLEN")
    @ApiModelProperty(value = "最大长度")
    private String fieldmaxlen;


    @TableField(value = "FIELDNULLABLE")
    @ApiModelProperty(value = "是否空值")
    private String fieldnullable;
}
