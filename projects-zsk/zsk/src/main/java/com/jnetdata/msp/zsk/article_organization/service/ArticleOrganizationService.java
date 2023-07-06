package com.jnetdata.msp.zsk.article_organization.service;

import com.jnetdata.msp.zsk.article_organization.model.ArticleOrganization;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * 文章机构库 服务类
 * </p>
 *
 * @author zyj
 * @since 2021-09-16
 */
public interface ArticleOrganizationService extends BaseService<ArticleOrganization> {
        PropertyWrapper<ArticleOrganization> makeSearchWrapper(Map<String, Object> condition);
        ArticleOrganization getEntityAndJoinsById(Long id);
        void getListJoin(List<ArticleOrganization> list, ArticleOrganization vo);
}
