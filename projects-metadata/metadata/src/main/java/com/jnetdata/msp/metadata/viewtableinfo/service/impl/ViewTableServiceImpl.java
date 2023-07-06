package com.jnetdata.msp.metadata.viewtableinfo.service.impl;


import com.jnetdata.msp.metadata.fieldinfo.model.Fieldinfo;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.viewfieldinfo.model.ViewField;
import com.jnetdata.msp.metadata.viewfieldinfo.service.impl.ViewFieldServiceImpl;
import com.jnetdata.msp.metadata.viewtableinfo.mapper.ViewTableMapper;
import com.jnetdata.msp.metadata.viewtableinfo.model.ViewTable;
import com.jnetdata.msp.metadata.viewtableinfo.service.ViewTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation= Propagation.REQUIRED)
public class ViewTableServiceImpl extends BaseServiceImpl<ViewTableMapper,ViewTable> implements ViewTableService {

    @Override
    protected PropertyWrapper<ViewTable> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition).eq("viewname").getWrapper();
    }


    @Autowired
    private ViewFieldServiceImpl viewFieldService;
    /**
     * 创建视图
     * @param
     */
    @Override
    public void createView(Tableinfo tableinfo, List<Fieldinfo> fieldinfoList) {
        ViewTable viewTable=new ViewTable();
        ViewField viewField=null;
        List<String> fieldNames=new ArrayList<>();
            viewTable.setTableviewname(tableinfo.getAnothername());
            viewTable.setViewname(tableinfo.getViewname());
            viewTable.setTableinfoid(tableinfo.getId());
            viewTable.setTablename(tableinfo.getTablename());
            viewTable.setCrtime(new Date());
        for (Fieldinfo fieldinfo : fieldinfoList) {
            fieldNames.add(fieldinfo.getFieldname());
        }
         baseMapper.createView(viewTable,fieldNames);//创建视图
         super.insert(viewTable);//视图表添加信息
        for (Fieldinfo fieldinfo : fieldinfoList) {//添加视图字段信息
            viewField=new ViewField();
            viewField.setViewid(viewTable.getId());
            viewField.setViewname(viewTable.getViewname());
            viewField.setFieldname(fieldinfo.getFieldname());
            viewField.setAnothername(fieldinfo.getAnothername());
            viewField.setFieldtype(fieldinfo.getFieldtype());
            viewField.setFieldname(fieldinfo.getFieldname());
            viewField.setDblength(fieldinfo.getDblength());
            viewField.setDefaultvalue(fieldinfo.getDefaultvalue());
            viewField.setEnmvalue(fieldinfo.getEnmvalue());
            viewField.setNotnull(fieldinfo.getNotbenull());
            viewField.setClassid(fieldinfo.getClassid());
            viewField.setDbscale(fieldinfo.getDbscale());
            viewField.setValidator(fieldinfo.getValidator());
            viewField.setRadorchk(fieldinfo.getRadorchk());
            viewField.setNotedit(fieldinfo.getNotedit());
            viewField.setHiddenfield(fieldinfo.getHiddenfield());
            viewField.setDbtype(fieldinfo.getDbtype());
            viewField.setFieldorder(fieldinfo.getFieldorder());
            viewField.setGroupid(fieldinfo.getGroupid());
            viewField.setNote(fieldinfo.getNote());
            viewField.setClasstype(fieldinfo.getClasstype());
            viewField.setCrtime(new Date());

            viewFieldService.insert(viewField);
        }
    }

    @Override
    public void createView(Tableinfo tableinfo) {
        String sql = "create view "+tableinfo.getTablename()+" as " + tableinfo.getViewsql();
        baseMapper.performSql(sql);
    }

    /**
     * 判断视图是否存在
     * @param viewName
     * @return
     */
    @Override
    public List<String> existsView(String viewName) {
        return baseMapper.existsView(viewName);
    }

    /**
     * 删除视图
     * @param viewName
     */
    @Override
    public void dropView(String viewName) {
         baseMapper.dropView(viewName);
    }

    @Override
    public String viewSql(String viewName) {
        return baseMapper.viewSql(viewName);
    }

    @Override
    public boolean performSql(String sql) {
        return  baseMapper.performSql(sql);
    }

}
