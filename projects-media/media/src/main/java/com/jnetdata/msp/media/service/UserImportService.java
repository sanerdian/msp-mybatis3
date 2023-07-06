package com.jnetdata.msp.media.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.vo.*;
import com.jnetdata.msp.tlujy.joinuser.model.Joinuser;
import com.jnetdata.msp.tlujy.vote.model.Vote;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import org.thenicesys.data.api.Pager;

import java.util.List;
import java.util.Map;

/**
 * Created by 19912 on 2020/8/15.
 */
public interface UserImportService extends BaseService<JobApi> {

    Lz_WorkerVo getInfobyExternalid(String externalid);

    Lz_WorkerVo getInfobyUserid(Long userid);

    int getCommentCount(Long userid);

    int getEvaluateCount(Long userid);

    int getCollectCount(Long userid);

    List<xinwenPtVo> listCollectXinwen(Long userid);

    List<xinwenPtVo> listCommentXinwen(Long userid);

    xinwenPtVo getCommentXinwen(Long id);

    List<xinwenPtVo> listEvaluateXinwen(Long userid);
}

