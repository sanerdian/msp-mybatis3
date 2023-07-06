package com.jnetdata.msp.flowable.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.model.util.NoCurrentUserException;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.flowable.enums.AuditStatus;
import com.jnetdata.msp.flowable.mapper.FlowFocusMapper;
import com.jnetdata.msp.flowable.model.FlowFocus;
import com.jnetdata.msp.flowable.service.FlowFocusService;
import com.jnetdata.msp.flowable.service.common.FlowCommonService;
import com.jnetdata.msp.flowable.vo.WorkVo;
import com.jnetdata.msp.vo.BaseVo;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.idm.api.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class FlowFocusServiceImpl extends BaseServiceImpl<FlowFocusMapper, FlowFocus> implements FlowFocusService {

    @Resource
    private FlowFocusMapper flowFocusMapper;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private FlowCommonService flowCommonService;

    @Override
    protected PropertyWrapper<FlowFocus> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .eq("userId")
                .getWrapper();
    }

    /**
     * 我关注的
     */
    @Override
    public JsonResult<Pager<WorkVo>> myFocus(BaseVo<WorkVo> vo){
        try {
            //获取登录用户id
            String userId = SessionUser.getCurrentUser().getId().toString();
            vo.getEntity().setLoginUserId(userId);

            //分页查询
            Page<WorkVo> page = new Page((long)vo.getPager().getCurrent(), (long)vo.getPager().getSize());
            flowFocusMapper.listPage(page, vo.getEntity());

            //设置对应流程定义名称和流程发起人名称
            List<WorkVo> voList = page.getRecords();
            flowCommonService.setProcDefiName(voList);
            flowCommonService.setStartUserName(voList);

            //设置审核状态名称
            for(WorkVo workVo: voList){
                if(!ObjectUtils.isEmpty(workVo.getAuditStatus())){
                    workVo.setAuditStatusStr(AuditStatus.getName(workVo.getAuditStatus().toString()));
                }
            }

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

    /**
     * 关注流程
     */
    @Override
    public JsonResult<String> addFocus(FlowFocus entity){
        try {
            //非空校验
            if(StringUtils.isEmpty(entity.getProcInstId())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "流程实例id（procInstId）不能为空");
            }
            HistoricProcessInstance inst = historyService.createHistoricProcessInstanceQuery().processInstanceId(entity.getProcInstId()).singleResult();
            if(ObjectUtils.isEmpty(inst)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "流程实例id（procInstId）不正确");
            }

            //重复关注校验
            String userId = SessionUser.getCurrentUser().getId().toString();
            List<FlowFocus> list = this.list(new PropertyWrapper<>(FlowFocus.class)
                    .eq("procInstId", entity.getProcInstId())
                    .eq("userId", userId));
            if(!CollectionUtils.isEmpty(list)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "重复关注");
            }

            //保存信息
            FlowFocus po = new FlowFocus();
            po.setProcInstId(entity.getProcInstId());
            po.setUserId(userId);
            po.setProcDefiKey(inst.getProcessDefinitionKey());
            po.setFocusTime(new Date());
            this.insert(po);

            return JsonResult.success();
        }catch (NoCurrentUserException e){
            return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "用户未登录");
        }catch (Exception e){
            log.error("关注流程异常：{}", e.getMessage());
        }
        return JsonResult.fail();
    }

    /**
     * 取消关注
     */
    @Override
    public JsonResult<String> removeFocus(FlowFocus entity){
        try {
            //非空校验
            if(StringUtils.isEmpty(entity.getProcInstId())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "流程实例id（procInstId）不能为空");
            }
            HistoricProcessInstance inst = historyService.createHistoricProcessInstanceQuery().processInstanceId(entity.getProcInstId()).singleResult();
            if(ObjectUtils.isEmpty(inst)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "流程实例id（procInstId）不正确");
            }

            //获取关注的流程
            String userId = SessionUser.getCurrentUser().getId().toString();
            List<FlowFocus> list = this.list(new PropertyWrapper<>(FlowFocus.class)
                    .eq("procInstId", entity.getProcInstId())
                    .eq("userId", userId));

            //删除
            for(FlowFocus focus: list){
                this.deleteById(focus.getId());
            }
            return JsonResult.success();
        }catch (NoCurrentUserException e){
            return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "用户未登录");
        }catch (Exception e){
            log.error("取消关注异常：{}", e.getMessage());
        }
        return JsonResult.fail();
    }
}
