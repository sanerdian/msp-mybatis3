package com.jnetdata.msp.log.userlogin.vo;

import com.jnetdata.msp.log.userlogin.model.UserLoginLog;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by TF on 2019/3/13.
 */
@Data
public class UserLoginLogVo {
    private PageVo1 pager;

    private UserLoginLog entity;
}
