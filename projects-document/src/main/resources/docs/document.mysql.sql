
DROP TABLE IF EXISTS `f_documents`;

CREATE TABLE `f_documents` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `content_type` varchar(10) NOT NULL COMMENT '类型',
  `description` varchar(300) NOT NULL COMMENT '文档描述',
  `pathname` varchar(200) NOT NULL COMMENT '上传文件保存路径',
  `url` varchar(200) NOT NULL COMMENT 'url路径',
   `create_by` bigint COMMENT '创建人',
   `create_time` datetime COMMENT '创建时间',
   `modify_by` bigint COMMENT '最后修改人',
   `modify_time` datetime COMMENT '最后修改时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文档';
