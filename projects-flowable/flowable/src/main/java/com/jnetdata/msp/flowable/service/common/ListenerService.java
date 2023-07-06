package com.jnetdata.msp.flowable.service.common;

import com.jnetdata.msp.flowable.enums.AuditStatus;
import com.jnetdata.msp.flowable.enums.Logic;
import com.jnetdata.msp.flowable.mapper.FlowMetadataMapper;
import com.jnetdata.msp.flowable.model.FlowMetadata;
import com.jnetdata.msp.flowable.util.FlowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 监听器相关服务
 */
@Slf4j
@Service(value = "listenerService")
public class ListenerService {

    @Resource
    private FlowMetadataMapper flowMetadataMapper;

    /**
     * 节点开始时更新数据
     * @param procInstId 流程实例id
     * @param auditStatus 状态
     */
    public void updateWhenStartNode(String procInstId, String auditStatus){
        try {
            log.info("节点开始时更新数据，流程实例id:{}, 状态:{}", procInstId, auditStatus);
            FlowMetadata metadata = flowMetadataMapper.selectById(procInstId);
            if(ObjectUtils.isEmpty(metadata)){ return; }
            Map map = FlowUtil.initMap(metadata.getMetadataTable(), metadata.getMetadataId());
            map.put("auditStatus", auditStatus);
            map.put("procInstId", procInstId);
            flowMetadataMapper.updateOnly(map);

            //更新关联信息中的状态
            this.updateStatus(procInstId, auditStatus);
        }catch (Exception e){
            log.error("节点开始时更新数据异常，流程实例id:{}, 状态:{}", procInstId, auditStatus);
        }
    }

    /**
     * 流程结束时更新数据
     * @param procInstId 流程实例id
     * @param auditStatus 审核状态
     */
    public void updateForEnd(String procInstId, String auditStatus){
        try {
            log.info("流程结束时更新数据，流程实例id:{}, 审核状态:{}", procInstId, auditStatus);
            FlowMetadata metadata = flowMetadataMapper.selectById(procInstId);
            Map map = FlowUtil.initMap(metadata.getMetadataTable(), metadata.getMetadataId());
            map.put("auditStatus", auditStatus);
            if(AuditStatus.FINISHED.getCode().equals(auditStatus)){
                map.put("finalResult", Logic.YES.getCode());
                map.put("procInstId", procInstId);
            }else if(AuditStatus.TERMINATED.getCode().equals(auditStatus)){
                map.put("finalResult", Logic.NO.getCode());
            }
            flowMetadataMapper.updateMetadata(map);

            //更新关联信息中的状态
            this.updateStatus(procInstId, auditStatus);
        }catch (Exception e){
            log.error("流程结束时更新数据异常，流程实例id:{}, 审核状态:{}", procInstId, auditStatus);
        }
    }

    /**
     * 更新关联信息中的状态
     */
    private void updateStatus(String procInstId, String auditStatus){
        try {
            log.info("更新关联信息中的状态，流程实例id:{}, 审核状态:{}", procInstId, auditStatus);
            FlowMetadata po = new FlowMetadata();
            po.setId(procInstId);
            po.setAuditStatus(Integer.parseInt(auditStatus));
            flowMetadataMapper.updateById(po);
        }catch (Exception e){
            log.error("更新关联信息中的状态异常：{}", e.getMessage());
        }
    }
}
