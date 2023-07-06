package com.jnetdata.msp.manage.publish.core.service;

import com.jnetdata.msp.manage.publish.core.common.constant.ChannelPublishEnum;
import com.jnetdata.msp.manage.publish.core.common.constant.DocumentPublishEnum;
import com.jnetdata.msp.manage.publish.core.common.constant.WebsitePublishEnum;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishVO;
import org.thenicesys.web.JsonResult;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/8/7 18:24
 */
public interface PublishService {

    /**
     * document文档发布
     * @param publishVO
     * @return
     */
    JsonResult<?>  documentPublish(PublishVO publishVO);

    /**
     * 元数据文档发布
     * @param publishVO
     * @return
     */
    JsonResult<?>  metadataPublish(PublishVO publishVO);

    /**
     * 栏目发布
     * @param publishVO
     * @return
     */
    JsonResult<?>  channelPublish(PublishVO publishVO);

    /**
     * 站点发布
     * @param publishVO
     * @return
     */
    JsonResult<?>  websitePublish(PublishVO publishVO);


    DocumentPublishEnum validateDocumentService(PublishVO publishVO);

    ChannelPublishEnum validateChannelService(PublishVO publishVO);

    WebsitePublishEnum validateWebsiteService(PublishVO publishVO);

    void validateWebsiteTemplate(PublishVO publishVO);

    void validateChannelTemplate(PublishVO publishVO);
}
