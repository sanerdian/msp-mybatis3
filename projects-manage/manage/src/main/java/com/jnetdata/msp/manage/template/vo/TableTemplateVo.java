package com.jnetdata.msp.manage.template.vo;

import com.jnetdata.msp.manage.template.model.TableTemplate;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

@Data
public class TableTemplateVo {
    private PageVo1 pager;

    private TableTemplate entity;
}
