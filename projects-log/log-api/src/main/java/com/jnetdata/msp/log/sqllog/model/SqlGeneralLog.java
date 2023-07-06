package com.jnetdata.msp.log.sqllog.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "mysql.general_log")
public class SqlGeneralLog {

    @TableField("user_host")
    private String userHost;

    @TableField("server_id")
    private Integer serverId;

    @TableField("thread_id")
    private Long threadId;

    @TableField("command_type")
    private String commandType;

    @TableField("argument")
    private String argument;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("event_time")
    private Date eventTime;

    @TableField(exist = false)
    private Date endEventTime;

}
