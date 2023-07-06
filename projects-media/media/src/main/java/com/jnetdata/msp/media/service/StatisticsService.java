package com.jnetdata.msp.media.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.vo.TjtVo;
import com.jnetdata.msp.media.vo.voteVo;
import com.jnetdata.msp.media.vo.xinwenVo;
import com.jnetdata.msp.tlujy.vote.model.Vote;
import org.thenicesys.data.api.Pager;

import java.util.List;
import java.util.Map;

/**
 * Created by 19912 on 2020/8/15.
 */
public interface StatisticsService extends BaseService<JobApi> {

    Pager<xinwenVo> xinwenlist(Page<xinwenVo> page, xinwenVo entity);

    TjtVo bmtjt(TjtVo vo);

    TjtVo bmtjtpl(TjtVo vo);

    TjtVo pltjt(TjtVo vo);

    TjtVo dztjt(TjtVo vo);

    Map<String,Object> usertjCount(TjtVo vo);

    Map<String,Object> usertjhyCount(TjtVo vo);

    Map<String,Object> voteUserCount(TjtVo vo);

    int tjage(TjtVo vo);

    List<Map<String,Object>> votedepttj(TjtVo vo);

    List<Map<String,Object>> voteContentdepttj(TjtVo vo);

    List<Map<String,Object>> votecommontj(TjtVo vo);

    Pager<voteVo> votelist(Page<Vote> page, voteVo entity);

    voteVo getVoteContent(Long votecontentid);

}

