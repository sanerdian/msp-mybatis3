package com.jnetdata.msp.metadata.tnetwork.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface ExcellenttMapper extends BaseMapper {
    @Select("select * from ${s} where id in (${set2})")
    List<Map> selectall(@Param("s") String s, @Param("set2") String set2);
}
