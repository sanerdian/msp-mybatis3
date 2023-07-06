package com.jnetdata.msp.metadata.fieldinfo.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.metadata.fieldinfo.model.Fieldinfo;
import com.jnetdata.msp.metadata.fieldinfo.vo.FieldinfoVo;
import org.thenicesys.data.api.IQueryWrapper;
import org.thenicesys.data.api.Pager;
import org.thenicesys.data.api.util.ConditionMap;
import org.thenicesys.web.JsonResult;
import org.thenicesys.web.vo.PageVo1;

import java.util.List;
import java.util.Map;

public interface FieldinfoService extends BaseService<Fieldinfo> {

    void deleteByTableinfoId(Long id);

    void updateTableGen(Long id);

    List<Fieldinfo> list(Fieldinfo entity);

    List<String> getTableField(String tablename);

    void createField(Fieldinfo entity);

    boolean updateField(Fieldinfo entity);

    void updateFieldName(Fieldinfo entity);

    void deleteField(Fieldinfo obj);

    void sort(Fieldinfo entity);

    List<Fieldinfo> getByIds(Long[] ids);

    String addField(Fieldinfo entity);

    Pager<Fieldinfo> getFields(PageVo1<Fieldinfo> pager, ConditionMap conditionMap);

    Map<String, List<Fieldinfo>> getGroupFields(Long tableId);

    List<Fieldinfo> lister(Long tableid);

    List<String> selectErtable(String tablename1);

    List<Map> getErtable(String tablename1);

    List<String> excleEr(String tablename);

    String getMajorKey(String tablename);

    List<String> getErPhone(String tablename);
}