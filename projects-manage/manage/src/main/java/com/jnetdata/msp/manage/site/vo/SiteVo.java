package com.jnetdata.msp.manage.site.vo;

import com.jnetdata.msp.manage.site.model.Site;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by WFJ on 2019/4/26.
 */
@Data
public class SiteVo {
    private PageVo1 pager;

    private Site entity;
}
