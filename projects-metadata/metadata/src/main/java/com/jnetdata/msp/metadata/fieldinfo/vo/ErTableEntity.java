package com.jnetdata.msp.metadata.fieldinfo.vo;

import lombok.Data;

import java.util.List;

@Data
public class ErTableEntity {
    private List<TableEntiy> tableEntiy;
    private List<TableNameVo> tableNameVo;
}
