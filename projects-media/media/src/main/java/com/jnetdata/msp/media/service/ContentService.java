package com.jnetdata.msp.media.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.vo.*;
import com.jnetdata.msp.member.limit.model.Permission;
import com.jnetdata.msp.tlujy.investigate.model.Investigate;
import com.jnetdata.msp.tlujy.vote.model.Vote;
import com.jnetdata.msp.tlujy.vote_user.model.VoteUser;
import com.jnetdata.msp.tlujy.votetheme.model.Votetheme;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import com.jnetdata.msp.tlujy.xinwen_comment.model.XinwenComment;
import com.jnetdata.msp.tlujy.xinwen_evaluate.model.XinwenEvaluate;
import com.jnetdata.msp.tlujy.xinwen_record.model.XinwenRecord;
import com.jnetdata.msp.tlujy.yjfk.model.Yjfk;
import com.jnetdata.msp.vo.BaseVo;
import org.apache.http.ParseException;
import org.thenicesys.data.api.Pager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ContentService extends BaseService<JobApi> {

    void updateXiwentopping(Xinwen020 xinwen020);

    List<OptionsVo> listXiwentopping(Long lmid);

    void updateXiwenrotation(Xinwen020 xinwen020);

    List<OptionsVo> listXiwenrotation(Long lmid);




}
