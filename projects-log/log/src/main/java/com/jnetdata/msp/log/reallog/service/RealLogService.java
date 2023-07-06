package com.jnetdata.msp.log.reallog.service;

import java.util.List;
import java.util.Map;

/**
 * Created by TF on 2019/3/28.
 */
public interface RealLogService {

    Map<String,Object> getLog(int pointer);

    List<String> readLog();
}
