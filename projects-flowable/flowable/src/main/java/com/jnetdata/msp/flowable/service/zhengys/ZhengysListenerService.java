package com.jnetdata.msp.flowable.service.zhengys;

import com.jnetdata.msp.flowable.constants.FlowCons;
import com.jnetdata.msp.flowable.enums.Logic;
import com.jnetdata.msp.flowable.mapper.FlowMetadataMapper;
import com.jnetdata.msp.flowable.model.FlowMetadata;
import com.jnetdata.msp.flowable.util.FlowUtil;
import com.jnetdata.msp.member.group.mapper.GroupMapper;
import com.jnetdata.msp.member.group.mapper.GrpUserMapper;
import com.jnetdata.msp.member.group.model.Groupinfo;
import com.jnetdata.msp.member.group.model.GrpUser;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.idm.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 政研社项目监听器相关服务
 */
@Slf4j
@Service(value = "zhengysListenerService")
public class ZhengysListenerService {

    @Resource
    private FlowMetadataMapper flowMetadataMapper;
    @Resource
    private GrpUserMapper grpUserMapper;
    @Resource
    private GroupMapper groupMapper;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private IdentityService identityService;

    /**
     * 节点开始时更新状态
     * @param procInstId 流程实例id
     * @param status 状态
     */
    public void updateWhenStartNode(String procInstId, String status){
        try {
            log.info("节点开始时更新数据，流程实例id:{}, 状态:{}", procInstId, status);
            FlowMetadata metadata = flowMetadataMapper.selectById(procInstId);
            if(ObjectUtils.isEmpty(metadata)){ return; }
            Map map = FlowUtil.initMap(metadata.getMetadataTable(), metadata.getMetadataId());
            map.put("auditStatus", status);
            map.put("procInstId", procInstId);
            flowMetadataMapper.updateOnly(map);
        }catch (Exception e){
            log.error("节点开始时更新数据异常，流程实例id:{}, 状态:{}", procInstId, status);
        }
    }

    /**
     * 处理本部门的节点
     * @param procInstId  流程实例id
     * @param status 状态
     * @param groupId 用户组id
     */
    public void selfDeptHandle(String procInstId, String status, String groupId){
        this.setTaskHandlerSelf(procInstId, groupId);
        this.updateWhenStartNode(procInstId, status);
    }

    /**
     * 处理分管副主任节点
     * @param procInstId  流程实例id
     * @param status 状态
     * @param groupId 用户组id
     */
    public void deputyHeaderHandle(String procInstId, String status, String groupId){
        this.setTaskHandlerDeputy(procInstId, groupId);
        this.updateWhenStartNode(procInstId, status);
    }

    /**
     * 获取任务处理人并保存(本部门的节点)
     */
    private void setTaskHandlerSelf(String procInstId, String groupId){
        try {
            //获取发起人所在组织结构的所有人员id
            HistoricProcessInstance procInst = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult();
            List<GrpUser> grpUserList = grpUserMapper.selectList(new PropertyWrapper<>(GrpUser.class).eq("userId", procInst.getStartUserId()).getWrapper());
            List<GrpUser> grpUserList2 = grpUserMapper.selectList(new PropertyWrapper<>(GrpUser.class).eq("groupId", grpUserList.get(0).getGroupId()).getWrapper());
            List<String> grpUserIds = grpUserList2.stream().map(m -> m.getUserId().toString()).collect(Collectors.toList());

            //获取节点配置角色的所有人员
            List<User> userList = identityService.createNativeUserQuery().sql(FlowCons.SQL_GET_USERS).parameter("groupId", groupId).listPage(0, 100);

            //获取任务处理人
            String taskHandler = null;
            for(User user: userList){
                if(grpUserIds.contains(user.getId())){
                    taskHandler = user.getId();
                    break;
                }
            }

            //保存
            this.saveTaskTandler(taskHandler, procInstId);
        }catch (Exception e){
            log.error("获取任务处理人并保存(本部门的节点)异常：{}", e.getMessage());
        }
    }

    /**
     * 获取任务处理人并保存(分管副主任节点)
     */
    private void setTaskHandlerDeputy(String procInstId, String groupId){
        try {
            //通过组织结构的上下级关系，获取分管副主任
            HistoricProcessInstance procInst = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult();
            List<GrpUser> grpUserList = grpUserMapper.selectList(new PropertyWrapper<>(GrpUser.class).eq("userId", procInst.getStartUserId()).getWrapper());
            Groupinfo groupinfo = groupMapper.selectById(grpUserList.get(0).getGroupId());
            List<GrpUser> grpUserList2 = grpUserMapper.selectList(new PropertyWrapper<>(GrpUser.class).eq("groupId", groupinfo.getParentId()).getWrapper());
            String taskHandler = grpUserList2.get(0).getUserId().toString();

            //保存
            this.saveTaskTandler(taskHandler, procInstId);
        }catch (Exception e){
            log.error("获取任务处理人并保存(分管副主任节点)异常：{}", e.getMessage());
        }
    }

    /**
     * 保存任务处理人
     */
    private void saveTaskTandler(String taskHandler, String procInstId){
        if(!StringUtils.isEmpty(taskHandler)){
            FlowMetadata po = new FlowMetadata();
            po.setId(procInstId);
            po.setTaskTandler(taskHandler);
            flowMetadataMapper.updateById(po);
        }
    }
}
