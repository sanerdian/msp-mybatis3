package com.jnetdata.msp.visual.log.service.impl;

import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.log.template.model.TemplateLog;
import com.jnetdata.msp.visual.log.mapper.ElementMapper;
import com.jnetdata.msp.visual.log.model.ElementModel;
import com.jnetdata.msp.visual.log.service.ElementService;
import lombok.val;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.net.InetAddress;
import java.util.Map;

/**
 * 功能描述：
 */
@Service
public class ElementServiceImpl extends BaseServiceImpl<ElementMapper, ElementModel> implements ElementService {
    @Override
    protected PropertyWrapper<ElementModel> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .between("crtime","endTime")
                .like("templateName")
                .like("templateType")
                .like("operation")
                .like("cruser")
                .eq("cruserid")
                .eq("id")
                .getWrapper();
    }
    @Override
    public void AddLog(String templateName,String operation) {
        val user = SessionUser.getCurrentUser();
        String address;
        try {
            address = InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            address="127.0.0.1";
        }
        ElementModel elementModel = new ElementModel();
        elementModel.setTemplateName(templateName);
        elementModel.setIpadress(address);
        elementModel.setCruser(user.getName());
        elementModel.setCruserid(user.getId());
        elementModel.setOperation(operation);
        /*elementModel.setTemplateName(templateName);
        elementModel.setTemplateType(templateType);*/
        this.insert(elementModel);
    }
}
