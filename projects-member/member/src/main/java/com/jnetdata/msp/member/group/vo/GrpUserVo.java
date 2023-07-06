package com.jnetdata.msp.member.group.vo;

import com.jnetdata.msp.member.group.model.GrpUser;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by WFJ on 2019/4/1.
 */
@Data
public class GrpUserVo {

    private PageVo1 pager;

    private GrpUser entity;
}
