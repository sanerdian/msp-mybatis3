package com.jnetdata.msp.manage.column.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.manage.column.model.Programa;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

public interface PicturewqMapper {
    @Delete("DELETE FROM j_file WHERE COLUMNID=#{columnid} and SITEID=#{siteId}")
    Boolean Delectb(@Param("columnid") Long columnid,@Param("siteId") Long siteId);

}
