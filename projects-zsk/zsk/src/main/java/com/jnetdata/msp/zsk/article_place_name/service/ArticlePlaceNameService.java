package com.jnetdata.msp.zsk.article_place_name.service;

import com.jnetdata.msp.zsk.article_place_name.model.ArticlePlaceName;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * 文章地名库 服务类
 * </p>
 *
 * @author zyj
 * @since 2021-09-16
 */
public interface ArticlePlaceNameService extends BaseService<ArticlePlaceName> {
        PropertyWrapper<ArticlePlaceName> makeSearchWrapper(Map<String, Object> condition);
        ArticlePlaceName getEntityAndJoinsById(Long id);
        void getListJoin(List<ArticlePlaceName> list, ArticlePlaceName vo);
}
