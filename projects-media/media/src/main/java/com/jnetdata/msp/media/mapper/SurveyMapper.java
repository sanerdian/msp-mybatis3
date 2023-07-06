package com.jnetdata.msp.media.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jnetdata.msp.media.model.JobApi;

import java.util.List;
import java.util.Map;

public interface SurveyMapper extends BaseMapper<JobApi> {
    /**从调查问卷-用户推送表中按问卷id、问卷回收情况进行统计*/
    List<Map> countByRecoverstatus(List<Long> investigateIds);
    /**从调查问卷-用户推送表中按用户所在部门为指定的调查问卷统计:问卷发布量、问卷回收量、问卷未回收量*/
    List<Map> countRecoverByGroup(Long investigateId);
    /**从调查问卷-答题表中按选择题答案id进行统计*/
    List<Map> countByAnswer(Long investigateId);
    /**从调查问卷-答题表中按问卷统计统计指定用户已经答的题目数以及最后答题时间*/
    List<Map> countAnsweredTopicByInvest(Long userid);
    /**从调查问卷-备选答案表中按问卷统计题目数*/
    List<Map> countTopicByInvest(List<Long> investigateIds);
}
