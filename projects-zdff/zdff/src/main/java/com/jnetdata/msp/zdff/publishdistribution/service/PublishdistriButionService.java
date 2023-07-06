package com.jnetdata.msp.zdff.publishdistribution.service;

import cn.hutool.db.PageResult;
import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.zdff.publishdistribution.model.PublishdistriBution;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.List;
import java.util.Map;

public interface PublishdistriButionService extends BaseService<PublishdistriBution> {

//    void editStatus(PublishdistriBution entity);

    /**
     *
     * @title 分页查询--站点分发信息
     * @author liyingnan
     * @param pageNo
     * @param pageSize
     * @param entity
     * @return PageResult
     */
//    PageResult<PublishdistriBution> selectPublishdistriButionBySelection(Integer pageNo, Integer pageSize, PublishdistriBution entity);

    PropertyWrapper<PublishdistriBution> makeSearchWrapper(Map<String, Object> condition);
    PublishdistriBution getEntityAndJoinsById(Long id);
    void getListJoin(List<PublishdistriBution> list, PublishdistriBution vo);

    public List<PublishdistriBution> findPublishdistriBution(Long siteId);
}
