package com.jnetdata.msp.metadata.moduleinfo.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.metadata.moduleinfo.model.ModuleView;
import org.apache.ibatis.annotations.Param;


public interface ModuleViewMapper extends BaseMapper<ModuleView> {
    void updateTableOwenerid(@Param("id") Long id, @Param("sid") Long[] ids);
}
