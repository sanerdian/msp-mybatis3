package com.jnetdata.msp.member.theclient;

import com.jnetdata.msp.log.content.model.ContentLog;

public interface ContentLogClient {

    /**
     * 添加操作日志
     * @param log
     */
    boolean insert(ContentLog log);

}
