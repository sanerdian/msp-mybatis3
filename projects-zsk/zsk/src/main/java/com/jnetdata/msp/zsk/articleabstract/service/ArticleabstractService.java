package com.jnetdata.msp.zsk.articleabstract.service;

import com.jnetdata.msp.zsk.articleabstract.model.Articleabstract;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * 文章摘要库 服务类
 * </p>
 *
 * @author zyj
 * @since 2021-09-16
 */
public interface ArticleabstractService extends BaseService<Articleabstract> {
        PropertyWrapper<Articleabstract> makeSearchWrapper(Map<String, Object> condition);
        Articleabstract getEntityAndJoinsById(Long id);
        void getListJoin(List<Articleabstract> list, Articleabstract vo);
}
