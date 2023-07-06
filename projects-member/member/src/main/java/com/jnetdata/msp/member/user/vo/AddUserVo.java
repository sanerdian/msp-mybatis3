package com.jnetdata.msp.member.user.vo;

import com.jnetdata.msp.member.user.model.User;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by TF on 2019/3/25.
 */
@Data
public class AddUserVo {

    private User entity;

    Long[] groupIds;

}
