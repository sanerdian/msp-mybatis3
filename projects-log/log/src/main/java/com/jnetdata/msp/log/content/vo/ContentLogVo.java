package com.jnetdata.msp.log.content.vo;

import com.jnetdata.msp.log.content.model.ContentLog;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by TF on 2019/3/13.
 */
@Data
public class ContentLogVo {

    private PageVo1 pager;

    private ContentLog entity;
}
