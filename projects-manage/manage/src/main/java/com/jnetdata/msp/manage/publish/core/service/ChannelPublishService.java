package com.jnetdata.msp.manage.publish.core.service;


import com.jnetdata.msp.manage.publish.core.common.vo.PublishVO;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/8/20 16:38
 */
public interface ChannelPublishService {

    public void publishIndex(PublishVO publishVO);

    public void publishAll(PublishVO publishVO);

    public void publishIncrement(PublishVO publishVO);

    public void publishCancel(PublishVO publishVO);

    public void publishPreview(PublishVO publishVO);
}
