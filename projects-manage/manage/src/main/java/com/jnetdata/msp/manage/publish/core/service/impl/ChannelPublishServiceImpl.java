package com.jnetdata.msp.manage.publish.core.service.impl;

import com.jnetdata.msp.manage.publish.core.common.utils.PublishUtil;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishContextCache;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishVO;
import com.jnetdata.msp.manage.publish.core.publish.PublishManager;
import com.jnetdata.msp.manage.publish.core.service.ChannelPublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
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
public class ChannelPublishServiceImpl implements ChannelPublishService {

    @Resource
    private PublishContextCache publishContextCache;

    @Resource
    private PublishManager publishManager;

    @Resource
    private PublishUtil publishUtil;

    @Override
    public void publishIndex(PublishVO publishVO) {
        buildCurrentChannelIdList(publishVO);

        publishManager.publishChannelIndex(publishVO);
    }

    @Override
    public void publishAll(PublishVO publishVO) {
        buildChannelIdList(publishVO);

        publishManager.publishChannelMetadatas(publishVO);

        publishManager.publishChannelIndex(publishVO);
    }

    @Override
    public void publishIncrement(PublishVO publishVO) {
        //暂时使用完全发布
        publishAll(publishVO);
    }

    @Override
    public void publishCancel(PublishVO publishVO) {
        buildChannelIdList(publishVO);

        publishManager.publishCancelChannelMetadatas(publishVO);

        publishManager.publishCancelChannelIndex(publishVO);
    }

    @Override
    public void publishPreview(PublishVO publishVO) {
        buildChannelIdList(publishVO);
        File file;
        for (Long chnlId : publishVO.getChannelList()) {
            file = new File(publishContextCache.getChannelPath().get(chnlId).getFilePath());
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    private void buildChannelIdList(PublishVO publishVO){
        if (publishVO.getChannelList() == null) {
            List<Long> channelIdList = new ArrayList<>();
            publishUtil.readChildrenChannelById(publishVO, channelIdList);
            publishVO.setChannelList(channelIdList);
        }
    }

    private void buildCurrentChannelIdList(PublishVO publishVO){
        if (publishVO.getChannelList() == null) {
            List<Long> channelIdList = new ArrayList<>();
            channelIdList.add(publishVO.getChnlId());
            publishVO.setChannelList(channelIdList);
        }
    }
}
