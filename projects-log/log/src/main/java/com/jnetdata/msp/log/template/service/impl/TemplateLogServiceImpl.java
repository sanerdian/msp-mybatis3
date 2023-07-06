package com.jnetdata.msp.log.template.service.impl;

import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.log.template.mapper.TemplateLogMapper;
import com.jnetdata.msp.log.template.model.TemplateLog;
import com.jnetdata.msp.log.template.service.TemplateLogService;
import lombok.val;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

/**
 * Created by TF on 2019/3/13.
 */
@Service
public class TemplateLogServiceImpl extends BaseServiceImpl<TemplateLogMapper,TemplateLog> implements TemplateLogService {
    @Override
    protected PropertyWrapper<TemplateLog> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .between("createTime","endTime")
                .like("templateName")
                .like("templateType")
                .like("columnName")
                .like("handleType")
                .like("handleResult")
                .like("detail")
                .like("address")
                .getWrapper();
    }


    @Override
    public void addLog(String str, String tempname, Integer temptype, String requestIp,String columnName) {
        val user = SessionUser.getCurrentUser();
        TemplateLog log = new TemplateLog();
        log.setHandleType(str+"模板");
        log.setTemplateName(tempname);
        log.setColumnName(columnName);
        log.setTemplateType(temptype==1?"概览模板":temptype==2?"细览模板":temptype==3?"嵌套模板":"其他");
        log.setAddress(requestIp);
        log.setCrUser(user.getName());
        log.setCreateBy(user.getId());
        log.setHandleResult(0L);
        super.insert(log);
    }
}
