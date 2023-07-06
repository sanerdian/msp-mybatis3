package com.jnetdata.msp.log.template.vo;

import com.jnetdata.msp.log.template.model.TemplateLog;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by TF on 2019/3/13.
 */
@Data
public class TemplateLogVo {
    private PageVo1 pager;

    private TemplateLog entity;
}
