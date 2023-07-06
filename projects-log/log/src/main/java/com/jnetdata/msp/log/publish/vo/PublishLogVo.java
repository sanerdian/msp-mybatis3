package com.jnetdata.msp.log.publish.vo;

import com.jnetdata.msp.log.publish.model.PublishLog;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by TF on 2019/3/13.
 */
@Data
public class PublishLogVo {

    private PageVo1 pager;

    private PublishLog entity;
}
