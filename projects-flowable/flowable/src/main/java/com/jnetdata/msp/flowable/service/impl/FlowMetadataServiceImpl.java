package com.jnetdata.msp.flowable.service.impl;

import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.flowable.mapper.FlowMetadataMapper;
import com.jnetdata.msp.flowable.mapper.MetadataConfigMapper;
import com.jnetdata.msp.flowable.model.FlowMetadata;
import com.jnetdata.msp.flowable.model.MetaPub;
import com.jnetdata.msp.flowable.model.MetadataConfig;
import com.jnetdata.msp.flowable.service.FlowMetadataService;
import com.jnetdata.msp.flowable.service.common.FlowCommonService;
import com.jnetdata.msp.flowable.util.FlowUtil;
import com.jnetdata.msp.flowable.vo.feiyi.HistoryVo;
import com.jnetdata.msp.flowable.vo.ProcessStartVo;
import com.jnetdata.msp.flowable.vo.feiyi.SubmitVo;
import com.jnetdata.msp.flowable.vo.feiyi.TodoVo;
import com.jnetdata.msp.member.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FlowMetadataServiceImpl extends BaseServiceImpl<FlowMetadataMapper, FlowMetadata> implements FlowMetadataService {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private FlowCommonService flowCommonService;

    @Resource
    private MetadataConfigMapper metadataConfigMapper;

    @Resource
    private FlowMetadataMapper flowMetadataMapper;

    @Override
    protected PropertyWrapper<FlowMetadata> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .eq("procInstId")
                .getWrapper();
    }

    /**
     * 获取关联的流程实例
     */
    @Override
    public JsonResult getProcInst(FlowMetadata flowMetadata) {
        try {
            if(StringUtils.isEmpty(flowMetadata.getMetadataId()) || StringUtils.isEmpty(flowMetadata.getMetadataTable())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()),"元数据id和元数据表名都不能为空");
            }

            //获取metadataId对应的流程列表
            List<FlowMetadata> metadataList = this.list(new PropertyWrapper<>(FlowMetadata.class)
                    .eq("metadataId", flowMetadata.getMetadataId())
                    .eq("metadataTable", flowMetadata.getMetadataTable()));
            Set<String> procInstIds = metadataList.stream().map(m -> m.getId()).collect(Collectors.toSet());
            List<HistoricProcessInstance> instList = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceIds(procInstIds).orderByProcessInstanceStartTime().desc().list();

            //返回正在运行的流程
            for(HistoricProcessInstance inst: instList){
                if(ObjectUtils.isEmpty(inst.getEndTime())){
                    return JsonResult.success(inst);
                }
            }

            //流程都结束了，返回最新创建的流程
            if(!CollectionUtils.isEmpty(instList) && instList.size() >= 1){
                return JsonResult.success(instList.get(0));
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        return JsonResult.fail();
    }

    /**
     * 保存对象
     */
    @Override
    public void saveEntity(String procInstId, ProcessStartVo startVo){
        try {
            FlowMetadata entity = new FlowMetadata();
            entity.setId(procInstId);
            entity.setMetadataTable(startVo.getMetadataTable().toLowerCase());
            entity.setMetadataId(startVo.getMetadataId());
            entity.setMetadataTitle(startVo.getMetadataTitle());
            entity.setMetadataUrl(startVo.getMetadataUrl());
            entity.setCreateTime(new Date());

            //获取当前用户信息
            try {
                User user = (User)SessionUser.getCurrentUser();
                entity.setCreatorId(user.getId());
                entity.setCreatorName(user.getName());
            }catch (Exception e2){
                log.error("获取当前用户信息异常：{}", e2.getMessage());
            }

            //获取流程定义信息
            HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult();
            if(!ObjectUtils.isEmpty(instance)){
                entity.setProcDefiId(instance.getProcessDefinitionId());
                entity.setProcDefiKey(instance.getProcessDefinitionKey());
            }

            //获取流水号
            Integer max = flowMetadataMapper.getMaxSerialNumber();
            entity.setSerialNumber(ObjectUtils.isEmpty(max) ? 1 : (max+1));

            //保存
            this.insert(entity);
        }catch (Exception e){
            log.error("保存对象异常：{}", e.getMessage());
        }
    }

    /**
     * 填充我的待办数据
     * @param taskList 任务列表
     */
    @Override
    public List<TodoVo> fillTodoVo(List<Task> taskList){
        List<TodoVo> list = new ArrayList<>();
        try {
            //获取元数据配置
            Map<String, MetadataConfig> map = this.getMap();

            for(Task task: taskList){
                //设置任务id和流程定义id
                TodoVo todoVo = new TodoVo();
                todoVo.setProcDefiId(task.getProcessDefinitionId());
                todoVo.setTaskId(task.getId());
                todoVo.setProcInstId(task.getProcessInstanceId());

                //获取流程对应的元数据信息
                FlowMetadata metadata = this.getById(task.getProcessInstanceId());
                if(!ObjectUtils.isEmpty(metadata)){
                    //设置流程对应的元数据信息
                    todoVo.setMetadataId(metadata.getMetadataId());
                    todoVo.setMetadataTitle(metadata.getMetadataTitle());
                    todoVo.setMetadataUrl(metadata.getMetadataUrl());
                    todoVo.setCreateTime(metadata.getCreateTime());
                    todoVo.setCreatorName(metadata.getCreatorName());
                    todoVo.setAuditStatus(metadata.getAuditStatus());

                    //设置对应的元数据配置信息
                    MetadataConfig config = map.get(metadata.getMetadataTable());
                    if(!ObjectUtils.isEmpty(config)){
                        todoVo.setMetadataNo(config.getId());
                        todoVo.setMetadataDesc(config.getMetadataDesc());
                    }
                }
                list.add(todoVo);
            }
        }catch (Exception e){
            log.error("填充我的待办数据异常：{}", e.getMessage());
        }
        return list;
    }

    /**
     * 填充我的已办数据
     * @param historyTaskList 历史任务列表
     */
    @Override
    public List<HistoryVo> fillHistoryVo(List<HistoricTaskInstance> historyTaskList){
        List<HistoryVo> list = new ArrayList<>();
        try {
            //获取元数据配置
            Map<String, MetadataConfig> map = this.getMap();

            for(HistoricTaskInstance historicTask: historyTaskList){
                //设置流程定义id
                HistoryVo historyVo = new HistoryVo();
                historyVo.setProcDefiId(historicTask.getProcessDefinitionId());
                historyVo.setProcInstId(historicTask.getProcessInstanceId());

                //获取流程对应的元数据信息
                FlowMetadata metadata = this.getById(historicTask.getProcessInstanceId());
                if(!ObjectUtils.isEmpty(metadata)){
                    //设置流程对应的元数据信息
                    historyVo.setMetadataId(metadata.getMetadataId());
                    historyVo.setMetadataTitle(metadata.getMetadataTitle());
                    historyVo.setMetadataUrl(metadata.getMetadataUrl());
                    historyVo.setCreateTime(metadata.getCreateTime());
                    historyVo.setCreatorName(metadata.getCreatorName());
                    historyVo.setAuditStatus(metadata.getAuditStatus());

                    //设置对应的元数据配置信息
                    MetadataConfig config = map.get(metadata.getMetadataTable());
                    if(!ObjectUtils.isEmpty(config)){
                        historyVo.setMetadataNo(config.getId());
                        historyVo.setMetadataDesc(config.getMetadataDesc());
                    }
                }
                list.add(historyVo);
            }
        }catch (Exception e){
            log.error("填充我的已办数据异常：{}", e.getMessage());
        }
        return list;
    }

    /**
     * 填充我的提交数据
     * @param processList 流程列表
     */
    @Override
    public List<SubmitVo> fillSubmitVo(List<HistoricProcessInstance> processList){
        List<SubmitVo> list = new ArrayList<>();
        try {
            //获取元数据配置
            Map<String, MetadataConfig> map = this.getMap();

            for(HistoricProcessInstance process: processList){
                //设置流程定义id
                SubmitVo submitVo = new SubmitVo();
                submitVo.setProcDefiId(process.getProcessDefinitionId());
                submitVo.setProcInstId(process.getId());

                //获取流程对应的元数据信息
                FlowMetadata metadata = this.getById(process.getId());
                if(!ObjectUtils.isEmpty(metadata)){
                    //设置流程对应的元数据信息
                    submitVo.setMetadataId(metadata.getMetadataId());
                    submitVo.setMetadataTitle(metadata.getMetadataTitle());
                    submitVo.setMetadataUrl(metadata.getMetadataUrl());
                    submitVo.setCreateTime(metadata.getCreateTime());
                    submitVo.setAuditStatus(metadata.getAuditStatus());

                    //设置对应的元数据配置信息
                    MetadataConfig config = map.get(metadata.getMetadataTable());
                    if(!ObjectUtils.isEmpty(config)){
                        submitVo.setMetadataNo(config.getId());
                        submitVo.setMetadataDesc(config.getMetadataDesc());
                    }
                }
                list.add(submitVo);
            }
        }catch (Exception e){
            log.error("填充我的提交数据异常：{}", e.getMessage());
        }
        return list;
    }

    /**
     * 获取元数据配置
     */
    private Map<String, MetadataConfig> getMap(){
        Map<String, MetadataConfig> map = new HashMap<>();
        try {
            List<MetadataConfig> list = metadataConfigMapper.selectList(null);
            for(MetadataConfig config: list){
                map.put(config.getMetadataTable(), config);
            }
        }catch (Exception e){
            log.error("获取元数据配置异常：{}", e.getMessage());
        }
        return map;
    }

    /**
     * 删除草稿箱数据
     */
    @Override
    public void deleteDraftbox(String procInstId){
        try {
            FlowMetadata flowMetadata = this.getById(procInstId);
            if(!StringUtils.isEmpty(flowMetadata.getMetadataTable()) && !StringUtils.isEmpty(flowMetadata.getMetadataId())){
                Map<String, String> map = new HashMap<>();
                map.put("metadataTable", flowMetadata.getMetadataTable());
                map.put("metadataId", flowMetadata.getMetadataId());
                flowMetadataMapper.deleteDraftbox(map);
            }
        }catch (Exception e){
            log.error("删除草稿箱数据异常：{}", e.getMessage());
        }
    }

    /**
     * 获取元数据信息
     * @param procInstId 流程实例id
     */
    @Override
    public MetaPub getMetadata(String procInstId){
        MetaPub metaPub = new MetaPub();
        try{
            //根据流程id获取获取元数据公共字段信息
            FlowMetadata metadata = flowMetadataMapper.selectById(procInstId);
            Map map = FlowUtil.initMap(metadata.getMetadataTable(), metadata.getMetadataId());
            metaPub = flowMetadataMapper.singleMetaPub(map);
            metaPub.setTableName(metadata.getMetadataTable());

            //获取流程发起人、流程发起时间
            HistoricProcessInstance procInst = historyService.createHistoricProcessInstanceQuery().processInstanceId(metadata.getId()).singleResult();
            metaPub.setStartTime(procInst.getStartTime());
            String startUserId = procInst.getStartUserId();
            if(StringUtils.isEmpty(procInst.getStartUserId())){
                //流程信息中流程发起人为空时，获取元数据和流程关联信息的创建人
                startUserId = metadata.getCreatorId().toString();
            }
            if(!StringUtils.isEmpty(startUserId)){
                metaPub.setStartUserId(startUserId);
                org.flowable.idm.api.User user = identityService.createUserQuery().userId(startUserId).singleResult();
                metaPub.setStartUserName(ObjectUtils.isEmpty(user) ? null : user.getDisplayName());
            }
        }catch (Exception e){
            log.error("获取元数据公共信息异常：{}", e.getMessage());
        }
        return metaPub;
    }

}
