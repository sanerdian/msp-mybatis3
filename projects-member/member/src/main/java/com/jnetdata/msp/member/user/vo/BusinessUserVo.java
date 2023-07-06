package com.jnetdata.msp.member.user.vo;

import com.jnetdata.msp.member.business.model.Business;
import com.jnetdata.msp.member.user.model.User;
import lombok.Data;

/**
 * Created by TF on 2019/3/25.
 */
@Data
public class BusinessUserVo {

    private User user;

    private Business business;

}
