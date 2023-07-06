package com.jnetdata.msp.media.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.media.mapper.JobApiMapper;
import com.jnetdata.msp.media.mapper.StatisticsMapper;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.service.StatisticsService;
import com.jnetdata.msp.media.util.ValueHelper;
import com.jnetdata.msp.media.vo.*;
import com.jnetdata.msp.tlujy.vote.model.Vote;
import com.jnetdata.msp.tlujy.vote_content.model.VoteContent;
import com.jnetdata.msp.tlujy.vote_content.service.VoteContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl extends BaseServiceImpl<JobApiMapper, JobApi> implements StatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Autowired
    private VoteContentService voteContentService;

    @Override
    public Pager<xinwenVo> xinwenlist(Page<xinwenVo> page, xinwenVo entity) {

        long min = (page.getCurrent()-1)*page.getSize();
        long max = page.getCurrent()*page.getSize();

        List<xinwenVo> list = statisticsMapper.selectPageXinwen(min,max,entity);
        int counts=statisticsMapper.selectPageXinwenMaxCount(entity);

        //查询新闻表

        Pager pager= ValueHelper.changePage(page);
        pager.setRecords(list);
        pager.setTotal(counts);

        return pager;
    }

    @Override
    public TjtVo bmtjt(TjtVo vo){

        List<Map<String,Object>> list= statisticsMapper.tjgroup(vo);

        List<Object> listname = new ArrayList<>();
        List<Object> listvalue = new ArrayList<>();

        if(list!=null&&list.size()>0){
            for (Map<String,Object> map: list) {
                listname.add(map.get("GROUPNAME"));
                listvalue.add(map.get("COUNTS"));
            }
        }

        vo.setListname(listname);
        vo.setListvalue(listvalue);

        return vo;
    }

    @Override
    public TjtVo bmtjtpl(TjtVo vo){

        List<Map<String,Object>> list= statisticsMapper.tjgrouppl(vo);

        List<Object> listname = new ArrayList<>();
        List<Object> listvalue = new ArrayList<>();

        if(list!=null&&list.size()>0){
            for (Map<String,Object> map: list) {
                listname.add(map.get("GROUPNAME"));
                listvalue.add(map.get("COUNTS"));
            }
        }

        vo.setListname(listname);
        vo.setListvalue(listvalue);

        return vo;
    }

    @Override
    public TjtVo pltjt(TjtVo vo) {
        List<Map<String,Object>> list= statisticsMapper.tjcomment(vo);

        List<Object> listname = new ArrayList<>();
        List<Object> listvalue = new ArrayList<>();

        if(list!=null&&list.size()>0){
            for (Map<String,Object> map: list) {
                listname.add(map.get("CRTIME"));
                listvalue.add(map.get("COUNTS"));
            }
        }

        vo.setListname(listname);
        vo.setListvalue(listvalue);

        return vo;
    }

    @Override
    public TjtVo dztjt(TjtVo vo) {
        List<Map<String,Object>> list= statisticsMapper.tjevaluate(vo);

        List<Object> listname = new ArrayList<>();
        List<Object> listvalue = new ArrayList<>();

        if(list!=null&&list.size()>0){
            for (Map<String,Object> map: list) {
                listname.add(map.get("CRTIME"));
                listvalue.add(map.get("COUNTS"));
            }
        }

        vo.setListname(listname);
        vo.setListvalue(listvalue);

        return vo;
    }

    @Override
    public Map<String,Object> usertjCount(TjtVo vo) {
        Map<String,Object> map= statisticsMapper.tjuser(vo);
        return map;
    }

    @Override
    public Map<String,Object> usertjhyCount(TjtVo vo) {
        Map<String,Object> map= statisticsMapper.tjhyuser(vo);
        return map;
    }

    @Override
    public Map<String,Object> voteUserCount(TjtVo vo) {
        Map<String,Object> map= statisticsMapper.voteUserCount(vo);
        return map;
    }

    @Override
    public int tjage(TjtVo vo) {
        return statisticsMapper.tjage(vo);
    }

    @Override
    public List<Map<String,Object>> votedepttj(TjtVo vo) {
        List<Map<String,Object>> map= statisticsMapper.votedepttj(vo);
        return map;
    }

    @Override
    public List<Map<String,Object>> voteContentdepttj(TjtVo vo) {
        List<Map<String,Object>> map= statisticsMapper.voteContentdepttj(vo);
        return map;
    }

    @Override
    public List<Map<String,Object>> votecommontj(TjtVo vo) {
        List<Map<String,Object>> map= statisticsMapper.votecommontj(vo);
        return map;
    }

    @Override
    public Pager<voteVo> votelist(Page<Vote> page, voteVo entity) {

        long min = (page.getCurrent()-1)*page.getSize();
        long max = page.getCurrent()*page.getSize();

        List<Vote> votelist = statisticsMapper.selectPageVote(min,max,entity);
        int counts=statisticsMapper.selectPageVoteMaxCount(entity);
        List<voteVo> list=new ArrayList<>();

        //查询新闻表

        for (int i = 0; i <votelist.size(); i++) {
            Vote vote=votelist.get(i);

            voteVo votevo=new voteVo();
            votevo.setId(vote.getId());
            votevo.setTitle(vote.getTitle());
            votevo.setCruser(vote.getCrUser());
            votevo.setCreateTime(vote.getCreateTime());
            votevo.setVoteCounts(statisticsMapper.selectTpCounts(vote.getId()));
            votevo.setVoteUserCounts(statisticsMapper.selectTpUserCounts(vote.getId()));
            list.add(votevo);
        }
        Pager pager= ValueHelper.changePage(page);
        pager.setRecords(list);
        pager.setTotal(counts);

        return pager;
    }

    @Override
    public voteVo getVoteContent(Long votecontentid){
        VoteContent voteContent=voteContentService.getById(votecontentid);

        voteVo voteVo=new voteVo();
        voteVo.setTitle(voteContent.getTitle());
        voteVo.setVoteCounts(statisticsMapper.selectTpContentCounts(votecontentid));
        voteVo.setVoteUserCounts(statisticsMapper.selectTpContentUserCounts(votecontentid));

        return voteVo;
    }


}
