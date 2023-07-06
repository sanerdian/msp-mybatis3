package com.jnetdata.msp.media.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.vo.*;
import com.jnetdata.msp.tlujy.vote.model.Vote;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserImportMapper extends BaseMapper<JobApi> {

    Lz_WorkerVo getInfobyExternalid(@Param("externalid")String externalid);

    List<xinwenPtVo> listCollectXinwen(@Param("userid") Long userid);

    List<xinwenPtVo> listCommentXinwen(@Param("userid") Long userid);

    xinwenPtVo getCommentXinwen(@Param("id") Long id);

    List<xinwenPtVo> listEvaluateXinwen(@Param("userid") Long userid);

    int CommentXinwenCount(@Param("userid") Long userid);

    int EvaluateXinwenCount(@Param("userid") Long userid);

    int CollectXinwenCount(@Param("userid") Long userid);
}
