package com.jnetdata.msp.metadata.viewfieldinfo.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.metadata.viewfieldinfo.model.ViewField;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface ViewFieldMapper extends BaseMapper<ViewField> {
    //select viewfieldid from view_dbfieldinfo a where viewid = #{viewid}
    Long[] getVFId(@Param("viewid") Long id);

    @Update("update view_tableinfo set generatetime=null where VIEWID=#{id}")
    void updateViewGen(@Param("id") Long id);
}
