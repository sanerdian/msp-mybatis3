package com.jnetdata.msp.media.directive;
/**
 * @title 发布用到常量
 */
public interface PublishConstant {
	//默认的编码,如模板的编码
	String ENCODING_DEFAULT = "UTF-8";
	String INPUT_ENCODING = "UTF-8";
	String OUTPUT_ENCODING = "UTF-8";

	String MEDIA_CONTEXT = "MEDIA_CONTEXT";//上下文，用于跨标签传输数据

	//分页信息
	String MEDIA_PAGE_CURRENT ="MEDIA_PAGE_CURRENT";//分页：当前页码
	String MEDIA_PAGE_SIZE ="MEDIA_PAGE_SIZE";//分页：每页显示记录数
	String MEDIA_PAGE_TOTAL="MEDIA_PAGE_TOTAL";//分页：总的记录数
	String MEDIA_PAGE_URL_FIRST="MEDIA_PAGE_URL_FIRST";//分页：第一页的访问地址，假设第一页为:/a/b/c.html,则第二页为/a/b/c_2.html，第三页为/a/b/c_3.html，依次类推

	String FILE_RESOURCE_LOADER_PATH = "";


	String MEDIA_DOC_ID  = "MEDIA_DOC_ID";//当前文档ID
	String MEDIA_DOC_DATA = "MEDIA_DOC_DATA";//数据：文档
	String MEDIA_DOC_INDEX = "MEDIA_DOC_INDEX";//序号：文档

	String MEDIA_CHANNEL_ID  = "MEDIA_CHANNEL_ID";//当前栏目ID
	String MEDIA_CHANNEL_DATA = "MEDIA_CHANNEL_DATA";//数据：栏目
	String MEDIA_CHANNEL_INDEX = "MEDIA_CHANNEL_INDEX";//序号：栏目

	String MEDIA_SITE_ID   = "MEDIA_SITE_ID";//当前站点ID
	String MEDIA_SITE_DATA = "MEDIA_SITE_DATA";//数据：网站

	int DEFAULT_PAGESIZE = 10;	// 默认每页显示数据条数

	String MEDIA_PUBLISH_TYPE="MEDIA_PUBLISH_TYPE";//1是站点，2是栏目，3是文档，用于区分当前模板拥有者的身份
	int PUBLISH_SITE=1;
	int PUBLISH_CHANNEL=2;
	int PUBLISH_DOC=3;
	int PREVIEW_SITE=11;
	int	PREVIEW_CHANNEL=12;
	int PREVIEW_DOC=13;
}
