package com.jnetdata.msp.metadata.precise.vo;

import lombok.Data;

import java.util.List;

@Data
/**用于查询人员的标签*/
public class UserTagQueryVo {
    String containsondept;//是包含子部门还是不包含
    List<TreeNodeVo> bms;/*部门或下属机构*/
    List<String> tagNames;/*要查询人员的哪些标签*/
}
