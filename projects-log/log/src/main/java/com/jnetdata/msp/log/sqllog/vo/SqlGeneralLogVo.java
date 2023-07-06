package com.jnetdata.msp.log.sqllog.vo;

import com.jnetdata.msp.log.sqllog.model.SqlGeneralLog;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by TF on 2019/3/26.
 */
@Data
public class SqlGeneralLogVo {
    private PageVo1 pager;

    private SqlGeneralLog entity;
}
