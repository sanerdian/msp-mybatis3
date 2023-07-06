package com.jnetdata.msp.config.configtype.vo;

import com.jnetdata.msp.config.configtype.model.ConfigType;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by TF on 2019/3/26.
 */
@Data
public class ConfigTypeVo {

    private PageVo1 pager;

    private ConfigType entity;
}
