package com.jnetdata.msp.media.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.vo.voteVo;
import com.jnetdata.msp.media.vo.xinwenVo;
import com.jnetdata.msp.tlujy.investigate.model.Investigate;
import com.jnetdata.msp.tlujy.vote.model.Vote;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContentMapper extends BaseMapper<JobApi> {

    void updateXiwenrotationMax (@Param("min") int min ,@Param("max") int max);

    void updateXiwenrotationMin (@Param("min") int min ,@Param("max") int max);

    void updateXiwentoppingMax (@Param("min") int min ,@Param("max") int max);

    void updateXiwentoppingMin (@Param("min") int min ,@Param("max") int max);
    void removeTopping(@Param("id") long id);
}
