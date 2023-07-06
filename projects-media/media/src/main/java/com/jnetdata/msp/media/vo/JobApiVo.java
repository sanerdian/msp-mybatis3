package com.jnetdata.msp.media.vo;

import com.jnetdata.msp.media.model.JobApi;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;
@Data
public class JobApiVo {
    private PageVo1 pager;

    private JobApi entity;
}
