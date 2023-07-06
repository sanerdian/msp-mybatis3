package com.jnetdata.msp.media.vo.survey;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class InvestigateQueryVo {
    String siteid;
    Long status;
    String title;
    List<Date> createTime;
}
