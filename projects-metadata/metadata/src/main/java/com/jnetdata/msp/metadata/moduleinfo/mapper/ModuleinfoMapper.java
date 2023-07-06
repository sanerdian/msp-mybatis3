package com.jnetdata.msp.metadata.moduleinfo.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.metadata.moduleinfo.model.Moduleinfo;
import org.apache.ibatis.annotations.Select;


public interface ModuleinfoMapper extends BaseMapper<Moduleinfo> {
    @Select("select max(moduleorder) from moduleinfo")
    Integer getmaxorder();
}
