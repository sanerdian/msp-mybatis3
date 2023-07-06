package com.jnetdata.msp.media.service;

import com.jnetdata.msp.media.vo.survey.InvestigateCurrentVo;
import com.jnetdata.msp.media.vo.survey.InvestigateQueryVo;
import com.jnetdata.msp.media.vo.survey.InvestigateVo;
import com.jnetdata.msp.media.vo.survey.InvestigateWithCount;
import com.jnetdata.msp.tlujy.answer_user.model.AnswerUser;
import com.jnetdata.msp.tlujy.investigate.model.Investigate;
import com.jnetdata.msp.tlujy.investigate_user.model.InvestigateUser;
import com.jnetdata.msp.vo.BaseVo;
import org.thenicesys.data.api.Pager;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SurveyService {
    Long save(InvestigateVo vo);
    InvestigateVo getById(Long id);

    void deletesOnDisk(List<Long> ids);

    void deletesByRemark(List<Long> ids);

    void updates(List<Investigate> investigates);

    Map<String, List> getAnswers(Long id);

    void saveAnswers(Long id, Map<Long, List> answerMap,Long secondCost);

    List<InvestigateCurrentVo> getSurvey4CurrentUser(Investigate template);

    Pager<InvestigateWithCount> pageSurvey(BaseVo<InvestigateQueryVo> vo);

    List<Map> countRecoverByGroup(Long investigateId);

    Pager<InvestigateUser> pageInvestigateUser(BaseVo<InvestigateUser> vo);

    Map countByAnswer(Long investigateId);

    void exportAnswer(AnswerUser vo, HttpServletResponse response);

    void exportInvestigate(Long investigateid, HttpServletResponse response);

    void commitAnswers(Long investigateid);
}
