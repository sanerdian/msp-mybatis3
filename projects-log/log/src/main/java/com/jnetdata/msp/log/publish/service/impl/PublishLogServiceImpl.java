package com.jnetdata.msp.log.publish.service.impl;

import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.log.publish.mapper.PublishLogMapper;
import com.jnetdata.msp.log.publish.model.PublishLog;
import com.jnetdata.msp.log.publish.service.PublishLogService;
import lombok.val;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

/**
 * Created by TF on 2019/3/13.
 */
@Service
public class PublishLogServiceImpl extends BaseServiceImpl<PublishLogMapper,PublishLog> implements PublishLogService {

    @Override
    protected PropertyWrapper<PublishLog> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("pathName")
                .like("column")
                .like("path")
                .between("createTime","endTime")
                .like("address")
                .getWrapper();
    }

    @Override
    public void addLog(String ip, String filePath, String fileName, String columnName) {
        val user = SessionUser.getCurrentUser();
        PublishLog log = new PublishLog();
        log.setPath(filePath);
        log.setCrUser(user.getName());
        log.setCreateBy(user.getId());
        log.setPathName(fileName);
        log.setColumnname(columnName);
        log.setAddress(ip);
        super.insert(log);
    }
}
