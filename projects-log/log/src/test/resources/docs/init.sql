create database cssn_test default charset=utf8;
use cssn_test;

create table t_user (
     `id` bigint not null comment 'id',
     `name` varchar(50) not null comment '账号名称',
     `passwd` varchar(50) not null comment '登录密码',
     `salt` varchar(50) not null comment '密码盐',
     `person_name` varchar(50) COMMENT '个人姓名',
     `urlpic` varchar(200) COMMENT '个人头像',
     `cellphone` varchar(18) comment '手机号码',
     `cellphone_validated` datetime comment '手机号验证通过时间',
     `email` varchar(20) comment 'email地址',
     `email_validated` datetime comment 'email验证通过时间',
     `register_date` datetime comment '注册时间',
     `last_login_date` datetime comment '最后登录时间',
     `member_type`  varchar(20)  comment '会员类型',
      `effective_time` datetime comment '生效日期',
      `expiry_time` datetime comment '有效期',
     `locked` bit not null comment '状态(true: 停用; false: 启用)',
     `nick_name`  varchar(50)  comment '昵称',
     `sex` varchar(1)  comment '性别',
     `birthday` datetime comment '生日',
     `province`  varchar(100)  comment '省',
     `city` varchar(100)  comment '市',
     `county` varchar(100) comment '县',
     `detailed_address`  varchar(255) comment '详细地址',
     `company_id` bigint comment '所属公司Id',
     `company_type` bigint comment '冗余字段, 所属公司类型',
     `company_abbr` varchar(20) comment '单位简称，专用做水印',
     `create_by` bigint comment '创建人Id' comment '最后登录时间',
     `create_time` datetime  comment '创建时间' comment '最后登录时间',
     `modify_by` bigint  comment '最后修改人Id' comment '最后登录时间',
     `modify_time` datetime  comment '最后修改时间' comment '最后登录时间',
     PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';
ALTER TABLE t_user ADD UNIQUE index_user_unique1(`name`);
ALTER TABLE t_user ADD UNIQUE index_user_unique2(`cellphone`);
ALTER TABLE t_user ADD UNIQUE index_user_unique3(`email`);

create table t_role (
     `id` bigint not null auto_increment comment 'id',
     `name` varchar(40) not null comment '角色名',
     `description` varchar(50) comment '描述',
     `create_by` bigint comment '创建人Id',
     `create_time` datetime  comment '创建时间',
     `modify_by` bigint  comment '最后修改人Id',
     `modify_time` datetime  comment '最后修改时间',
     PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';
ALTER TABLE t_role ADD UNIQUE index_role_unique1(`name`);

create table t_user_role (
     `id` bigint not null comment 'id',
     `user_id` bigint not null comment '用户Id',
     `role_id` bigint not null comment '角色Id',
     `create_by` bigint comment '创建人Id',
     `create_time` datetime  comment '创建时间',
     `modify_by` bigint  comment '最后修改人Id',
     `modify_time` datetime  comment '最后修改时间',
     PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联关系表';
ALTER TABLE t_user_role ADD UNIQUE index_userrole_unique1(`user_id`,`role_id`);

create table t_permission (
     `id` bigint not null auto_increment comment 'id',
     `permission` varchar(50) not null comment '权限',
     `description` varchar(50) comment '描述',
     `create_by` bigint comment '创建人Id',
     `create_time` datetime  comment '创建时间',
     `modify_by` bigint  comment '最后修改人Id',
     `modify_time` datetime  comment '最后修改时间',
     PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';
ALTER TABLE t_permission ADD UNIQUE index_permission_unique1(`permission`);

create table t_acl (
     `id` bigint not null auto_increment comment 'id',
     `role_id` bigint not null comment '角色Id',
     `permission_id` bigint not null comment '权限Id',
     `create_by` bigint comment '创建人Id',
     `create_time` datetime  comment '创建时间',
     `modify_by` bigint  comment '最后修改人Id',
     `modify_time` datetime  comment '最后修改时间',
     PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='acl表';
ALTER TABLE t_acl ADD UNIQUE index_acl_unique1(`role_id`,`permission_id`);


create table t_invoice_template (
     `id` bigint not null comment 'id',
     `user_id` bigint not null COMMENT '账号Id',
     `invoice_type` varchar(20) not null,
     `enterprise_name` varchar(40),
     `social_code` varchar(100),
     `address` varchar(100),
     `cellphone` varchar(20),
     `create_by` bigint comment '创建人Id',
     `create_time` datetime  comment '创建时间',
     `modify_by` bigint  comment '最后修改人Id',
     `modify_time` datetime  comment '最后修改时间',
     PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发票模板';

CREATE TABLE t_delivery_address_template (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `user_id` bigint(20) NOT NULL,
  `country` varchar(100) NOT NULL COMMENT '国家',
  `province` varchar(100) NOT NULL COMMENT '省',
  `city` varchar(100) NOT NULL COMMENT '市',
  `county` varchar(100) NOT NULL COMMENT '县',
  `incepter` varchar(2000) NOT NULL COMMENT '收货人名称',
  `address` varchar(4000) NOT NULL COMMENT '收货地址',
  `postcode` varchar(100) DEFAULT NULL COMMENT '邮编',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮件',
  `fax` varchar(1000) DEFAULT NULL COMMENT '传真',
  `telephone` varchar(1000) DEFAULT NULL COMMENT '电话',
  `cellphone` varchar(1000) DEFAULT NULL COMMENT '手机',
  `the_default` int(10) NOT NULL COMMENT '是否默认地址',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='买家收货地址模板表';

create table  t_parameter (
     `id` bigint not null comment 'id',
     `type` varchar(50) not null comment '参数类型',
     `code` varchar(30) not null comment '参数代码',
     `cname` varchar(30) not null comment '参数中文名',
     `order_no` int  not null COMMENT '显示顺序',
    `ext_info` varchar(50) COMMENT '附加信息',
     `status` int not null COMMENT '状态',
     `create_by` bigint comment '创建人Id',
     `create_time` datetime  comment '创建时间',
     `modify_by` bigint  comment '最后修改人Id',
     `modify_time` datetime  comment '最后修改时间',
     PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='参数表';
ALTER TABLE t_parameter ADD UNIQUE index_parameter_unique1(`type`,`code`);

create table t_data_collect (
     `id` bigint not null comment 'id',
     `user_id` bigint not null comment '账号Id',
     `user_name` varchar(100) not null comment '账号名称',
     `a001` varchar(100) comment '标准唯一标识符',
     `a100` varchar(100) comment '标准号',
     `a298` varchar(255) comment '标准中文名称',
     `a302` varchar(255) comment '标准英文名称',
     `a000` varchar(10) comment '标准状态',
     `book_number` varchar(20) comment '书号',
     `book_name` varchar(200) comment '书名',
     `author_first_name` varchar(50) comment '作者',
     `type_of_collect` varchar(20) comment '收藏类型：standard or book',
     `class_id` bigint comment '收藏分类ID',
     `collect_time` datetime comment '收藏时间',
     `create_by` bigint comment '创建人Id',
     `create_time` datetime  comment '创建时间',
     `modify_by` bigint  comment '最后修改人Id',
     `modify_time` datetime  comment '最后修改时间',
     PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='我的收藏';


/*创建收藏分类表时，需添加一条默认收藏数据*/
create table `t_collect_classification` (
  `id` bigint(20) not null comment 'id',
  `user_id` bigint(20) default null comment '账号id',
  `name` varchar(20) not null comment '账号名称',
  `create_by` bigint(20) default null comment '创建人id',
  `create_time` datetime default null comment '创建时间',
  `modify_by` bigint(20) default null comment '最后修改人id',
  `modify_time` datetime default null comment '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收藏分类';
insert into `t_collect_classification` values ('1', null, '默认收藏', null, null, null, null);


create table t_data_track (
     `id` bigint not null comment 'id',
     `user_id` bigint not null comment '账号Id',
     `user_name` varchar(100) not null comment '账号名称',
     `a001` varchar(100) comment '标准唯一标识符',
     `a100` varchar(100) comment '标准号',
     `a298` varchar(255) comment '标准中文名称',
     `a302` varchar(255) comment '标准英文名称',
     `a000` varchar(10) comment '标准状态',
     `track_time` datetime comment '跟踪时间',
     `create_by` bigint comment '创建人Id',
     `create_time` datetime  comment '创建时间',
     `modify_by` bigint  comment '最后修改人Id',
     `modify_time` datetime  comment '最后修改时间',
     PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='标准跟踪';

create table t_amount_card (
     `id` bigint not null comment 'id',
     `user_id` bigint not null COMMENT '账号Id',
     `user_name` varchar(20) not null COMMENT '账号名称',
     `amount` bigint not null COMMENT '金额',
     `active` bit not null COMMENT '是否激活',
     `create_by` bigint comment '创建人Id',
     `create_time` datetime  comment '创建时间',
     `modify_by` bigint  comment '最后修改人Id',
     `modify_time` datetime  comment '最后修改时间',
      PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='余额卡';


create table t_amount_card_record (
    `id` bigint not null ,
    `user_id` bigint not null COMMENT '账号Id',
    `amount_card_id` bigint not null COMMENT '余额卡Id',
    `record_type` varchar(255) not null COMMENT '操作类型',
    `sign` int not null COMMENT '金额符号(1/0/-1)',
    `amount` bigint not null comment '金额',
    `description` varchar(255) null ,
    `status` varchar(255) null ,
    `related_type` varchar(255) null ,
    `related_orderno` varchar(255) null ,
    `operator` varchar(255) not null comment '操作人',
    `operate_time` datetime not null,
    `create_by` bigint comment '创建人Id',
     `create_time` datetime  comment '创建时间',
     `modify_by` bigint  comment '最后修改人Id',
     `modify_time` datetime  comment '最后修改时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='余额卡记录';

create table t_amount_trade_flow_record (
    `id` bigint not null ,
    `user_id` bigint not null COMMENT '账号Id',
     `user_name` varchar(20) not null COMMENT '账号名称',
     `type` varchar(255) not null COMMENT '交易类型',
    `from_amount` bigint not null COMMENT '交易账号',
    `sign` int not null COMMENT '金额符号(1/0/-1)',
    `amount` bigint not null comment '金额',
    `description` varchar(255) null ,
    `related_type` varchar(255) null ,
    `related_orderno` varchar(255) null ,
    `operator` varchar(255) not null comment '操作人',
    `trade_time` datetime not null,
    `create_by` bigint comment '创建人Id',
     `create_time` datetime  comment '创建时间',
     `modify_by` bigint  comment '最后修改人Id',
     `modify_time` datetime  comment '最后修改时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资金交易流水表';


create table t_arrival_reminder (
     `id` bigint not null comment 'id',
     `user_id` bigint not null COMMENT '账号Id',
     `user_name` varchar(100) not null COMMENT '账号名称',
     `a001` varchar(100) comment '标准唯一标识符',
     `a100` varchar(100) comment '标准号',
     `a298` varchar(255) comment '标准中文名称',
     `a302` varchar(255) comment '标准英文名称',
     `record_time` datetime comment '到货提醒添加时间',
     `create_by` bigint comment '创建人Id',
     `create_time` datetime  comment '创建时间',
     `modify_by` bigint  comment '最后修改人Id',
     `modify_time` datetime  comment '最后修改时间',
     PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='到货提醒';

create table t_company (
     `id` bigint not null comment 'id',
     `company_type` bigint not null comment '公司类型',
     `license` varchar(200) comment '营业执照',
     `name` varchar(100) not null comment '公司名称',
     `name_abbr` varchar(100) comment '公司名称简称',
     `linkman` varchar(50) comment '联系人',
     `tel` varchar(50) comment '公司联系电话',
     `address` varchar(500) comment '公司地址',
     `authentication` varchar(10) comment '认证结果',
     `audit_date` datetime  comment '审核日期',
     `register_time` datetime  comment '注册时间',
     `create_by` bigint comment '创建人Id',
     `create_time` datetime  comment '创建时间',
     `modify_by` bigint  comment '最后修改人Id',
     `modify_time` datetime  comment '最后修改时间',
     PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公司';
ALTER TABLE t_company ADD UNIQUE index_company_unique1(`name`);
ALTER TABLE t_company ADD UNIQUE index_company_unique2(`name_abbr`);