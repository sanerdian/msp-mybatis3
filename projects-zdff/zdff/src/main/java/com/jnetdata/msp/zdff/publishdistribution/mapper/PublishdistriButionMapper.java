package com.jnetdata.msp.zdff.publishdistribution.mapper;


import com.jnetdata.msp.zdff.publishdistribution.model.PublishdistriBution;
import com.jnetdata.msp.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author zyj
 * @since 2021-01-18
 */
public interface PublishdistriButionMapper extends BaseMapper<PublishdistriBution> {
    /*站点分发按站点统计*/
    List<PublishdistriBution> findPublishdistriBution(@Param("siteId") long siteId);
}
