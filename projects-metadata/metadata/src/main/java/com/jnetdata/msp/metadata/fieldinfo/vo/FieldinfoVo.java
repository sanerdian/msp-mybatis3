package com.jnetdata.msp.metadata.fieldinfo.vo;

import com.jnetdata.msp.metadata.fieldinfo.model.Fieldinfo;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

@Data
public class FieldinfoVo {
    private PageVo1 pager;
    private Fieldinfo entity;
}
