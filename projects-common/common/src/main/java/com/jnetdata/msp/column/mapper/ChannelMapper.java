package com.jnetdata.msp.column.mapper;

import com.jnetdata.msp.column.model.Channel;
import com.jnetdata.msp.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.thenicesys.mybatis.dynsql.DynamicSqlMapper;

import java.util.List;

/**
 * Created by WFJ on 2019/4/26.
 */
public interface ChannelMapper extends BaseMapper<Channel>, DynamicSqlMapper {

    @Select("<script>" +
            "SELECT T1.CHANNELID as id,T1.CHNLNAME as name FROM (SELECT C.* FROM CHANNELINFO C WHERE C.STATUS = 0 " +
            "AND C.PARENTID =#{id} )T1 LEFT JOIN " +
            "(SELECT A.CHANNELID,B.TABLENAME FROM CHANNELINFO A JOIN TABLEINFO B ON A.TABLEID=B.TABLEINFOID WHERE A.STATUS = 0 AND " +
            "A.PARENTID =#{chnlIds} )T2 ON T1.CHANNELID=T2.CHANNELID ORDER BY T1.CHNLORDER DESC"
            + "</script>")
    List<Channel> selectChildChannel(@Param("id")long chnlId, @Param("chnlIds")long chnlIds);
}
