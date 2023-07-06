package com.jnetdata.msp.member.group.vo;

import com.jnetdata.msp.member.group.model.GroupModel;
import lombok.Data;

import java.util.List;

/**
 * Created by WFJ on 2019/4/10.
 */

@Data
public class GroupModelVo {

    private List<GroupModel> list;

    private Integer total;
}
