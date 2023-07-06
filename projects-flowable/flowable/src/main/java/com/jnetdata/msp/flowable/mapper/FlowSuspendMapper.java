package com.jnetdata.msp.flowable.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.flowable.model.FlowFocus;
import com.jnetdata.msp.flowable.model.FlowSuspend;
import com.jnetdata.msp.flowable.vo.WorkVo;
import org.apache.ibatis.annotations.Param;

public interface FlowSuspendMapper extends BaseMapper<FlowSuspend> {


    IPage<WorkVo> listPage(IPage page, @Param("vo") WorkVo vo);
}
