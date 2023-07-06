package com.jnetdata.msp.zsk.name_post.service;

import com.jnetdata.msp.zsk.name_post.model.NamePost;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * 人名职务库 服务类
 * </p>
 *
 * @author zyj
 * @since 2021-09-16
 */
public interface NamePostService extends BaseService<NamePost> {
        PropertyWrapper<NamePost> makeSearchWrapper(Map<String, Object> condition);
        NamePost getEntityAndJoinsById(Long id);
        void getListJoin(List<NamePost> list, NamePost vo);
}
