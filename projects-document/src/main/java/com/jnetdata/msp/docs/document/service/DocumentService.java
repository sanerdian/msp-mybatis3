package com.jnetdata.msp.docs.document.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.docs.document.model.Document;
import com.jnetdata.msp.core.model.PublishObj;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public interface DocumentService extends BaseService<Document> {

    /**
     * 根据ids查询文件的属性
     *
     * @param ids 以逗号分隔的id
     * @return
     */
    List<Map<String, String>> getFileAttributes(String ids);


    /**
     * TODO xuning(暂时不实现用元数据的方式)
     * 发布相关:   获取可发布文档列表
     *
     * @param chnlDocIds 以,分割的chnlDocId
     * @param tableId    此处tableId为0,暂不使用
     * @return
     */
    default List<Map<String, Object>> getListPublish(String chnlDocIds, long tableId, PublishObj publishObj) {
        return null;
    }


    /**
     * TODO xuning(暂时不实现用元数据的方式)
     * 发布相关:   获取已发布文档列表
     * 要根据文档状态列表            publishObj.getStatusList()
     * 要获取文档数量,为空不限制      publishObj.getNum()
     *
     * @param channelId  栏目id
     * @param tableId  此处tableId为0,暂不使用
     * @param publishObj 发布对象
     * @return
     */
    default List<Map<String, Object>> getListPublish(long channelId, long tableId, PublishObj publishObj) {
        return null;
    }

}
