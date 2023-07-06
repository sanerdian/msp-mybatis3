package com.jnetdata.msp.media.directive.tag.config;

import lombok.Data;

/**
 * @title 文档列表配置
 */
@Data
public class DocumentsConfig extends BaseConfigList {
	Long channelid;
	Long siteid;
	String title;
	String status="21";//状态：发布21、撤稿25
	Long docstatus=0L;//删除状态：0正常1删除
}
