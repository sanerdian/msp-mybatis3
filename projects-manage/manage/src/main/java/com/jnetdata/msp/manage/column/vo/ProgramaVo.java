package com.jnetdata.msp.manage.column.vo;

import com.jnetdata.msp.manage.column.model.Programa;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by WFJ on 2019/4/26.
 */
@Data
public class ProgramaVo {
    private PageVo1 pager;

    private Programa entity;
}
