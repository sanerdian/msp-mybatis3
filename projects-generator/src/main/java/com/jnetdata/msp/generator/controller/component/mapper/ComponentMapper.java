package com.jnetdata.msp.generator.controller.component.mapper;

import com.jnetdata.msp.generator.controller.component.model.Component;
import com.jnetdata.msp.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface ComponentMapper extends BaseMapper<Component> {
    void updateTableOwenerid(@Param("id") Long id, @Param("sid") Long[] ids);
}
