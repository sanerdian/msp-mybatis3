/*
Navicat MySQL Data Transfer

Source Server         : 47.93.151.74
Source Server Version : 50726
Source Host           : 47.93.151.74:13306
Source Database       : fastdev
Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2020-02-05 16:06:13
*/

SET FOREIGN_KEY_CHECKS=0;


-- ----------------------------
-- Table structure for companyinfo
-- ----------------------------
--- DROP TABLE IF EXISTS `companyinfo`;
CREATE TABLE `companyinfo` (
  `COMPANYID` bigint(20) NOT NULL COMMENT '主键ID',
  `CPYNAME` varchar(255) NOT NULL COMMENT '企业名称',
  `CPYDESC` text COMMENT '公司简介',
  `REGTIME` datetime DEFAULT NULL COMMENT '当前时间',
  `STATUS` int(5) DEFAULT '1' COMMENT '0未开通 1开通',
  `PRINCIPAL` varchar(255) DEFAULT NULL COMMENT '负责人',
  `VALIDITY` int(5) DEFAULT NULL COMMENT '有效期',
  `ENABLE` int(5) DEFAULT NULL COMMENT '是 否  0,1',
  `BSNUM` int(5) DEFAULT NULL COMMENT '购买站点数',
  `PQPLUG` varchar(255) DEFAULT NULL COMMENT '插件ID,格式如下：1,2,3  ',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人',
  `CRTIME` datetime DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `CPYTEL` varchar(255) DEFAULT NULL COMMENT '企业电话',
  `CPYORDER` int(5) DEFAULT NULL,
  PRIMARY KEY (`COMPANYID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企业信息表';


-- ----------------------------
-- Table structure for groupinfo
-- ----------------------------
--- DROP TABLE IF EXISTS `groupinfo`;
CREATE TABLE `groupinfo` (
  `GROUPID` bigint(20) NOT NULL COMMENT '主键id',
  `GROUPNAME` varchar(255) DEFAULT NULL COMMENT '组织名称',
  `GROUPPATH` varchar(255) DEFAULT NULL COMMENT '组织层级结构',
  `GDESC` varchar(255) DEFAULT NULL COMMENT '组织描述',
  `PARENTID` bigint(20) DEFAULT NULL,
  `GROUPORDER` bigint(10) DEFAULT NULL COMMENT '组织排序字段',
  `STATUS` int(5) DEFAULT NULL COMMENT '组织状态',
  `ADDRESS` varchar(255) DEFAULT NULL COMMENT '组织地址',
  `MOBILE` varchar(255) DEFAULT NULL COMMENT '手机号码',
  `PHONE` varchar(255) DEFAULT NULL COMMENT '固定电话',
  `QQ` varchar(255) DEFAULT NULL COMMENT '组织qq',
  `EMAIL` varchar(255) DEFAULT NULL COMMENT '组织email',
  `GRPCODE` varchar(255) DEFAULT NULL COMMENT '组织编码',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `COMPANYID` bigint(20) DEFAULT NULL COMMENT '所属机构id',
  PRIMARY KEY (`GROUPID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织信息表';

-- ----------------------------
-- Table structure for grp_user
-- ----------------------------
--- DROP TABLE IF EXISTS `grp_user`;
CREATE TABLE `grp_user` (
  `GRPUSERID` bigint(20) NOT NULL COMMENT '主键id',
  `GROUPID` bigint(20) DEFAULT NULL COMMENT '组织编号',
  `USERID` bigint(20) DEFAULT NULL COMMENT '用户编号',
  `ISLEADER` int(5) DEFAULT NULL COMMENT '是否组长',
  `USERORDER` bigint(10) DEFAULT NULL COMMENT '排序字段',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人姓名',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`GRPUSERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织用户关联表';

-- ----------------------------
-- Table structure for ituser
-- ----------------------------
--- DROP TABLE IF EXISTS `ituser`;
CREATE TABLE `ituser` (
  `USERID` bigint(20) NOT NULL COMMENT '主键id',
  `USERNAME` varchar(255) DEFAULT NULL COMMENT '用户名',
  `MDPASSWORD` varchar(255) DEFAULT NULL COMMENT '加密密码',
  `PASSWORD` varchar(255) DEFAULT NULL COMMENT '真实密码',
  `REGTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '注册时间',
  `TEL` varchar(255) DEFAULT NULL COMMENT '电话',
  `MOBILE` varchar(255) DEFAULT NULL COMMENT '手机',
  `EMAIL` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `STATUS` int(10) DEFAULT NULL COMMENT '用户状态',
  `LASTLOGINIP` varchar(255) DEFAULT NULL COMMENT '登录ip',
  `SEX` varchar(255) DEFAULT NULL COMMENT '性别',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `headUrl` varchar(255) DEFAULT NULL COMMENT '头像url',
  `describe` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`USERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='互联网用户管理表';

-- ----------------------------
-- Table structure for limit_info
-- ----------------------------
--- DROP TABLE IF EXISTS `limit_info`;
CREATE TABLE `limit_info` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `TYPE` bigint(20) DEFAULT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `DESC` varchar(255) DEFAULT NULL,
  `SYSTEM` int(1) DEFAULT '0',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `STATUS` int(20) DEFAULT NULL,
  `modify_by` bigint(20) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for limit_role
-- ----------------------------
--- DROP TABLE IF EXISTS `limit_role`;
CREATE TABLE `limit_role` (
  `ID` bigint(20) NOT NULL,
  `ROLE_ID` bigint(20) DEFAULT NULL,
  `LIMIT_ID` bigint(20) DEFAULT NULL,
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人姓名',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for limit_type
-- ----------------------------
--- DROP TABLE IF EXISTS `limit_type`;
CREATE TABLE `limit_type` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `CODE` varchar(255) DEFAULT NULL,
  `DESC` varchar(255) DEFAULT NULL,
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `SYSTEM` int(20) DEFAULT NULL,
  `STATUS` varchar(20) DEFAULT NULL,
  `modify_by` bigint(20) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for menuman
-- ----------------------------
--- DROP TABLE IF EXISTS `menuman`;
CREATE TABLE `menuman` (
  `MENUID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type` int(2) DEFAULT NULL,
  `natural_id` varchar(20) DEFAULT NULL COMMENT '自然Id',
  `NAME` varchar(50) DEFAULT NULL COMMENT '标题',
  `URL` varchar(100) DEFAULT NULL COMMENT 'url',
  `seq` int(11) DEFAULT '0' COMMENT '显示顺序',
  `PARENTID` bigint(20) DEFAULT NULL COMMENT '父Id',
  `STATE` int(5) DEFAULT NULL COMMENT '是否活跃',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人Id',
  `CRTIME` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人Id',
  `modify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `photo_url` varchar(255) DEFAULT NULL COMMENT '图片链接',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人',
  `COMPANYID` decimal(20,0) DEFAULT NULL COMMENT '机构ID',
  PRIMARY KEY (`MENUID`)
) ENGINE=InnoDB AUTO_INCREMENT=1212939544660336642 DEFAULT CHARSET=utf8 COMMENT='资源表';


-- ----------------------------
-- Table structure for right_info
-- ----------------------------
--- DROP TABLE IF EXISTS `right_info`;
CREATE TABLE `right_info` (
  `RIGHTID` bigint(20) NOT NULL COMMENT '主键id',
  `OBJTYPE` int(5) DEFAULT NULL COMMENT '对象类型(0:系统用户,1角色,2组织，3：互联网用户)',
  `OBJID` bigint(20) DEFAULT NULL COMMENT '所属对象编号',
  `limitType` int(5) DEFAULT NULL COMMENT '权限类型',
  `OPRTYPE` int(2) DEFAULT NULL COMMENT '操作类型',
  `OPRIDTYPE` int(5) DEFAULT NULL COMMENT '操作对象名称类型',
  `OPRID` bigint(20) DEFAULT NULL COMMENT '操作对象id',
  `RIGHTVALUE` varchar(255) DEFAULT NULL COMMENT '权限值',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`RIGHTID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='授权关联表';

-- ----------------------------
-- Table structure for roleinfo
-- ----------------------------
--- DROP TABLE IF EXISTS `roleinfo`;
CREATE TABLE `roleinfo` (
  `ROLEID` bigint(20) NOT NULL COMMENT '主键id',
  `ROLENAME` varchar(255) DEFAULT NULL COMMENT '角色名称',
  `ROLETYPE` varchar(255) DEFAULT NULL COMMENT '角色类型',
  `ROLEPATH` varchar(255) DEFAULT NULL COMMENT '角色结构层级',
  `ROLEDESC` varchar(255) DEFAULT NULL COMMENT '描述说明',
  `PARENTID` bigint(20) DEFAULT NULL COMMENT '父角色编号',
  `ROLEORDER` bigint(10) DEFAULT NULL COMMENT '显示顺序字段',
  `STATUS` int(2) DEFAULT NULL COMMENT '状态',
  `ADDRESS` varchar(255) DEFAULT NULL COMMENT '地址',
  `MOBILE` varchar(255) DEFAULT NULL COMMENT '手机',
  `EMAIL` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `QQ` varchar(255) DEFAULT NULL COMMENT 'qq',
  `PHONE` varchar(255) DEFAULT NULL COMMENT '电话',
  `ROLECODE` varchar(255) DEFAULT NULL COMMENT '编码',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `COMPANYID` bigint(20) DEFAULT NULL COMMENT '所属机构',
  PRIMARY KEY (`ROLEID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色基本信息表';

-- ----------------------------
-- Table structure for role_user
-- ----------------------------
--- DROP TABLE IF EXISTS `role_user`;
CREATE TABLE `role_user` (
  `ROLEUSERID` bigint(20) NOT NULL COMMENT '主键id',
  `ROLEID` bigint(20) DEFAULT NULL COMMENT '角色id',
  `USERID` bigint(20) DEFAULT NULL COMMENT '用户id',
  `ISLEADER` int(5) DEFAULT NULL COMMENT '是否为组长',
  `USERORDER` bigint(20) DEFAULT NULL COMMENT '排序字段',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人姓名',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `JOINTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ROLEUSERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';


-- ----------------------------
-- Table structure for userinfo
-- ----------------------------
--- DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo` (
  `USERID` bigint(20) NOT NULL COMMENT '主键id',
  `USERNAME` varchar(255) DEFAULT NULL COMMENT '用户名',
  `NICKNAME` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `TRUENAME` varchar(255) DEFAULT NULL COMMENT '真实姓名',
  `MDPASSWORD` varchar(255) DEFAULT NULL COMMENT '加密密码',
  `PASSWORD` varchar(255) DEFAULT NULL COMMENT '真实密码',
  `GROUPID` bigint(20) DEFAULT NULL COMMENT '组织id',
  `CREDIT` varchar(255) DEFAULT NULL COMMENT '用户积分',
  `REGTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '注册时间',
  `ADDRESS` varchar(255) DEFAULT NULL COMMENT '用户地址',
  `TEL` varchar(255) DEFAULT NULL COMMENT '电话',
  `MOBILE` varchar(255) DEFAULT NULL COMMENT '手机',
  `EMAIL` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `LOGINTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '登录时间',
  `LOGOUTTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '登出时间',
  `STATUS` int(10) DEFAULT NULL COMMENT '用户状态',
  `LASTLOGINTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后登录时间',
  `LASTLOGINIP` varchar(255) DEFAULT NULL COMMENT '登录ip',
  `CARDID` varchar(255) DEFAULT NULL COMMENT '身份证号',
  `DUTIES` varchar(255) DEFAULT NULL COMMENT '职务',
  `SEX` varchar(255) DEFAULT NULL COMMENT '性别',
  `NATION` varchar(255) DEFAULT NULL COMMENT '民族',
  `ISACTOR` int(10) DEFAULT NULL COMMENT '参会类型',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `headUrl` varchar(255) DEFAULT NULL COMMENT '头像url',
  `descr` varchar(255) DEFAULT NULL COMMENT '描述',
  `SIGN` varchar(255) DEFAULT NULL COMMENT '0：系统用户，3：互联网用户',
  `TIMES` bigint(255) DEFAULT '0' COMMENT '登录次数',
  `IDCARDA` varchar(255) DEFAULT NULL COMMENT '身份证正面路径',
  `IDCARDB` varchar(255) DEFAULT NULL COMMENT '身份证背面路径',
  `BIRTHDATE` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '出生日期',
  `MARRYSTATE` varchar(255) DEFAULT NULL COMMENT '婚姻状况',
  `WEINXIN` varchar(255) DEFAULT NULL COMMENT '微信',
  `QQNUMBER` bigint(10) DEFAULT NULL COMMENT 'qq号',
  `CONTACTS` varchar(255) DEFAULT NULL COMMENT '备用联系人',
  `RESERVEPHONE` varchar(255) DEFAULT NULL COMMENT '备用联系方式',
  `DIPLOMAA` varchar(255) DEFAULT NULL COMMENT '毕业证',
  `DIPLOMAB` varchar(255) DEFAULT NULL COMMENT '学位证',
  `OTHERCARD` varchar(255) DEFAULT NULL COMMENT '其他证件',
  `NATIVEPLACE` varchar(255) DEFAULT NULL COMMENT '籍贯',
  `BIRTHADRESS` varchar(255) DEFAULT NULL COMMENT '户籍地址',
  `NORMALADRESS` varchar(255) DEFAULT '' COMMENT '常住地址',
  `ISENTRY` int(255) DEFAULT NULL COMMENT '入离职状态',
  `LEADERID` bigint(20) DEFAULT NULL COMMENT '直属领导',
  PRIMARY KEY (`USERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户管理表';

-- ----------------------------
-- Table structure for user_relation
-- ----------------------------
--- DROP TABLE IF EXISTS `user_relation`;
CREATE TABLE `user_relation` (
  `USERRELATIONID` bigint(20) NOT NULL,
  `USERID` bigint(20) DEFAULT NULL,
  `VIRLEADERID` bigint(20) DEFAULT NULL,
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`USERRELATIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sysconfig
-- ----------------------------
--- DROP TABLE IF EXISTS `sysconfig`;
CREATE TABLE `sysconfig` (
  `CONFIGID` bigint(20) NOT NULL COMMENT '主键id',
  `CTYPE` varchar(255) DEFAULT NULL COMMENT '编号',
  `CKEY` varchar(255) DEFAULT NULL COMMENT '属性名',
  `CVALUE` varchar(255) DEFAULT NULL COMMENT '属性值',
  `CDESC` varchar(255) DEFAULT NULL COMMENT '描述',
  `MSGTYPE` varchar(255) DEFAULT NULL COMMENT '消息类型',
  `IFENCRYPT` int(1) DEFAULT '0' COMMENT '是否加密 0不加密 1 加密',
  `GROUPID` bigint(20) DEFAULT NULL COMMENT '组织Id',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  `SITEID` bigint(20) DEFAULT NULL COMMENT '站点信息id',
  `MSGSIGN` int(20) DEFAULT NULL COMMENT '消息源类型，0：消息配置 1：测试配置 2：消息短信 3：消息微信 4：系统消息 5：app消息',
  PRIMARY KEY (`CONFIGID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置表';


-- ----------------------------
-- Table structure for customernewslog
-- ----------------------------
--- DROP TABLE IF EXISTS `customernewslog`;
CREATE TABLE `customernewslog` (
  `NEWSID` bigint(20) NOT NULL COMMENT '主键id',
  `ADDRESSEE` bigint(20) DEFAULT NULL COMMENT '接收用户id',
  `SENDER` bigint(20) DEFAULT NULL COMMENT '发送用户id',
  `NREWSTITLE` varchar(255) DEFAULT NULL COMMENT '标题内容',
  `NEWSCONTEXT` varchar(1000) DEFAULT NULL COMMENT '信息内容',
  `SENDSTATE` bigint(18) DEFAULT NULL COMMENT '信息发送状态',
  `NEWSTYPE` bigint(18) DEFAULT NULL COMMENT '信息类型',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `SENDTIME` datetime DEFAULT NULL COMMENT '发送时间',
  `BUSINESSTYPE` bigint(20) DEFAULT NULL COMMENT '业务类型',
  `status` int(10) DEFAULT NULL COMMENT '消息读取状态（0：未读；1：已读）',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '用户名',
  `CONFIGID` varchar(255) DEFAULT NULL COMMENT '消息源类型',
  `ADDRESSTYPE` int(10) DEFAULT NULL COMMENT '0组织，1角色，2用户',
  `ISDISPLAY` int(10) DEFAULT NULL COMMENT '是否显示，0不显示，1显示（组织角色群发已发不显示）-1为假删除',
  `NAMELIST` varchar(255) DEFAULT NULL COMMENT '消息接收人',
  `PARENTID` bigint(20) DEFAULT NULL,
  `RANGE` varchar(255) DEFAULT NULL COMMENT '发送范围',
  PRIMARY KEY (`NEWSID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息管理表';
