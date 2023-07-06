package com.jnetdata.msp.metadata.viewtableinfo.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.metadata.viewtableinfo.model.ViewTable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ViewTableMapper extends BaseMapper<ViewTable> {
    void createView(@Param("ViewTable")ViewTable viewTable,@Param("list") List<String> fieldName);

    List<String> existsView(@Param("viewname")String viewName);

    void dropView(@Param("viewname")String viewName);

    String viewSql(@Param("viewname") String viewName);

    boolean performSql(@Param("sql") String sql);
}
