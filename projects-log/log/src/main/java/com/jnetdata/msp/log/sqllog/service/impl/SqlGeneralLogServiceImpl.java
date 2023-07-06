package com.jnetdata.msp.log.sqllog.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.log.sqllog.mapper.SqlGeneralLogMapper;
import com.jnetdata.msp.log.sqllog.model.SqlGeneralLog;
import com.jnetdata.msp.log.sqllog.service.SqlGeneralLogService;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

@Service
public class SqlGeneralLogServiceImpl extends BaseServiceImpl<SqlGeneralLogMapper, SqlGeneralLog> implements SqlGeneralLogService {
    @Override
    protected PropertyWrapper<SqlGeneralLog> makeSearchWrapper(Map<String, Object> condition) {

        PropertyWrapper<SqlGeneralLog> wrapper = createWrapperUtil(condition)
                .like("userHost")
                .eq("commandType")
                .like("argument")
                .eq("threadId")
                .between("eventTime","endEventTime")
                .getWrapper().like("userHost","fastdev");

     /*  boolean eventTime = condition.containsKey("eventTime");
        boolean endEndEventTime = condition.containsKey("endEventTime");
        if (eventTime || endEndEventTime) {
            if (eventTime && endEndEventTime) {
                wrapper.between("eventTime", condition.get("eventTime"), condition.get("endEventTime"));
            }
            else if (eventTime) {
                wrapper.ge("eventTime", condition.get("eventTime"));
            }
            else {
                wrapper.lt("eventTime", condition.get("endEventTime"));
            }
        }*/

        return wrapper;

    }

}
