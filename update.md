1.0.2 -> 1.1.6
run
alter table userinfo add EDUCATIONBACKGROUND varchar(255);
alter table userinfo add POLITICSSTATUS varchar(255);
alter table companyinfo add PARENT_ID bigint(20);
alter table companyinfo add PARENT_NAME varchar(50);
update companyinfo set PARENT_ID = 0, PARENT_NAME = '根机构' where PARENT_ID is null;
alter table limit_type add TYPE  varchar(20);
alter table limit_type add TABLE_ID bigint(20);
alter table limit_type add TABLE_NAME varchar(20);
alter table visual_template add HTML_CODE_PURE varchar(20);
alter table visual_template add MODULE_COUNT varchar(20);
alter table visual_template add FREE_HTML_CODE varchar(20);
alter table tableinfo add DBSOURCEID bigint(20);
alter table tableinfo add VIEWSQL text;
alter table class add BMNAME varchar(255);
alter table class add MATCHG varchar(255);
alter table class add METHODID varchar(255);
alter table menuman add SITEID bigint(20);



复制其他可视化表

CREATE TABLE `encoding` (
`id` bigint(11) NOT NULL,
`bmname` varchar(255) DEFAULT NULL COMMENT '编码规则名称',
`bmstyle` varchar(255) DEFAULT NULL COMMENT '编码规则样式',
`stutas` int(20) DEFAULT NULL COMMENT '使用状态',
`crtime` datetime DEFAULT NULL COMMENT '创建时间',
`cruser` varchar(255) DEFAULT NULL COMMENT '创建人',
`typee` int(20) DEFAULT NULL,
`cumulative` int(20) DEFAULT NULL COMMENT '累计方式：0自动累加，1按年累计，2按月累加，3按日累加',
`digit` int(20) DEFAULT NULL COMMENT '编号位数',
`nummber` int(20) DEFAULT NULL COMMENT '当前编号',
`prefix` varchar(255) DEFAULT NULL COMMENT '编码前缀',
PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `encoding`(`id`, `bmname`, `bmstyle`, `stutas`, `crtime`, `cruser`, `typee`, `cumulative`, `digit`, `nummber`, `prefix`) VALUES (1622862361552293890, '中科聚网', 'yyyyMMdd##', 0, '2023-02-07 15:38:33', 'admin', 0, 0, 5, 0, 'ZKJU');



CREATE TABLE `j_dbsource` (
`ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
`CRUSER` varchar(255) DEFAULT NULL,
`CRTIME` datetime DEFAULT NULL,
`CRNUMBER` bigint(20) DEFAULT NULL,
`STATUS` smallint(6) DEFAULT '0',
`MODIFY_BY` bigint(20) DEFAULT NULL,
`MODIFY_USER` varchar(255) DEFAULT NULL,
`SEQENCING` int(10) DEFAULT NULL,
`MODIFY_TIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
`NAME` varchar(100) DEFAULT NULL COMMENT '名称',
`IP` varchar(20) DEFAULT NULL COMMENT 'ip',
`PORT` bigint(10) DEFAULT NULL COMMENT '端口',
`ACOUNT` varchar(255) DEFAULT NULL COMMENT '账号',
`PW` varchar(255) DEFAULT NULL COMMENT '密码',
`TYPE` varchar(10) DEFAULT NULL COMMENT '数据源类型',
PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='eslink';

run/src/main/java/com/jnetdata/msp/config/CustomShiroFilterFactoryBean.java
run/src/main/java/com/jnetdata/msp/config/FlowableConfig.java
run/src/main/java/com/jnetdata/msp/config/ShiroSessionManager.java
run/src/main/java/com/jnetdata/msp/manage/
run/src/main/java/com/jnetdata/msp/metadata/tableinfo/
run/src/main/java/com/org/
run/src/main/webapp1/


权限修改:添加模块,元数据权限,重启权限
   如果项目升级后admin账号元数据模块,元数据表数量变少,执行sql添加权限(如果admin的用户id不是1,替换一下owner_id):
   INSERT INTO `j_permission`(`ID`, `OWNER_TYPE`, `OWNER_ID`, `ISURL`, `PERMISSION`, `DESCRIPTION`, `CRUSER`, `CRNUMBER`, `CRTIME`, `MODIFY_BY`, `MODIFY_TIME`, `MODIFY_USER`) VALUES (7, 0, 1, NULL, 'metadataModule', NULL, NULL, NULL, '2022-12-12 09:12:39', NULL, '2022-12-12 09:12:39', NULL);
   INSERT INTO `j_permission`(`ID`, `OWNER_TYPE`, `OWNER_ID`, `ISURL`, `PERMISSION`, `DESCRIPTION`, `CRUSER`, `CRNUMBER`, `CRTIME`, `MODIFY_BY`, `MODIFY_TIME`, `MODIFY_USER`) VALUES (8, 0, 1, NULL, 'manage', NULL, NULL, NULL, '2022-12-12 09:12:39', NULL, '2022-12-12 09:12:39', NULL);
生成代码的jar包版本固定为1.0.0,不需要跟随系统升级