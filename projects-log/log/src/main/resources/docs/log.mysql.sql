/*
Navicat MySQL Data Transfer

Source Server         : 47.93.151.74
Source Server Version : 50726
Source Host           : 47.93.151.74:13306
Source Database       : fastdev

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2020-02-05 16:28:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for log
-- ----------------------------
--- DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `LOGID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `address` varchar(255) DEFAULT NULL COMMENT 'ip地址',
  `LOGDESC` varchar(1000) DEFAULT NULL COMMENT '日志描述',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `LOGTYPE` int(5) DEFAULT NULL COMMENT '日志类型（1.操作日志2.报错日志3.控制台日志4.sql日志）',
  `LOGOBJNAME` varchar(1000) DEFAULT NULL,
  `LOGOBJTYPE` int(5) DEFAULT NULL COMMENT '操作对象类型',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '用户名',
  PRIMARY KEY (`LOGID`)
) ENGINE=InnoDB AUTO_INCREMENT=1192743281507356675 DEFAULT CHARSET=utf8 COMMENT='日志表';

-- ----------------------------
-- Table structure for logininfo
-- ----------------------------
--- DROP TABLE IF EXISTS `logininfo`;
CREATE TABLE `logininfo` (
  `LOGINID` bigint(20) NOT NULL COMMENT '主键id',
  `LOGINTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '登陆时间',
  `LOGINIP` varchar(255) NOT NULL COMMENT '登陆ip',
  `LOGOUTTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '退出时间',
  `TIMES` bigint(20) NOT NULL DEFAULT '0' COMMENT '登陆次数',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`LOGINID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_content_log
-- ----------------------------
--- DROP TABLE IF EXISTS `t_content_log`;
CREATE TABLE `t_content_log` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `handle_type` varchar(255) DEFAULT NULL COMMENT '操作类型',
  `content_type` varchar(255) DEFAULT NULL COMMENT '内容类型',
  `column` varchar(255) DEFAULT NULL COMMENT '栏目',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  `RESULT` varchar(255) DEFAULT NULL COMMENT '操作明细',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  `detail` varchar(255) DEFAULT NULL COMMENT '操作状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='内容操作日志表';

-- ----------------------------
-- Table structure for t_feiyi_log
-- ----------------------------
--- DROP TABLE IF EXISTS `t_feiyi_log`;
CREATE TABLE `t_feiyi_log` (
  `ID` bigint(20) NOT NULL,
  `handle_type` varchar(255) DEFAULT NULL COMMENT '操作类型',
  `content_type` varchar(255) DEFAULT NULL COMMENT '内容类型',
  `column` varchar(255) DEFAULT NULL COMMENT '栏目',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  `RESULT` varchar(255) DEFAULT NULL COMMENT '操作明细',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  `detail` varchar(255) DEFAULT NULL COMMENT '操作状态',
  `TABLE` varchar(255) DEFAULT NULL,
  `TABLEID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_metadata_log
-- ----------------------------
--- DROP TABLE IF EXISTS `t_metadata_log`;
CREATE TABLE `t_metadata_log` (
  `ID` bigint(10) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL COMMENT '表名',
  `HANDLETYPE` varchar(255) DEFAULT NULL COMMENT '操作类型',
  `RESULT` varchar(255) DEFAULT NULL COMMENT '操作结果',
  `TITLE` varchar(255) DEFAULT NULL COMMENT '元数据标题',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人姓名',
  `ADDRESS` varchar(255) DEFAULT NULL COMMENT 'IP地址',
  `DETAIL` text COMMENT '操作明细',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_model_log
-- ----------------------------
--- DROP TABLE IF EXISTS `t_model_log`;
CREATE TABLE `t_model_log` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `column` varchar(255) DEFAULT NULL COMMENT '栏目',
  `model_id` bigint(20) DEFAULT NULL COMMENT '组件id',
  `model_type` varchar(255) DEFAULT NULL COMMENT '组件类型',
  `handle_type` varchar(255) DEFAULT NULL COMMENT '操作类型',
  `detail` varchar(255) DEFAULT NULL COMMENT '操作明细',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组件操作日志表';

-- ----------------------------
-- Table structure for t_publish_log
-- ----------------------------
--- DROP TABLE IF EXISTS `t_publish_log`;
CREATE TABLE `t_publish_log` (
  `ID` bigint(20) NOT NULL,
  `COLUMNNAME` varchar(255) DEFAULT NULL COMMENT '栏目名',
  `PATH` varchar(255) DEFAULT NULL COMMENT '路径',
  `PATHNAME` varchar(255) DEFAULT NULL COMMENT '项目名',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人姓名',
  `ADDRESS` varchar(255) DEFAULT NULL COMMENT 'IP地址',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_safety_log
-- ----------------------------
--- DROP TABLE IF EXISTS `t_safety_log`;
CREATE TABLE `t_safety_log` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `address` varchar(255) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL COMMENT '原始消息',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='安全日志表';

-- ----------------------------
-- Table structure for t_task_log
-- ----------------------------
--- DROP TABLE IF EXISTS `t_task_log`;
CREATE TABLE `t_task_log` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `address` varchar(255) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL COMMENT '原始消息',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='定时任务日志表';

-- ----------------------------
-- Table structure for t_template_log
-- ----------------------------
--- DROP TABLE IF EXISTS `t_template_log`;
CREATE TABLE `t_template_log` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `column_name` varchar(255) DEFAULT NULL COMMENT '栏目名称',
  `template_type` varchar(255) DEFAULT NULL COMMENT '模板类型',
  `template_name` varchar(255) DEFAULT NULL COMMENT '模板名称',
  `handle_type` varchar(255) DEFAULT NULL COMMENT '操作类型',
  `handle_result` varchar(255) DEFAULT NULL COMMENT '操作结果',
  `detail` varchar(255) DEFAULT NULL COMMENT '操作明细',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  `ADDRESS` varchar(255) DEFAULT NULL COMMENT 'ip地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板管理日志表';

-- ----------------------------
-- Table structure for t_uesrmenu_log
-- ----------------------------
--- DROP TABLE IF EXISTS `t_uesrmenu_log`;
CREATE TABLE `t_uesrmenu_log` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `address` varchar(255) DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `content` varchar(1000) DEFAULT NULL COMMENT '原始消息',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户菜单操作日志表';
SET FOREIGN_KEY_CHECKS=1;
