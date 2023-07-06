package com.jnetdata.msp.member.role.vo;

import com.jnetdata.msp.member.role.model.RoleModel;
import lombok.Data;

import java.util.List;

/**
 * Created by WFJ on 2019/4/8.
 */
@Data
public class RoleModelVo {

    private List<RoleModel> list;

    private Integer total;


}
