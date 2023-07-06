
-- 消息中心
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message` (
  `id` bigint(20) NOT NULL,
  `msg_type` varchar(50) NOT NULL COMMENT '消息类型',
  `source_type` varchar(50) DEFAULT NULL COMMENT '消息来源类型',
  `source_id` bigint(20) DEFAULT NULL COMMENT '消息来源Id',
  `from_userid` bigint(20) DEFAULT NULL COMMENT '消息来源账号Id',
  `target_id` bigint(20) DEFAULT NULL COMMENT '消息目标Id',
  `msg_title` varchar(100) DEFAULT NULL COMMENT '消息标题',
  `msg_content` varchar(1000) DEFAULT NULL COMMENT '消息内容',
  `mst_date` datetime DEFAULT NULL COMMENT '消息时间',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人Id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人Id',
  `modify_time` datetime DEFAULT NULL COMMENT '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
