package com.jnetdata.msp.generator.controller.component.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

@Data
@TableName("component")
public class Component implements EntityId<Long> {

    @TableId(value = "componentid" , type = IdType.ID_WORKER)
    @ApiModelProperty(value = "id" , hidden = true)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "部署地址", example = "XXX")
    private String componentdpath;

    @ApiModelProperty(value = "生成地址", example = "XXX")
    private String componentcpath;

    @ApiModelProperty(value = "访问地址" , example = "XXX")
    private String componenturl;

    @ApiModelProperty(value = "模板ID", example = "")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long moduleinfoid;

    @ApiModelProperty(value = "操作系统名称", example = "")
    private String osname;

    @ApiModelProperty(value = "备注", example = "")
    private String remark;
}
