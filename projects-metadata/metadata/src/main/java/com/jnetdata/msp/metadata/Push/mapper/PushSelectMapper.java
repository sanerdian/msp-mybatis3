package com.jnetdata.msp.metadata.Push.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface PushSelectMapper extends BaseMapper {
    @Select("select *\n" +
            "from ${s}\n" +
            "where columnid in\n" +
            "(select pushid\n" +
            "from t_push \n" +
            "where classid in(\n" +
            "select classid\n" +
            "from subscribeclass where userid=#{set2} \n" +
            ")) and DOCSTATUS =0\n" +
            "UNION\n" +
            "select *\n" +
            "from ${s}\n" +
            "WHERE id in (\n" +
            "select xwid\n" +
            "from t_pushtocategory\n" +
            "where classid IN(\n" +
            "select classid\n" +
            "from subscribeclass where userid=#{set2} \n" +
            ")\n" +
            ")")
    List<Map> selectall(@Param("s") String s, @Param("set2") Long set2);

    @Select("select tablename\n" +
            "from tableinfo\n" +
            "where TABLEINFOID=\n" +
            "(\n" +
            "select tableid\n" +
            "from CHANNELINFO\n" +
            "where CHANNELID=#{set2}\n" +
            ")")
    String selectonetable(@Param("set2") Long set2);


    @Select("select *\n" +
            "from ${s}\n" +
            "where (columnid in\n" +
            "(select pushid\n" +
            "from t_push \n" +
            "where (classid in(\n" +
            "select classid\n" +
            "from subscribeclass where userid=#{set2} \n" +
            ") ) and pushid= #{set1} ) or id in (\n" +
            "select xwid\n" +
            "from t_pushtocategory \n" +
            "where classid IN(\n" +
            "select classid\n" +
            "from subscribeclass where userid=#{set2} \n" +
            ")and  pushid= #{set1} \n" +
            "UNION\n" +
            "select xwid\n" +
            "from t_reference \n" +
            "where nameid in(\n" +
            "#{set1} \n" +
            ")\n" +
            ")) and DOCSTATUS =0")
    List<Map> selectalll(@Param("s") String s, @Param("set2") Long set2,@Param("set1") Long set1);
}
