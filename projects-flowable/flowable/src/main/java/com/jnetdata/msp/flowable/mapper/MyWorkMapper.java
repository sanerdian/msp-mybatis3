package com.jnetdata.msp.flowable.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jnetdata.msp.flowable.vo.WorkVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyWorkMapper {

    /**
     * 我的待办
     */
    IPage<WorkVo> myTodo(IPage page, @Param("vo") WorkVo vo);

    /**
     * 我的已办
     */
    IPage<WorkVo> myDone(IPage page, @Param("vo") WorkVo vo);

    /**
     * 获取流程的当前步骤
     */
    List<WorkVo> getCurrentStep(List<String> procInstIds);
}
