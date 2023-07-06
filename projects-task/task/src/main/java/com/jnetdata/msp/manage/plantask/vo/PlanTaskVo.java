package com.jnetdata.msp.manage.plantask.vo;

import com.jnetdata.msp.manage.plantask.model.PlanTask;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

@Data
public class PlanTaskVo {
    private PageVo1 pager;

    private PlanTask entity;
}
