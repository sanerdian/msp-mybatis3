package com.jnetdata.msp.flowable.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.flowable.model.FlowFocus;
import com.jnetdata.msp.flowable.vo.WorkVo;
import com.jnetdata.msp.vo.BaseVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

public interface FlowFocusService extends BaseService<FlowFocus> {


    /**
     * 我的关注
     */
    JsonResult<Pager<WorkVo>> myFocus(BaseVo<WorkVo> vo);

    /**
     * 关注流程
     */
    JsonResult<String> addFocus(FlowFocus entity);

    /**
     * 取消关注
     */
    JsonResult<String> removeFocus(FlowFocus entity);


}
