package com.jnetdata.msp.flowable.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.flowable.model.FlowMetadata;
import com.jnetdata.msp.flowable.model.MetaPub;
import com.jnetdata.msp.flowable.vo.feiyi.HistoryVo;
import com.jnetdata.msp.flowable.vo.ProcessStartVo;
import com.jnetdata.msp.flowable.vo.feiyi.SubmitVo;
import com.jnetdata.msp.flowable.vo.feiyi.TodoVo;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.thenicesys.web.JsonResult;

import java.util.List;

public interface FlowMetadataService extends BaseService<FlowMetadata> {

    /**
     * 获取关联的流程实例
     */
    JsonResult getProcInst(FlowMetadata flowMetadata);

    /**
     * 保存对象
     */
    void saveEntity(String procInstId, ProcessStartVo startVo);

    /**
     * 填充我的待办数据
     * @param taskList 任务列表
     */
    List<TodoVo> fillTodoVo(List<Task> taskList);

    /**
     * 填充我的已办数据
     * @param historyTaskList 历史任务列表
     */
    List<HistoryVo> fillHistoryVo(List<HistoricTaskInstance> historyTaskList);

    /**
     * 填充我的提交数据
     * @param processList 流程列表
     */
    List<SubmitVo> fillSubmitVo(List<HistoricProcessInstance> processList);

    /**
     * 删除草稿箱数据
     */
    void deleteDraftbox(String procInstId);

    /**
     * 获取元数据信息
     */
    MetaPub getMetadata(String procInstId);
}
