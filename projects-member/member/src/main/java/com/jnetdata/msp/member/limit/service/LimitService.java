package com.jnetdata.msp.member.limit.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.member.limit.model.Limit;
import com.jnetdata.msp.member.limit.vo.LimitRerlationTypeVo;

import java.util.List;

/**
 * Created by WFJ on 2019/4/2.
 */
public interface LimitService extends BaseService<Limit> {
    /**
     * 根据 vo查询
     * @param vo
     * @return
     */
    List<Limit> list(Limit vo);
}
