package com.jnetdata.msp.zsk.topicarea.service;

import com.jnetdata.msp.zsk.topicarea.model.Topicarea;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * 主题词-地名库 服务类
 * </p>
 *
 * @author zyj
 * @since 2021-09-13
 */
public interface TopicareaService extends BaseService<Topicarea> {
        PropertyWrapper<Topicarea> makeSearchWrapper(Map<String, Object> condition);
        Topicarea getEntityAndJoinsById(Long id);
        void getListJoin(List<Topicarea> list, Topicarea vo);
}
