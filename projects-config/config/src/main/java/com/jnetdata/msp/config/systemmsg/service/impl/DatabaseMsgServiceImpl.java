package com.jnetdata.msp.config.systemmsg.service.impl;

import com.jnetdata.msp.config.systemmsg.mapper.DatabaseMsgMapper;
import com.jnetdata.msp.config.systemmsg.model.DatabaseMsg;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.config.systemmsg.service.DatabaseMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;

/**
 * Created by TF on 2019/3/27.
 */
@Slf4j
@Service
public class DatabaseMsgServiceImpl extends BaseServiceImpl<DatabaseMsgMapper, DatabaseMsg> implements DatabaseMsgService {

    @Autowired
    DataSource dataSource;


    @Override
    public DatabaseMsg getMsg() throws Exception{


        DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource);
        DatabaseMetaData metaData = manager.getDataSource().getConnection().getMetaData();
        DatabaseMsg msg = new DatabaseMsg();

        msg.setType(metaData.getDatabaseProductName());//数据库类型
        msg.setVersion(metaData.getDatabaseProductVersion());//数据库版本
        msg.setSymbol(metaData.getIdentifierQuoteString());//数据库转义符
        msg.setDrive(metaData.getDriverVersion());//数据库驱动版本
        msg.setAddress(metaData.getURL());//数据库地址
        msg.setUserName(metaData.getUserName());//用户名

        return msg;
    }
}
