package com.jnetdata.msp.manage.column.vo;

import lombok.Data;

import java.util.List;

@Data
public class SetLimitVo {
    Long id;
    Long userId;
    List<String> checks;
}
