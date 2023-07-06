package com.jnetdata.msp.flowable.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.flowable.model.FlowFocus;
import com.jnetdata.msp.flowable.vo.WorkVo;
import org.apache.ibatis.annotations.Param;

public interface FlowFocusMapper extends BaseMapper<FlowFocus> {


    IPage<WorkVo> listPage(IPage page, @Param("vo") WorkVo vo);
}
