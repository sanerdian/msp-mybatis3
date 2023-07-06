package com.jnetdata.msp.flowable.service.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.model.util.NoCurrentUserException;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.flowable.mapper.MyWorkMapper;
import com.jnetdata.msp.flowable.vo.WorkVo;
import com.jnetdata.msp.flowable.vo.feiyi.TodoVo;
import com.jnetdata.msp.vo.BaseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的工作
 */
@Slf4j
@Service
public class MyWorkService {

    @Resource
    private MyWorkMapper myWorkMapper;
    @Autowired
    private FlowCommonService flowCommonService;

    /**
     * 我的待办
     */
    public JsonResult<Pager<WorkVo>> myTodo(BaseVo<WorkVo> vo){
        try {
            //获取登录用户id
            String userId = SessionUser.getCurrentUser().getId().toString();
            vo.getEntity().setLoginUserId(userId);

            //分页查询
            Page<WorkVo> page = new Page((long)vo.getPager().getCurrent(), (long)vo.getPager().getSize());
            myWorkMapper.myTodo(page, vo.getEntity());

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

    /**
     * 我的已办
     */
    public JsonResult<Pager<WorkVo>> myDone(BaseVo<WorkVo> vo){
        try {
            //获取登录用户id
            String userId = SessionUser.getCurrentUser().getId().toString();
            vo.getEntity().setLoginUserId(userId);

            //分页查询
            Page<WorkVo> page = new Page((long)vo.getPager().getCurrent(), (long)vo.getPager().getSize());
            myWorkMapper.myDone(page, vo.getEntity());

            //设置对应流程定义名称和流程发起人名称
            List<WorkVo> voList = page.getRecords();
            flowCommonService.setProcDefiName(voList);
            flowCommonService.setStartUserName(voList);
            this.setCurrentStepName(voList);

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
     * 设置流程的当前步骤名称
     */
    private void setCurrentStepName(List<WorkVo> voList){
        try {
            //获取参数：流程实例id集合
            List<String> procInstIds = new ArrayList<>();
            for(WorkVo vo: voList){
                if(procInstIds.contains(vo.getProcInstId())){continue;}
                procInstIds.add(vo.getProcInstId());
            }

            //获取对应的步骤，并与流程实例id对应
            List<WorkVo> stepList = myWorkMapper.getCurrentStep(procInstIds);
            Map<String, String> map = new HashMap<>();
            for(WorkVo step: stepList){
                map.put(step.getProcInstId(), step.getCurrentStepName());
            }

            //设置属性
            for(WorkVo vo: voList){
                vo.setCurrentStepName(map.get(vo.getProcInstId()));
            }
        }catch (Exception e){
            log.error("设置流程的当前步骤名称异常：{}", e.getMessage());
        }
    }

}
