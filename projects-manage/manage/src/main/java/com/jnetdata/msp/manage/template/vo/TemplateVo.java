package com.jnetdata.msp.manage.template.vo;

import com.jnetdata.msp.manage.template.model.Template;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

@Data
public class TemplateVo {
    private PageVo1 pager;

    private Template entity;
}
