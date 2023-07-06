package com.jnetdata.msp.log.usermenu.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.log.usermenu.mapper.UserMenuLogMapper;
import com.jnetdata.msp.log.usermenu.model.UserMenuLog;
import com.jnetdata.msp.log.usermenu.service.UserMenuLogService;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

/**
 * Created by TF on 2019/3/13.
 */
@Service
public class UserMenuLogServiceImpl extends BaseServiceImpl<UserMenuLogMapper,UserMenuLog> implements UserMenuLogService {

    @Override
    protected PropertyWrapper<UserMenuLog> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("content")
                .like("address")
                .like("remark")
                .like("crUser")
                .getWrapper().between(condition.get("startTIme")!=null&&condition.get("endTime")!=null,"crTime",condition.get("startTIme"),condition.get("endTime"));
    }
    @Override
    public String selectSql(String name){
        String s = baseMapper.selectsql(name);
        return s;
    }
}
