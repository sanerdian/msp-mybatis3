package com.jnetdata.msp.log.usermenu.vo;

import com.jnetdata.msp.log.usermenu.model.UserMenuLog;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by TF on 2019/3/13.
 */
@Data
public class UserMenuLogVo {
    private PageVo1 pager;

    private UserMenuLog entity;
}
