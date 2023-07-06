package com.jnetdata.msp.metadata.viewtableinfo.service;



import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.metadata.fieldinfo.model.Fieldinfo;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.viewtableinfo.model.ViewTable;

import java.util.List;

public interface ViewTableService extends BaseService<ViewTable> {
    //创建视图
    void createView(Tableinfo tableinfo, List<Fieldinfo> fieldinfoList);
    //创建视图
    void createView(Tableinfo tableinfo);

    List<String> existsView(String viewName);

    void dropView(String viewName);

    String viewSql(String viewName);

    boolean performSql(String sql);
}
