package com.jnetdata.msp.media.vo.push;

import lombok.Data;

import java.util.List;

@Data
/**用于查询人员的标签*/
public class UserTagQueryVo {
    List<TreeNodeVo> bms;/*部门或下属机构*/
    List<String> tagNames;/*要查询人员的哪些标签*/
}
