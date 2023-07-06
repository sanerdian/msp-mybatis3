package com.jnetdata.msp.metadata.fieldinfo.vo;

import lombok.Data;

import java.util.List;

@Data
public class TableNameVo {
    private String tablename;
    private String tableChinaName;
    private List<FormNameVo> tablelist;
}
