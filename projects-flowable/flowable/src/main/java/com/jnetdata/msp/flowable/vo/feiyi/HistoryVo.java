package com.jnetdata.msp.flowable.vo.feiyi;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

/**
 * 我的已办
 */
@Data
public class HistoryVo {

    @ApiModelProperty(value = "流程定义ID")
    private String procDefiId;

    @ApiModelProperty(value = "流程实例ID")
    private String procInstId;

    @ApiModelProperty(value = "元数据id")
    private String metadataId;

    @ApiModelProperty(value = "元数据序号（对应metadata_config表id）")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Integer metadataNo;

    @ApiModelProperty(value = "元数据标题")
    private String metadataTitle;

    @ApiModelProperty(value = "元数据url")
    private String metadataUrl;

    @ApiModelProperty(value = "元数据描述")
    private String metadataDesc;

    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "审核状态：1-审核中，2-已终止，3-已撤回，4-已完成，5-已驳回")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Integer auditStatus;

    @ApiModelProperty(value = "元数据分类：1-基础数据，2-资源")
    private String metadataType;
}
