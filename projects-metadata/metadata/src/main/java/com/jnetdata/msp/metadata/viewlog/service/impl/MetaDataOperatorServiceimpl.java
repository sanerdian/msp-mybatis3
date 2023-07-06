package com.jnetdata.msp.metadata.viewlog.service.impl;

import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.log.usermenu.model.UserMenuLog;
import com.jnetdata.msp.metadata.viewfieldinfo.mapper.ViewFieldMapper;
import com.jnetdata.msp.metadata.viewfieldinfo.model.ViewField;
import com.jnetdata.msp.metadata.viewfieldinfo.service.ViewFieldService;
import com.jnetdata.msp.metadata.viewlog.mapper.MetaDataOperatorMapper;
import com.jnetdata.msp.metadata.viewlog.model.MetaDataOperator;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.mybatis.impl.PropertyWrapper;


import java.net.InetAddress;

import java.util.Date;
import java.util.Map;

/**
 * 功能描述：
 */

@Service
@Transactional(propagation= Propagation.REQUIRED)
public class MetaDataOperatorServiceimpl extends BaseServiceImpl<MetaDataOperatorMapper, MetaDataOperator> implements com.jnetdata.msp.metadata.viewlog.service.MetaDataOperatorService {
    @Override
    protected PropertyWrapper<MetaDataOperator> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .eq("id")
                .like("username")
                .eq("userid")
                .like("handletype")
                .between("crtime","endTime")
                .like("title")
                .getWrapper();
    }
    @Override
    public void addLog(String handletype,String title){
        MetaDataOperator metaDataOperator = new MetaDataOperator();
        val user = SessionUser.getCurrentUser();
        metaDataOperator.setUsername(user.getName());
        metaDataOperator.setUserid(user.getId());
        metaDataOperator.setTitle(title);
        metaDataOperator.setHandletype(handletype);
        metaDataOperator.setCrtime(new Date());
        this.insert(metaDataOperator);
    }


    @Override
    public void AddLog(String handletype, String title) {
        val user = SessionUser.getCurrentUser();
        String address;
        try {
            address = InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            address="127.0.0.1";
        }
        MetaDataOperator metaDataOperator = new MetaDataOperator();
        metaDataOperator.setUserid(user.getId());
        metaDataOperator.setUsername(user.getName());
        metaDataOperator.setIpaddress(address);
        metaDataOperator.setTitle(title);
        metaDataOperator.setHandletype(handletype);
        metaDataOperator.setCrtime(new Date());
        this.insert(metaDataOperator);
    }
}
