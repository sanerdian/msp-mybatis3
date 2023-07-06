package com.jnetdata.msp.member.role.vo;

import com.jnetdata.msp.member.role.model.Role;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by WFJ on 2019/4/2.
 */
@Data
public class RoleVo {
    private PageVo1 pager;

    private Role entity;
}
