package com.jnetdata.msp.member.limit.vo;

import com.jnetdata.msp.member.limit.model.Limit;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by WFJ on 2019/4/2.
 */
@Data
public class LimitVo {
    private PageVo1 pager;

    private Limit entity;

}
