package com.jnetdata.msp.flowable.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;

import java.util.Date;

@Data
@TableName("j_flow_suspend")
@ApiModel(value="流程挂起")
public class FlowSuspend implements EntityId<Integer>  {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id")
    @TableField(value = "user_id")
    private String userId;

    @ApiModelProperty(value = "流程实例id")
    @TableField(value = "proc_inst_id")
    private String procInstId;

    @ApiModelProperty(value = "流程定义id")
    @TableField(value = "proc_defi_id")
    private String procDefiId;

    @ApiModelProperty(value = "流程定义key")
    @TableField(value = "proc_defi_key")
    private String procDefiKey;

    @ApiModelProperty(value = "挂起时间")
    @TableField(value = "suspend_time")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date suspendTime;

    @ApiModelProperty(value = "步骤名称")
    @TableField(value = "step_name")
    private String stepName;

    @ApiModelProperty(value = "接收时间")
    @TableField(value = "receive_time")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date receiveTime;

}
