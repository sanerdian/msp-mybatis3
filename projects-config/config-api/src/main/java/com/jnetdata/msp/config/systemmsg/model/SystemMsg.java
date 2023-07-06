package com.jnetdata.msp.config.systemmsg.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

/**
 * Created by TF on 2019/3/27.
 */

@Data
@ApiModel(value = "应用信息实体类",description = "应用信息")
public class SystemMsg extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", hidden = true)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;


    @ApiModelProperty(value = "计算机名")
    private String compaterName;


    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "系统默认语言")
    private String language;


    @ApiModelProperty(value = "系统默认字符编码")
    private String code;

    @ApiModelProperty(value = "容器类型")
    private String vessel;

    @ApiModelProperty(value = "java安装目录")
    private String javaAddress;

    @ApiModelProperty(value = "WebServer运行目录")
    private String webAddress;

    @ApiModelProperty(value = "Java的运行环境版本")
    private String javaEnvironment;

    @ApiModelProperty(value = "操作系统的名称")
    private String systemName;

    @ApiModelProperty(value = "操作系统的构架")
    private String framework;

    @ApiModelProperty(value = "操作系统的版本")
    private String systemVersion;

    @ApiModelProperty(value = "cpu核心数")
    private String cpuNum;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "Mac")
    private String mac;

    @ApiModelProperty(value = "虚拟机内存总量")
    private String memorySum;

    @ApiModelProperty(value = "虚拟机空闲内存量")
    private String memoryFree;

    @ApiModelProperty(value = "虚拟机使用最大内存量")
    private String memoryUse;


}
