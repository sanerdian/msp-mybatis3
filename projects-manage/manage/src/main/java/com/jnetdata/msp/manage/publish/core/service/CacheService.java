package com.jnetdata.msp.manage.publish.core.service;

public interface CacheService {

    /**
     * 初始化模板缓存
     */
    void initSite();

    /**
     * 初始化模板缓存
     */
    void initChannel();

    /**
     * 初始化模板缓存
     */
    void initTemplate();

    /**
     * 初始化系统路径配置缓存
     */
    void initSysPath();
}
