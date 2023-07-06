package com.jnetdata.msp.member.limit.vo;

import com.jnetdata.msp.member.limit.model.Limit;
import lombok.Data;

import java.util.List;

/**
 * Created by WFJ on 2019/4/19.
 */
@Data
public class LimitRerlationTypeVo {
    //权限集合
    private List<Limit> list;
    //分类名称
    private String name;


}
