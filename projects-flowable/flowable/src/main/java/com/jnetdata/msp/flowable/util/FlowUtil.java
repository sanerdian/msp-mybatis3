package com.jnetdata.msp.flowable.util;

import com.jnetdata.msp.flowable.enums.MetadataColumn;

import java.util.HashMap;
import java.util.Map;

public class FlowUtil {

    /**
     * 更新元数据初始化map
     * @param tableName 元数据表名
     * @param id 元数据id
     * @return
     */
    public static Map initMap(String tableName, String id){
        Map map = new HashMap();
        map.put("tableName", tableName);
        map.put("id", id);
        map.put("columnAuditStatus", MetadataColumn.AUDIT_STATUS.getName());
        map.put("columnAuditTime", MetadataColumn.AUDIT_TIME.getName());
        map.put("columnAuditorId", MetadataColumn.AUDITOR_ID.getName());
        map.put("columnAuditorName", MetadataColumn.AUDITOR_NAME.getName());
        map.put("columnProcInstId", MetadataColumn.PROC_INST_ID.getName());
        map.put("columnFinalResult", MetadataColumn.FINAL_RESULT.getName());
        map.put("columnTaskId", MetadataColumn.TASK_ID.getName());
        return map;
    }
}
