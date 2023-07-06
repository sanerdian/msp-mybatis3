package com.jnetdata.msp.media.service.impl;

//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
//import com.jnetdata.msp.media.mapper.JobApiMapper;
//import com.jnetdata.msp.media.mapper.StatisticsMapper;
import com.jnetdata.msp.media.mapper.UserImportMapper;
import com.jnetdata.msp.media.model.JobApi;
//import com.jnetdata.msp.media.service.StatisticsService;
import com.jnetdata.msp.media.service.UserImportService;
import com.jnetdata.msp.media.vo.Lz_WorkerVo;
//import com.jnetdata.msp.media.vo.tjtVo;
//import com.jnetdata.msp.media.vo.voteVo;
//import com.jnetdata.msp.media.vo.xinwenVo;
import com.jnetdata.msp.media.vo.xinwenPtVo;
import com.jnetdata.msp.tlujy.collect.mapper.CollectMapper;
import com.jnetdata.msp.tlujy.collect.model.Collect;
import com.jnetdata.msp.tlujy.joinuser.mapper.JoinuserMapper;
import com.jnetdata.msp.tlujy.joinuser.model.Joinuser;
//import com.jnetdata.msp.tlujy.vote.model.Vote;
//import com.jnetdata.msp.tlujy.vote_content.model.VoteContent;
//import com.jnetdata.msp.tlujy.vote_content.service.VoteContentService;
//import com.jnetdata.msp.tlujy.yjfk_photo.model.YjfkPhoto;
import com.jnetdata.msp.tlujy.xinwen020.mapper.Xinwen020Mapper;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import com.jnetdata.msp.tlujy.xinwen_comment.mapper.XinwenCommentMapper;
import com.jnetdata.msp.tlujy.xinwen_comment.model.XinwenComment;
import com.jnetdata.msp.tlujy.xinwen_evaluate.mapper.XinwenEvaluateMapper;
import com.jnetdata.msp.tlujy.xinwen_evaluate.model.XinwenEvaluate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;

//import java.util.ArrayList;
import java.util.List;
//import java.util.Map;

@Service
public class UserImportServiceImpl extends BaseServiceImpl<UserImportMapper, JobApi> implements UserImportService {

    @Autowired
    UserImportMapper userImportMapper;
    @Autowired
    JoinuserMapper joinuserMapper;
    @Autowired
    XinwenCommentMapper xinwenCommentMapper;
    @Autowired
    XinwenEvaluateMapper xinwenEvaluateMapper;
    @Autowired
    CollectMapper collectMapper;
    @Autowired
    Xinwen020Mapper xinwen020Mapper;

    @Override
    public Lz_WorkerVo getInfobyExternalid(String externalid) {
        return userImportMapper.getInfobyExternalid(externalid);
    }

    @Override
    public Lz_WorkerVo getInfobyUserid(Long userid) {
        List<Joinuser> joinusers=joinuserMapper.selectList(new PropertyWrapper(Joinuser.class).eq("userid",String.valueOf(userid)).getWrapper());

        if(joinusers!=null&&joinusers.size()>0){
            return userImportMapper.getInfobyExternalid(joinusers.get(0).getOutid());
        }

        return null;
    }

    @Override
    public int getCommentCount(Long userid) {
        int count=userImportMapper.CommentXinwenCount(userid);
        return count;
    }

    @Override
    public int getEvaluateCount(Long userid) {
        int count=userImportMapper.EvaluateXinwenCount(userid);
        return count;
    }

    @Override
    public int getCollectCount(Long userid) {
        int count=userImportMapper.CollectXinwenCount(userid);
        return count;
    }

    @Override
    public List<xinwenPtVo> listCollectXinwen(Long userid) {

        List<xinwenPtVo> list=userImportMapper.listCollectXinwen(userid);

        return list;
    }

    @Override
    public List<xinwenPtVo> listCommentXinwen(Long userid) {

        List<xinwenPtVo> list=userImportMapper.listCommentXinwen(userid);

        return list;
    }

    @Override
    public xinwenPtVo getCommentXinwen(Long id) {

        xinwenPtVo list=userImportMapper.getCommentXinwen(id);

        return list;
    }

    @Override
    public List<xinwenPtVo> listEvaluateXinwen(Long userid) {

        List<xinwenPtVo> list=userImportMapper.listEvaluateXinwen(userid);

        return list;
    }



}
