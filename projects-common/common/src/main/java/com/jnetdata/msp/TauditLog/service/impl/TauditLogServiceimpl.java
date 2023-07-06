package com.jnetdata.msp.TauditLog.service.impl;

import com.jnetdata.msp.TauditLog.mapper.TauditLogMapper;
import com.jnetdata.msp.TauditLog.model.TauditLogModel;
import com.jnetdata.msp.TauditLog.service.TauditLogService;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import lombok.val;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.net.InetAddress;
import java.util.Date;
import java.util.Map;

/**
 * 功能描述：
 */
@Service
public class TauditLogServiceimpl extends BaseServiceImpl<TauditLogMapper, TauditLogModel> implements TauditLogService {
    @Override
    public PropertyWrapper<TauditLogModel> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("cruser")
                .like("columnName")
                .like("operationStatus")
                .between("crtime","endTime")
                .getWrapper();
    }

    @Override
    public void addLog(Long status, String columnName, String operationStatus) {
        val user = SessionUser.getCurrentUser();
        String address;

        try {
            address = InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            address="127.0.0.1";
        }
        TauditLogModel tauditLogModel = new TauditLogModel();
        tauditLogModel.setCruser(user.getName());
        tauditLogModel.setCruserid(user.getId());
        tauditLogModel.setUptime(new Date());
        tauditLogModel.setUpuser(user.getName());
        tauditLogModel.setUpuserid(user.getId());
        tauditLogModel.setColumnName(columnName);
        tauditLogModel.setCrtime(new Date());
        tauditLogModel.setStatus(status);
        tauditLogModel.setOperationStatus(operationStatus);
        this.insert(tauditLogModel);
    }
}
