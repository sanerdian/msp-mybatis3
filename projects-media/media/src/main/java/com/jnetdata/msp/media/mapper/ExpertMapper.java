package com.jnetdata.msp.media.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.vo.IntegralVo;
import com.jnetdata.msp.media.vo.CommentVo;
import com.jnetdata.msp.tlujy.xinwen_comment.model.XinwenComment;
import com.jnetdata.msp.tlujy.xinwen_evaluate.model.XinwenEvaluate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExpertMapper extends BaseMapper<JobApi> {

    //查询评论中心详情列表信息
    List<XinwenComment> selectPageComment(@Param("min") Long min, @Param("max") Long max, @Param("entity") CommentVo entity);

    //查询评论中心详情列表信息,最大数
    int selectPageCommentMaxCount(@Param("entity") CommentVo entity);

    //查询点赞列表信息
    List<XinwenEvaluate> selectPageEvaluate(@Param("min") Long min, @Param("max") Long max, @Param("entity") CommentVo entity);

    //查询点赞列表信息,最大数
    int selectPageEvaluateMaxCount(@Param("entity") CommentVo entity);

    //查询积分列表
    List<IntegralVo> selectPageIntegral(@Param("min") Long min, @Param("max") Long max, @Param("entity") IntegralVo entity);

    //查询积分,最大数
    int selectPageIntegralMaxCount(@Param("entity") IntegralVo entity);

}
