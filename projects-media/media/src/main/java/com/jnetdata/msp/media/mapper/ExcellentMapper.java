package com.jnetdata.msp.media.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.vo.*;
import com.jnetdata.msp.tlujy.investigate.model.Investigate;
import com.jnetdata.msp.tlujy.vote.model.Vote;
import com.jnetdata.msp.tlujy.xinwen_comment.model.XinwenComment;
import com.jnetdata.msp.tlujy.xinwen_evaluate.model.XinwenEvaluate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExcellentMapper extends BaseMapper<JobApi> {

    //根据条件查询新闻
    List<xinwenVo> selectPageXinwen(@Param("min") Long min, @Param("max") Long max, @Param("entity") xinwenVo xinwenVo , @Param("workerVo") Lz_WorkerVo lz_workerVo );

    //根据条件查询新闻
    int selectPageXinwenMaxCount(@Param("entity") xinwenVo entity , @Param("workerVo") Lz_WorkerVo lz_workerVo );

    //根据条件查询新闻
    List<Vote> selectPageVote(@Param("min") Long min, @Param("max") Long max, @Param("entity") voteVo entity);

    List<Investigate> selectInvestigateList(@Param("userid") String userid);

    //根据条件查询新闻
    int selectPageVoteMaxCount(@Param("entity") voteVo entity);

    //判断当前登陆人是否符合人员属性
    int checkLz_Worker(@Param("userid") String userid,@Param("pushsql") String pushsql);

    List<TreeVo> userZd(@Param("dwbsm") String dwbsm);

    void updateXiwenfollowMax (@Param("ptuserid") String userid ,@Param("min") int min ,@Param("max") int max);

    void updateXiwenfollowMin (@Param("ptuserid") String userid ,@Param("min") int min ,@Param("max") int max);

    List<String> bmParentList(@Param("dwbsm") String dwbsm);
}
