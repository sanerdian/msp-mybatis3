package com.jnetdata.msp.media.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.vo.*;
import com.jnetdata.msp.tlujy.integral.model.Integral;
import com.jnetdata.msp.tlujy.vote.model.Vote;
import com.jnetdata.msp.tlujy.vote_user.model.VoteUser;
import com.jnetdata.msp.tlujy.votetheme.model.Votetheme;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import com.jnetdata.msp.tlujy.xinwen_comment.model.XinwenComment;
import com.jnetdata.msp.tlujy.xinwen_evaluate.model.XinwenEvaluate;
import com.jnetdata.msp.tlujy.yjfk.model.Yjfk;
import org.thenicesys.data.api.Pager;

import javax.servlet.ServletOutputStream;
import java.util.List;

/**
 * Created by 19912 on 2020/8/15.
 */
public interface ExpertService extends BaseService<JobApi> {

    Pager<voteVo> votelist(Page<Vote> page, voteVo entity);

    Pager<Votetheme> votethemelist(Page<Votetheme> page, VoteThemeVo entity);

    VoteFromVo getvote(Long id);

    Pager<voteVo> voteContentlist(Page<Vote> page, voteVo entity);

    Pager<voteUserVo> voteUserlist(Page<VoteUser> page, voteUserVo entity);

    VoteFromVo voteFrom(VoteFromVo vo);

    int votePush(PushSettingVo vo);

    Pager<CommentCenterVo> xinwenlist(Page<Xinwen020> page, CommentCenterVo entity);

    Pager<CommentVo> listcommentVo(Page<XinwenComment> page, CommentVo entity);

    Pager<CommentVo> listEvaluateVo(Page<XinwenEvaluate> page, CommentVo entity);

    Pager<IntegralVo> listIntegralVo(Page<Integral> page, IntegralVo entity);

    int IntegralAdd(Long counts);

    Pager<yjfkVo> listyjfkVo(Page<Yjfk> page, yjfkVo entity);

    void export(ServletOutputStream os, List<List<String>> List, String[] title, String sheetName);

    void votedeletes(String votedeletes);

    void votethemedeletes(String votedeletes);

}
