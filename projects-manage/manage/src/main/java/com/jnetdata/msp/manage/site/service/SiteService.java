package com.jnetdata.msp.manage.site.service;

import com.jnetdata.msp.column.model.Channel;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.manage.site.model.Site;

import java.util.List;

public interface SiteService extends BaseService<Site> {

    boolean updateStatus(String ids,int status);

    List<Site> getDelList();

    List<Site> getTreeData(String webclass);

    List<Site> getTreeDataAsList(String webclass);

    List<Site> getAllTreeData();

    void updateSeq(Site entity);

    /**
     * TODO xuning 待实现
     * 获取站点下全部栏目,栏目必须有模板概览和细览id,如无则直接忽略该栏目下子栏目
     *      如栏目有多级,则每个层级均如此
     *      内部数据按页面展示的栏目顺序  从上(及子栏目) 至下
     * @param webSiteId
     * @return
     */
     List<Channel> getChannelList(Long webSiteId);


}
