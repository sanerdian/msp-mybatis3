package com.jnetdata.msp.log.metadata.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.log.metadata.model.MetadataLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by TF on 2019/3/13.
 */
public interface MetadataLogMapper extends BaseMapper<MetadataLog> {

    @Select("SELECT no ,`name` FROM `dict`\n" +
            "where type ='field_type' ")
    List<Map>selectType();
    @Select("SELECT no ,`name` FROM `dict`\n" +
            "where type ='field_type' ")
    List<Map>selectDbtype();
    @Select("select name\n" +
            "from metadata_group\n" +
            "where id = #{group}")
    String selectGroup(Long group);
}
