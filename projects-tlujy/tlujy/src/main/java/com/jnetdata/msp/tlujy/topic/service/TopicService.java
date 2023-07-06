package com.jnetdata.msp.tlujy.topic.service;

import com.jnetdata.msp.tlujy.topic.model.Topic;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyj
 * @since 2020-11-24
 */
public interface TopicService extends BaseService<Topic> {
        PropertyWrapper<Topic> makeSearchWrapper(Map<String, Object> condition);
        Topic getEntityAndJoinsById(Long id);
        void getListJoin(List<Topic> list, Topic vo);
}
