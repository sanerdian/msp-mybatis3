package com.jnetdata.msp.flowable.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;

@Data
@TableName("j_flow_metadata_config")
@ApiModel(value="元数据配置", description="元数据配置")
public class MetadataConfig implements EntityId<Integer> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "配置编码，主键")
    @TableId(value = "config_code", type = IdType.INPUT)
    private Integer id;

    @ApiModelProperty(value = "元数据表名")
    @TableField("metadata_table")
    private String metadataTable;

    @ApiModelProperty(value = "元数据描述")
    @TableField("metadata_desc")
    private String metadataDesc;

    @ApiModelProperty(value = "元数据分类：1-基础数据，2-资源")
    @TableField("metadata_type")
    private String metadataType;

    @ApiModelProperty(value = "对应的流程key")
    @TableField("process_key")
    private String processKey;


}
