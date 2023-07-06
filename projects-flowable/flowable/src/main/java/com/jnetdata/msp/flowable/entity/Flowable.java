package com.jnetdata.msp.flowable.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Flowable {
	@ApiModelProperty(value = "状态")
	private Integer state;

	@ApiModelProperty(value = "模型名称")
	private String modelName;

	@ApiModelProperty(value = "模型ID")
	private String modelId;

	@ApiModelProperty(value = "流程定义ID")
	private String procDefiId;

	@ApiModelProperty(value = "流程定义名称")
	private String procDefiName;

	@ApiModelProperty(value = "任务ID")
	private String taskId;

	@ApiModelProperty(value = "流程实例ID")
	private String procInstId;

	@ApiModelProperty(value = "元数据id")
	private String metadataId;

	@ApiModelProperty(value = "删除原因")
	private String deleteReason;

	@ApiModelProperty(value = "新闻状态")
	private String newsStatus;

	@ApiModelProperty(value = "栏目ID")
	private String columnId;
}
