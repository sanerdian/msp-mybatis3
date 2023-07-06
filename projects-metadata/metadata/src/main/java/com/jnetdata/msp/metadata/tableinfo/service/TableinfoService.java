package com.jnetdata.msp.metadata.tableinfo.service;

import com.jnetdata.msp.core.model.PublishObj;
import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.metadata.es.controller.EsTableinfo;
import com.jnetdata.msp.metadata.fieldinfo.model.Fieldinfo;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.vo.BaseVo;
import com.jnetdata.msp.metadata.vo.TableField;
import org.thenicesys.data.api.Pager;

import java.util.List;
import java.util.Map;

public interface TableinfoService extends BaseService<Tableinfo> {

    void insertInfo(String tablename, String fieldnames, List<Object> list);

    void createTable(Tableinfo entity);

    List<Tableinfo> findByTableName(String tablename);

    List<String> existsTable(String tablename);

    void dropTable(String tablename);

    void updateTablename(Tableinfo entity);

    /*
    * 元数据变化
    * */
    List<Map<String, Object>> countNews();

    /*
    * 本周元数据新增数
    * return
    * */
    Map<String, Object> addThisWeek();

    /*
    * 本月元数据新增数
    * */
    Map<String, Object> addThisMonth();

    /*
    * 本年元数据新增数
    * return
    * */
    Map<String, Object> addThisYear();

    void toTop(String tablename, Long dataId);

    void reSetTop(String tablename, Long dataId);

    void clearRecycle(String tablename);

    List<Map<String, Object>> selectMess(String fieldnames, String tablename, Long columnId);

    List<Map<String, Object>> selectMess2(String fieldnames, String id);

    void insertTable(String fieldnames, List<Object> list);

    int insertInfoBatch(List<Map<String, String>> params);

    void statusPass(int status, String[] ids, String tableName, String reason);

    void createView(Tableinfo tableinfo, List<Fieldinfo> fieldInfos);

    List<TableField> getViewFields(String viewname);

    List<Tableinfo> getDbTables(String name);

    void addDbTableFields(Tableinfo tableinfo, Long groupId);

    /**
     * TODO xuning
     * 根据 metaDataId 和 tableId 获取文档
     * publishObj,暂不使用:要根据文档状态列表            publishObj.getStatusList()
     * publishObj,暂不使用:要获取文档数量,为空不限制      publishObj.getNum()
     *
     * @param metaDataId 元数据表数据id
     * @param tableId    元数据表id
     * @return
     */
    /*default Map<String, Object> getOnePublish(long metaDataId, long tableId, PublishObj publishObj) {
        //publishObj.getStatusList()
        //publishObj.getNum()
        return null;
    }*/
    Map<String, Object> getOnePublish(long metaDataId, long tableId, PublishObj publishObj);

    /**
     * TODO xuning
     * 发布相关:   获取可发布文档列表
     *
     * @param metadataIds 以,分割的 metadataId
     * @param tableId 元数据表id
     * @return
     */
    List<Map<String, Object>> getListPublish(String metadataIds, long tableId, PublishObj publishObj);

    /**
     * TODO xuning
     * 根据 tableId 获取文档列表
     * 要根据文档状态列表            publishObj.getStatusList()
     * 要获取文档数量,为空不限制      publishObj.getNum()
     * @param channelId 栏目id
     * @param tableId 元数据表id
     * @return
     */
     List<Map<String, Object>> getListPublish(long channelId, long tableId, PublishObj publishObj);

    List<Map<String, Object>> getPubList(long channelId, long tableId, PublishObj publishObj);

    List<Map<String, Object>> getPubList(String chnlIds, long tableId, PublishObj publishObj);

    Tableinfo getTableinfo(Long dbSourceId,String name);

    void saveEsToDb(List<EsTableinfo> esTableinfos);

    List<Map<String, Object>> getAllFromTable(String tablename);

    void insertByListMap(List<Map<String, Object>> datas,String tableName,List<String> titles);

    Pager<Tableinfo> pageByPermission(BaseVo<Tableinfo> vo);
}