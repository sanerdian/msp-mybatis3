package com.jnetdata.msp.zdff.publishdistribution.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "站点分发信息", description = "")
public class ftpentity {
    private String ipAddr;//ip地址
    private Integer port;//端口号
    private String userName;//用户名
    private String pwd;//密码
    private String path;//aaa路径
}
