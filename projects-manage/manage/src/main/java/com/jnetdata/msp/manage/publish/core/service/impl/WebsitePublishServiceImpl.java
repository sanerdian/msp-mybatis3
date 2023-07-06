package com.jnetdata.msp.manage.publish.core.service.impl;

import com.jnetdata.msp.manage.publish.core.common.utils.PublishUtil;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishContextCache;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishVO;
import com.jnetdata.msp.manage.publish.core.publish.PublishManager;
import com.jnetdata.msp.manage.publish.core.service.WebsitePublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/8/7 18:24
 */
@Service
@Slf4j
public class WebsitePublishServiceImpl implements WebsitePublishService {

    @Resource
    private PublishContextCache publishContextCache;

    @Resource
    private PublishManager publishManager;

    @Resource
    private PublishUtil publishUtil;

    @Override
    public void publishIndex(PublishVO publishVO) {
        buildCurrentSiteIdList(publishVO);

        publishManager.publishSiteIndex(publishVO);
    }

    @Override
    public void publishAll(PublishVO publishVO) {
        buildChannelIdList(publishVO);

        publishManager.publishChannelMetadatas(publishVO);

        publishManager.publishChannelIndex(publishVO);

        publishIndex(publishVO);

    }

    @Override
    public void publishIncrement(PublishVO publishVO) {
        publishAll(publishVO);
    }

    @Override
    public void publishCancel(PublishVO publishVO) {
        buildChannelIdList(publishVO);

        publishManager.publishCancelChannelMetadatas(publishVO);

        publishManager.publishCancelChannelIndex(publishVO);

        publishManager.publishCancelChannelIndex(publishVO);
    }

    private void buildChannelIdList(PublishVO publishVO){
        if (publishVO.getSiteList() == null) {
            List<Long> channelIdList = new ArrayList<>();
            publishUtil.readChildrenChannelBySiteId(publishVO.getSiteId(), channelIdList);
            publishVO.setChannelList(channelIdList);
        }
    }

    private void buildCurrentSiteIdList(PublishVO publishVO){
        if (publishVO.getSiteList() == null) {
            List<Long> siteIdList = new ArrayList<>();
            siteIdList.add(publishVO.getSiteId());
            publishVO.setSiteList(siteIdList);
        }
    }
}
