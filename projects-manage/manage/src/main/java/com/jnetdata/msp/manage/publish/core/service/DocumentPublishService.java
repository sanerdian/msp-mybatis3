package com.jnetdata.msp.manage.publish.core.service;


import com.jnetdata.msp.manage.publish.core.common.vo.PublishVO;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/8/7 18:24
 */
public interface DocumentPublishService {

    public void publishIndex(PublishVO publishVO);

    public void publishAll(PublishVO publishVO);

    public void publishAllBatch(PublishVO publishVO);

    public void publishCancel(PublishVO publishVO);

    public void publishCancelBatch(PublishVO publishVO);

    public void publishPreview(PublishVO publishVO);
}
