package com.jnetdata.msp.media.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.vo.TjtVo;
import com.jnetdata.msp.media.vo.voteVo;
import com.jnetdata.msp.media.vo.xinwenVo;
import com.jnetdata.msp.tlujy.vote.model.Vote;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StatisticsMapper extends BaseMapper<JobApi> {

    //根据条件查询新闻
    List<xinwenVo> selectPageXinwen(@Param("min") Long min, @Param("max") Long max, @Param("entity") xinwenVo entity);

    //根据条件查询新闻
    int selectPageXinwenMaxCount(@Param("entity") xinwenVo entity);

    List<Map<String,Object>> tjgroup(@Param("entity") TjtVo vo);

    List<Map<String,Object>> tjgrouppl(@Param("entity") TjtVo vo);

    List<Map<String,Object>> tjcomment(@Param("entity") TjtVo vo);

    List<Map<String,Object>> tjevaluate(@Param("entity") TjtVo vo);

    Map<String,Object> tjuser(@Param("entity") TjtVo vo);

    Map<String,Object> tjhyuser(@Param("entity") TjtVo vo);

    Map<String,Object> voteUserCount(@Param("entity") TjtVo vo);

    int tjage(@Param("entity") TjtVo vo);

    List<Map<String,Object>> votedepttj(@Param("entity") TjtVo vo);

    List<Map<String,Object>> voteContentdepttj(@Param("entity") TjtVo vo);

    List<Map<String,Object>> votecommontj(@Param("entity") TjtVo vo);

    //根据条件查询新闻
    List<Vote> selectPageVote(@Param("min") Long min, @Param("max") Long max, @Param("entity") voteVo entity);

    //根据条件查询新闻
    int selectPageVoteMaxCount(@Param("entity") voteVo entity);

    int selectTpCounts(@Param("voteid") long voteid);

    int selectTpUserCounts(@Param("voteid") long voteid);

    int selectTpContentCounts(@Param("votecontentid") long votecontentid);

    int selectTpContentUserCounts(@Param("votecontentid") long votecontentid);

}
