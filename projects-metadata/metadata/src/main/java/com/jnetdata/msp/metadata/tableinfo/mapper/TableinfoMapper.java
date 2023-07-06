package com.jnetdata.msp.metadata.tableinfo.mapper;

import com.jnetdata.msp.column.model.Channel;
import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.metadata.fieldinfo.model.Fieldinfo;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.vo.TableField;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface TableinfoMapper extends BaseMapper<Tableinfo> {

    void createTable(Tableinfo entity);

    List<String> existsTable(@Param("tablename") String tablename);

    void dropTable(@Param("tablename") String tablename);

    void updateTablename(Tableinfo entity);

    void insertInfo(@Param("tablename") String tablename, @Param("fieldnames") String fieldnames, @Param("list") List<Object> list);

    void toTop(@Param("tablename") String tablename, @Param("dataId") Long dataId);

    @Update("UPDATE ${tablename} set SEQENCING = null where id = #{dataId}")
    void reSetTop(@Param("tablename") String tablename, @Param("dataId") Long dataId);

    @Delete("DELETE FROM ${tablename} WHERE docstatus = 1")
    void clearRecycle(@Param("tablename") String tablename);

    @Select("select ${fieldnames} from ${tablename} where columnid = #{columnId}")
    List<Map<String,Object>> selectMess(@Param("fieldnames") String fieldnames, @Param("tablename") String tablename, @Param("columnId") Long columnId);

    @Select("select ${fieldnames} from dbfieldinfo where TABLEID=#{id}")
    List<Map<String,Object>> selectMess2(@Param("fieldnames") String fieldnames, @Param("id") String id);

    void insertTable(@Param("fieldnames") String fieldnames, @Param("list") List<Object> list);

    void createSeq(Tableinfo entity);

    void dropSeq(Tableinfo entity);

    void createTri(Tableinfo entity);

    @Update("UPDATE ${tableName} SET STATUS = #{status},DOCTITLE = #{reason} where id in (${ids})")
    void statusPass(@Param("status") int status, @Param("ids") String ids, @Param("tableName") String tableName,@Param("reason") String reason);

    void createView(@Param("ViewTable") Tableinfo viewTable, @Param("list") List<String> fieldNames);

    List<TableField> getViewFields(@Param("viewname") String viewname);

    @Insert("${sql}")
    void insertInfoBatch(Map<String,String> param);

    boolean createComment(Tableinfo entity);

    List<Tableinfo> getDbTables(@Param("name") String name);

    List<Fieldinfo> getDbTableFields(String tablename);

    @Select("select TABLENAME,PK from tableinfo where tableinfoid=#{tableId}")
    Map<String, Object> getTabName(@Param("tableId")long tableId);

    @Select("select * from #{tablename} where #{pk} = #{metaDataId} and DOCSTATUS=0")
    Map<String, Object> getOnePublish(@Param("metaDataId")long metaDataId,@Param("tablename")String tablename,@Param("pk")long pk);

    @Select("select * from #{tablename} where #{pk} in (#{metadataIds}) and DOCSTATUS=0")
    List<Map<String, Object>> getListPublish(@Param("metadataIds")String metadataIds,@Param("tablename")String tablename,@Param("pk")long pk);

    @Select("select * from ${tablename} where COLUMNID = #{channelId} and docstatus = 0")
    List<Map<String, Object>> getListPublish_channelId(@Param("channelId")long channelId,@Param("tablename")String tablename);

    List<Map<String, Object>> getPubList(@Param("channelId")Long channelId,@Param("tablename")String tablename, @Param("chnlIds")String chnlIds,@Param("from")Integer from,@Param("size")Integer size,@Param("order")String order);

    Integer getPubCount(@Param("channelId")Long channelId, @Param("tablename")String tablename, @Param("chnlIds")String chnlIds);

    List<Map<String, Object>> getAllFromTable(String tablename);

    void insertByListMap(List<Map<String, Object>> datas,String tableName,List<String> titles);
}
