package com.jnetdata.msp.manage.extendField.vo;

import com.jnetdata.msp.manage.extendField.model.ExtendField;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

@Data
public class ExtendFieldVo {
    private PageVo1 pager;
    private ExtendField entity;
}
