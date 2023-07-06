package com.jnetdata.msp.zsk.article_number1.service;

import com.jnetdata.msp.zsk.article_number1.model.ArticleNumber1;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * 文章文号库 服务类
 * </p>
 *
 * @author zyj
 * @since 2021-09-16
 */
public interface ArticleNumber1Service extends BaseService<ArticleNumber1> {
        PropertyWrapper<ArticleNumber1> makeSearchWrapper(Map<String, Object> condition);
        ArticleNumber1 getEntityAndJoinsById(Long id);
        void getListJoin(List<ArticleNumber1> list, ArticleNumber1 vo);
}
