package com.jnetdata.msp.manage.publish.core.system.runner;

import com.jnetdata.msp.manage.publish.core.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Order(value = 1)
@Slf4j
public class CacheInitRunner implements ApplicationRunner {

    @Resource
    CacheService cacheService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //初始化缓存
        cacheService.initSysPath();
        cacheService.initTemplate();
        cacheService.initSite();
        cacheService.initChannel();
        log.info("缓存加载完毕");

    }
}
