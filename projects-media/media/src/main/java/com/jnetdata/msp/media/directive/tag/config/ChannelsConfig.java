package com.jnetdata.msp.media.directive.tag.config;

import lombok.Data;

@Data
public class ChannelsConfig extends BaseConfigList {
	Long siteid;//站点id
	String chnlname;//栏目名称，模糊查询
	Integer status;//栏目状态，逻辑删除用(0:启用,1:停用)
	Long parentid;//父栏目id
	/**
	 * 要查询的栏目列表的直接拥有者
	 * self：传入的栏目id所对应的栏目的直接子栏目
	 * site：传入的栏目id所对应的栏目的直接子栏目
	 */
	String owner;
}
