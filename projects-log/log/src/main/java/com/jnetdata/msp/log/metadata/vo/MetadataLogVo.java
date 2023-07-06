package com.jnetdata.msp.log.metadata.vo;

import com.jnetdata.msp.log.metadata.model.MetadataLog;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by TF on 2019/3/13.
 */
@Data
public class MetadataLogVo {

    private PageVo1 pager;

    private MetadataLog entity;
}
