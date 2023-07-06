package com.jnetdata.msp.tlujy.yjfk_photo.service;

import com.jnetdata.msp.tlujy.yjfk_photo.model.YjfkPhoto;
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
 * @since 2020-09-17
 */
public interface YjfkPhotoService extends BaseService<YjfkPhoto> {
        PropertyWrapper<YjfkPhoto> makeSearchWrapper(Map<String, Object> condition);
        YjfkPhoto getEntityAndJoinsById(Long id);
        void getListJoin(List<YjfkPhoto> list, YjfkPhoto vo);
}
