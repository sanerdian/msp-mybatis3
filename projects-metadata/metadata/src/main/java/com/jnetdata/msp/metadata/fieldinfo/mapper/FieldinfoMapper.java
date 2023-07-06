package com.jnetdata.msp.metadata.fieldinfo.mapper;

import com.jnetdata.msp.metadata.fieldinfo.model.Fieldinfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.thenicesys.mybatis.BaseMapper;

import java.util.List;
import java.util.Map;

public interface FieldinfoMapper extends BaseMapper<Fieldinfo> {

    List<String> getTableField(@Param("tablename") String tablename);

    @Update("update tableinfo set generatetime=null where TABLEINFOID=#{id}")
    void updateTableGen(@Param("id") Long id);

    void createField(Fieldinfo entity);

    void updateField(Fieldinfo entity);

    void updateFieldName(Fieldinfo entity);

    void deleteField(Fieldinfo obj);

    @Update("update dbfieldinfo set FIELDORDER = FIELDORDER+1 where TABLEID = #{tableid} and FIELDORDER >= #{fieldorder}")
    void sort(Fieldinfo entity);

    boolean createComment(Fieldinfo entity);
    List<String> selectEr(String tablename);

    List<Map> selectErMap(String tablename);

    List<String> excleEr(String tablename);

    String getMajorKey(String tablename);
    List<String> selectErPhone (String tablename);


}
