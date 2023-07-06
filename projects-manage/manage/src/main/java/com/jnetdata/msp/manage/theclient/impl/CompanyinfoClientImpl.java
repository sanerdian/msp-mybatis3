package com.jnetdata.msp.manage.theclient.impl;

import com.jnetdata.msp.member.companyinfo.model.Companyinfo;
import com.jnetdata.msp.member.companyinfo.service.CompanyInfoService;
import com.jnetdata.msp.manage.theclient.CompanyinfoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("com.jnetdata.msp.manage.theclient.impl.CompanyinfoClientImpl")
public class CompanyinfoClientImpl implements CompanyinfoClient {

    // TODO 模块拆分， 先引用原来的jar实现，后续改成微服务
    @Autowired
    private CompanyInfoService companyInfoService;

}
