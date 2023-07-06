package com.jnetdata.msp.flowable.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.flowable.model.FlowFocus;
import com.jnetdata.msp.flowable.model.FlowSuspend;
import com.jnetdata.msp.flowable.vo.ProcessVo;
import com.jnetdata.msp.flowable.vo.WorkVo;
import com.jnetdata.msp.vo.BaseVo;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

public interface FlowSuspendService extends BaseService<FlowSuspend> {

    /**
     * 挂起流程实例
     */
    JsonResult<String> suspend(FlowSuspend entity);

    /**
     * 激活流程实例
     */
    JsonResult<String> activate(FlowSuspend entity);

    /**
     * 我挂起的
     */
    JsonResult<Pager<WorkVo>> mySuspend(BaseVo<WorkVo> vo);
}
