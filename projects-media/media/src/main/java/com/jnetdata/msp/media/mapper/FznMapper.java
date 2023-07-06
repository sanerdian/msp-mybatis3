package com.jnetdata.msp.media.mapper;


import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FznMapper extends org.thenicesys.mybatis.BaseMapper{
    List<Map> pageStyles(@Param("type") int type, @Param("start") int start, @Param("end") int end);
}
