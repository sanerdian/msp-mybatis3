package com.jnetdata.msp.flowable.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.model.util.NoCurrentUserException;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.flowable.mapper.FlowSuspendMapper;
import com.jnetdata.msp.flowable.model.FlowSuspend;
import com.jnetdata.msp.flowable.service.FlowSuspendService;
import com.jnetdata.msp.flowable.service.common.FlowCommonService;
import com.jnetdata.msp.flowable.vo.WorkVo;
import com.jnetdata.msp.vo.BaseVo;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class FlowSuspendServiceImpl extends BaseServiceImpl<FlowSuspendMapper, FlowSuspend> implements FlowSuspendService {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private RuntimeService runtimeService;
    @Resource
    private FlowSuspendMapper flowSuspendMapper;
    @Autowired
    private FlowCommonService flowCommonService;

    @Override
    protected PropertyWrapper<FlowSuspend> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .eq("userId")
                .getWrapper();
    }

    /**
     * 挂起流程实例
     */
    @Override
    public JsonResult<String> suspend(FlowSuspend entity){
        try {
            if(StringUtils.isEmpty(entity.getProcInstId())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "流程实例id(procInstId)不能为空！");
            }
            //用户id
            String userId = SessionUser.getCurrentUser().getId().toString();

            //获取对应的流程实例信息
            HistoricProcessInstance procInst = historyService.createHistoricProcessInstanceQuery().processInstanceId(entity.getProcInstId()).singleResult();
            if(ObjectUtils.isEmpty(procInst)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "没有对应的流程！");
            }else if(!ObjectUtils.isEmpty(procInst.getEndTime())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "流程已结束，无法挂起！");
            }

            //校验流程是否已挂起
            Execution execution = runtimeService.createExecutionQuery().executionId(entity.getProcInstId()).singleResult();
            if(!ObjectUtils.isEmpty(execution) && execution.isSuspended()){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "流程已挂起，不要重复操作！");
            }

            //挂起
            runtimeService.suspendProcessInstanceById(entity.getProcInstId());

            //保存流程挂起信息
            FlowSuspend po = new FlowSuspend();
            po.setProcInstId(entity.getProcInstId());
            po.setUserId(userId);
            po.setProcDefiId(procInst.getProcessDefinitionId());
            po.setProcDefiKey(procInst.getProcessDefinitionKey());
            po.setSuspendTime(new Date());
            this.insert(po);

            return JsonResult.success();
        }catch (NoCurrentUserException e){
            return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "用户未登录");
        }catch (Exception e){
            log.error("挂起流程实例异常：{}", e.getMessage());
            return JsonResult.fail();
        }
    }

    /**
     * 激活流程实例
     */
    @Override
    public JsonResult<String> activate(FlowSuspend entity){
        try {
            if(StringUtils.isEmpty(entity.getProcInstId())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "流程实例id(procInstId)不能为空！");
            }
            //用户id
            String userId = SessionUser.getCurrentUser().getId().toString();

            //获取对应的流程实例信息
            HistoricProcessInstance procInst = historyService.createHistoricProcessInstanceQuery().processInstanceId(entity.getProcInstId()).singleResult();
            if(ObjectUtils.isEmpty(procInst)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "没有对应的流程！");
            }else if(!ObjectUtils.isEmpty(procInst.getEndTime())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "流程已结束，无法激活！");
            }

            //校验流程是否已挂起
            Execution execution = runtimeService.createExecutionQuery().executionId(entity.getProcInstId()).singleResult();
            if(!ObjectUtils.isEmpty(execution) && !execution.isSuspended()){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "流程没有被挂起，无需激活！");
            }

            //激活
            runtimeService.activateProcessInstanceById(entity.getProcInstId());

            //删除流程挂起信息
            List<FlowSuspend> list = this.list(new PropertyWrapper<>(FlowSuspend.class)
                    .eq("procInstId", entity.getProcInstId())
                    .eq("userId", userId));
            for(FlowSuspend po: list){
                this.deleteById(po.getId());
            }

            return JsonResult.success();
        }catch (Exception e){
            log.error("挂起流程实例异常：{}", e.getMessage());
            return JsonResult.fail();
        }
    }

    /**
     * 我挂起的
     */
    @Override
    public JsonResult<Pager<WorkVo>> mySuspend(BaseVo<WorkVo> vo){
        try {
            //获取登录用户id
            String userId = SessionUser.getCurrentUser().getId().toString();
            vo.getEntity().setLoginUserId(userId);

            //分页查询
            Page<WorkVo> page = new Page((long)vo.getPager().getCurrent(), (long)vo.getPager().getSize());
            flowSuspendMapper.listPage(page, vo.getEntity());

            //设置对应流程定义名称和流程发起人名称
            List<WorkVo> voList = page.getRecords();
            flowCommonService.setProcDefiName(voList);
            flowCommonService.setStartUserName(voList);

            //返回结果
            Pager<WorkVo> resultPager = flowCommonService.getPager(page.getRecords(), (new Long(page.getTotal())).intValue(), vo.getPager().getCurrent(), vo.getPager().getSize());
            return JsonResult.success(resultPager);
        }catch (NoCurrentUserException e){
            return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "用户未登录");
        }catch (Exception e){
            log.error("我的关注异常：{}", e.getMessage());
        }
        return JsonResult.fail();
    }
}
