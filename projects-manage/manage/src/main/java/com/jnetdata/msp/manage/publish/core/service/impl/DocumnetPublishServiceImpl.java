package com.jnetdata.msp.manage.publish.core.service.impl;

import com.jnetdata.msp.manage.publish.core.common.vo.PublishVO;
import com.jnetdata.msp.manage.publish.core.publish.PublishManager;
import com.jnetdata.msp.manage.publish.core.service.DocumentPublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/8/7 18:24
 */
@Service
@Slf4j
public class DocumnetPublishServiceImpl implements DocumentPublishService {

    @Resource
    private PublishManager publishManager;

    @Override
    public void publishIndex(PublishVO publishVO) {

    }

    @Override
    public void publishAll(PublishVO publishVO) {
        publishManager.publishChannelMetadatas(publishVO);
    }

    @Override
    public void publishAllBatch(PublishVO publishVO) {

    }

    @Override
    public void publishCancel(PublishVO publishVO) {

    }

    @Override
    public void publishCancelBatch(PublishVO publishVO) {

    }

    @Override
    public void publishPreview(PublishVO publishVO) {

    }
}
