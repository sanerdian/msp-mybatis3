package com.jnetdata.msp.media.vo;

import lombok.Data;

@Data
public class PushCondition {
    String field;//字段
    String operator;//操作符
    Object value;//值1
    Object value2;//值2
    String desc;//描述
}
