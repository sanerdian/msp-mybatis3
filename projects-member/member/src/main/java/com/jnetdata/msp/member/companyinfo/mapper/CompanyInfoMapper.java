package com.jnetdata.msp.member.companyinfo.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.member.companyinfo.model.Companyinfo;
import org.apache.ibatis.annotations.Update;

public interface CompanyInfoMapper extends BaseMapper<Companyinfo>{
    @Update("UPDATE companyinfo SET REGTIME = null where COMPANYID = #{id}")
    void setRegisTimeIsNull(Long id);
}
