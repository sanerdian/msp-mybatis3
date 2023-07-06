package com.jnetdata.msp.media.vo;

import lombok.Data;

@Data
public class SimpleUserVo {
    Long id;
    String name;
    String trueName;
    Long groupId;
    String groupName;
}
