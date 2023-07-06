
create table t_system_log(
     `id` bigint not null COMMENT 'id',
     `ip` varchar(100) not null COMMENT 'ip',
     `url` varchar(20) not null COMMENT 'url',
     `method` varchar(20) not null COMMENT '请求方法',
     `args` varchar(255) not null COMMENT '请求参数',
     `status` varchar(20) not null COMMENT '请求结果状态',
     `result` varchar(2000) not null COMMENT '请求结果',
     `exceptionmsg` varchar(20) not null COMMENT '异常信息',
     `create_by` bigint COMMENT '创建人',
     `create_time` datetime COMMENT '创建时间',
     `modify_by` bigint COMMENT '最后修改人',
     `modify_time` datetime COMMENT '最后修改时间',
     PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统日志';


