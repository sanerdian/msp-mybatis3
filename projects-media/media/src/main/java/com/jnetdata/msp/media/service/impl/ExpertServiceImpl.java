package com.jnetdata.msp.media.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.manage.column.service.ProgramaService;
import com.jnetdata.msp.media.mapper.ExpertMapper;
import com.jnetdata.msp.media.mapper.JobApiMapper;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.service.CustomizedService;
import com.jnetdata.msp.media.service.ExpertService;
import com.jnetdata.msp.media.service.PushService;
import com.jnetdata.msp.media.service.UserImportService;
import com.jnetdata.msp.media.util.ValueHelper;
import com.jnetdata.msp.media.util.publicMethodUtil;
import com.jnetdata.msp.media.vo.*;
import com.jnetdata.msp.member.group.model.Groupinfo;
import com.jnetdata.msp.member.group.model.GrpUser;
import com.jnetdata.msp.member.group.service.GroupService;
import com.jnetdata.msp.member.group.service.GrpUserService;
import com.jnetdata.msp.member.role.service.RoleUserService;
import com.jnetdata.msp.member.user.service.UserService;
import com.jnetdata.msp.tlujy.integral.mapper.IntegralMapper;
import com.jnetdata.msp.tlujy.integral.model.Integral;
import com.jnetdata.msp.tlujy.integral.service.IntegralService;
import com.jnetdata.msp.tlujy.vote.mapper.VoteMapper;
import com.jnetdata.msp.tlujy.vote.model.Vote;
import com.jnetdata.msp.tlujy.vote_content.mapper.VoteContentMapper;
import com.jnetdata.msp.tlujy.vote_content.model.VoteContent;
import com.jnetdata.msp.tlujy.vote_user.mapper.VoteUserMapper;
import com.jnetdata.msp.tlujy.vote_user.model.VoteUser;
import com.jnetdata.msp.tlujy.vote_user.service.VoteUserService;
import com.jnetdata.msp.tlujy.votetheme.mapper.VotethemeMapper;
import com.jnetdata.msp.tlujy.votetheme.model.Votetheme;
import com.jnetdata.msp.tlujy.xinwen020.mapper.Xinwen020Mapper;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import com.jnetdata.msp.tlujy.xinwen020.service.Xinwen020Service;
import com.jnetdata.msp.tlujy.xinwen_comment.mapper.XinwenCommentMapper;
import com.jnetdata.msp.tlujy.xinwen_comment.model.XinwenComment;
import com.jnetdata.msp.tlujy.xinwen_comment.service.XinwenCommentService;
import com.jnetdata.msp.tlujy.xinwen_evaluate.mapper.XinwenEvaluateMapper;
import com.jnetdata.msp.tlujy.xinwen_evaluate.model.XinwenEvaluate;
import com.jnetdata.msp.tlujy.xinwen_evaluate.service.XinwenEvaluateService;
import com.jnetdata.msp.tlujy.yjfk.mapper.YjfkMapper;
import com.jnetdata.msp.tlujy.yjfk.model.Yjfk;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpertServiceImpl extends BaseServiceImpl<JobApiMapper, JobApi> implements ExpertService {

    @Autowired
    private Xinwen020Service xinwen020Service;
    @Autowired
    private ProgramaService programaService;
    @Autowired
    private XinwenCommentService xinwenCommentService;
    @Autowired
    private IntegralService integralService;
    @Autowired
    private XinwenEvaluateService xinwenEvaluateService;
    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private GrpUserService grpUserService;
    @Autowired
    private VoteUserService voteUserService;
    @Autowired
    private XinwenCommentMapper xinwenCommentMapper;
    @Autowired
    private IntegralMapper integralMapper;
    @Autowired
    private Xinwen020Mapper xinwen020Mapper;
    @Autowired
    private YjfkMapper yjfkMapper;
    @Autowired
    private ExpertMapper expertMapper;
    @Autowired
    private XinwenEvaluateMapper xinwenEvaluateMapper;
    @Autowired
    private VotethemeMapper votethemeMapper;
    @Autowired
    private VoteMapper voteMapper;
    @Autowired
    private VoteContentMapper voteContentMapper;
    @Autowired
    private VoteUserMapper voteUserMapper;
    @Autowired
    private CustomizedService customizedService;
    @Autowired
    private ConfigModelService configModelService;
    @Autowired
    private UserImportService userImportService;
    @Autowired
    private PushService pushService;

    @Override
    public Pager<Votetheme> votethemelist(Page<Votetheme> page, VoteThemeVo entity) {

        PropertyWrapper wrapper = new PropertyWrapper(Vote.class);

        wrapper.eq("siteid",entity.getSiteid());

        if(!StringUtils.isEmpty(entity.getThemename())){
            wrapper.like("themename",entity.getThemename());
        }

        if(!StringUtils.isEmpty(entity.getCrUser())) {
            wrapper.like("crUser", entity.getCrUser());
        }

        List<Votetheme> votelist = votethemeMapper.selectPage((IPage<Votetheme>) page,wrapper.getWrapper().orderBy(true,false,"crtime")).getRecords();
        int counts=votethemeMapper.selectCount(wrapper.getWrapper());

        List<VoteThemeVo> list=new ArrayList<>();

        //查询新闻表

        for (int i = 0; i <votelist.size(); i++) {
            Votetheme votetheme=votelist.get(i);

            VoteThemeVo vo=new VoteThemeVo();
            vo.setId(votetheme.getId());
            vo.setThemename(votetheme.getThemename());
            vo.setStartdate(votetheme.getStartdate());
            vo.setEnddate(votetheme.getEnddate());
            vo.setCrUser(votetheme.getCrUser());
            vo.setCreateTime(votetheme.getCreateTime());
            vo.setPubstatus(votetheme.getPubstatus());
            list.add(vo);
        }
        Pager pager= ValueHelper.changePage(page);
        pager.setRecords(list);
        pager.setTotal(counts);

        return pager;

    }

    @Override
    public Pager<voteVo> votelist(Page<Vote> page, voteVo entity) {

        PropertyWrapper wrapper = new PropertyWrapper(Vote.class);

        //根据投票主题查询
        wrapper.eq("votethemeid",entity.getVotethemeid());

        if(!StringUtils.isEmpty(entity.getTitle())){
            wrapper.like("title",entity.getTitle());
        }

        if(!StringUtils.isEmpty(entity.getCruser())) {
            wrapper.like("crUser", entity.getCruser());
        }

        if(!StringUtils.isEmpty(entity.getDocchannelName())){
            wrapper.where(" (select count(*) from CHANNELINFO c where c.channelid=DOCCHANNELID and chnlname like '%"+entity.getDocchannelName()+"%')>0",null);
        }

        if(entity.getDatetime()!=null){
            wrapper.between("startdate",entity.getDatetime()[0],entity.getDatetime()[1]);
        }

        List<Vote> votelist = voteMapper.selectPage((IPage<Vote>) page,wrapper.getWrapper().orderBy(true,false,"crtime")).getRecords();
        int counts=voteMapper.selectCount(wrapper.getWrapper());

        List<voteVo> list=new ArrayList<>();

        //查询新闻表

        for (int i = 0; i <votelist.size(); i++) {
            Vote vote=votelist.get(i);

            Programa programa=programaService.getById(vote.getDocchannelid());

            //栏目名称
            String channelName=programa!=null?programa.getName():"";

            int voteCount=voteUserService.count(new PropertyWrapper<>(VoteUser.class).eq("voteid",vote.getId()));

            voteVo votevo=new voteVo();
            votevo.setId(vote.getId());
            votevo.setTitle(vote.getTitle());
            votevo.setStartdate(vote.getStartdate());
            votevo.setEnddate(vote.getEnddate());
            votevo.setDocchannelName(channelName);
            votevo.setCruser(vote.getCrUser());
            votevo.setVoteCounts(voteCount);
            votevo.setState(vote.getState());
            list.add(votevo);
        }
        Pager pager= ValueHelper.changePage(page);
        pager.setRecords(list);
        pager.setTotal(counts);

        return pager;

    }

    @Override
    public VoteFromVo getvote(Long id) {

        //获取投票对象
        Vote vote=voteMapper.selectById(id);

        VoteFromVo vo=new VoteFromVo();

        List<VoteContent> content=voteContentMapper.selectList(new PropertyWrapper<>(VoteContent.class).eq("voteid",id).getWrapper().orderBy(true,false,"crtime"));

        List<VoteContent> list2=new ArrayList<>();
        for (int i = 0; i <content.size(); i++) {
            VoteContent voteContene=new VoteContent();
            voteContene.setId(content.get(i).getId());
            voteContene.setTitle(content.get(i).getTitle());
            voteContene.setPhoto(content.get(i).getPhoto());
            list2.add(voteContene);
        }

        vo.setContentList(list2);
        vo.setId(vote.getId());
        vo.setTitle(vote.getTitle());
        vo.setStartdate(vote.getStartdate());
        vo.setEnddate(vote.getEnddate());
        vo.setLimittype(vote.getLimittype());
        vo.setLimitcount(vote.getLimitcount());
        vo.setState(vote.getState());
        vo.setRemarks(vote.getRemarks());
        vo.setPhoto(vote.getPhoto());
        vo.setPushtocondition(vote.getPushtocondition());
        vo.setPushtogroup(vote.getPushtogroup());
        vo.setPushtorange(vote.getPushtorange());
        vo.setPushtouser(vote.getPushtouser());
        vo.setTuijianzhe(vote.getTuijianzhe());
        vo.setVotethemeid(vote.getVotethemeid());

        return vo;

    }


    @Override
    public Pager<voteVo> voteContentlist(Page<Vote> page, voteVo entity) {

        PropertyWrapper wrapper = new PropertyWrapper(Vote.class);

        wrapper.eq("voteid",entity.getVoteid());

        if(!StringUtils.isEmpty(entity.getTitle())){
            wrapper.like("title",entity.getTitle());
        }
        List<VoteContent> votelist = voteContentMapper.selectPage((IPage) ValueHelper.changePage(page),wrapper.getWrapper().orderBy(true,false,"crtime")).getRecords();
        int counts=voteContentMapper.selectCount(wrapper.getWrapper());

        List<voteVo> list=new ArrayList<>();

        //查询新闻表

        for (int i = 0; i <votelist.size(); i++) {
            VoteContent vote=votelist.get(i);

            Programa programa=programaService.getById(vote.getDocchannelid());

            //栏目名称
            String channelName=programa!=null?programa.getName():"";

            int voteCount=voteUserService.count(new PropertyWrapper<>(VoteUser.class).eq("votecontentid",vote.getId()));

            voteVo votevo=new voteVo();
            votevo.setId(vote.getId());
            votevo.setTitle(vote.getTitle());
            votevo.setCreateTime(vote.getCreateTime());
            votevo.setDocchannelName(channelName);
            votevo.setCruser(vote.getCrUser());
            votevo.setRemarks(vote.getRemarks());
            votevo.setVoteCounts(voteCount);
            list.add(votevo);
        }
        Pager pager= ValueHelper.changePage(page);
        pager.setRecords(list);
        pager.setTotal(counts);

        return pager;

    }

    @Override
    public Pager<voteUserVo> voteUserlist(Page<VoteUser> page, voteUserVo entity) {

        PropertyWrapper wrapper = new PropertyWrapper(VoteUser.class);

        wrapper.eq("voteid",entity.getVoteid());

        if(!StringUtils.isEmpty(entity.getCrUser())) {
            wrapper.like("crUser", entity.getCrUser());
        }

        if(!StringUtils.isEmpty(entity.getDocchannelName())){
            wrapper.where(" (select count(*) from CHANNELINFO c where c.channelid=DOCCHANNELID and chnlname like '%"+entity.getDocchannelName()+"%')>0",null);
        }

        if(entity.getCreateTime()!=null) {
            wrapper.between("createTime",entity.getCreateTime(),new Date(entity.getCreateTime().getTime()+(24*60*60*1000)));
        }

        List<VoteUser> votelist = voteUserMapper.selectPage((IPage<VoteUser>) page,wrapper.getWrapper().orderBy(true,false,"crtime")).getRecords();
        int counts=voteUserMapper.selectCount(wrapper.getWrapper());

        List<voteUserVo> list=new ArrayList<>();

        //查询新闻表

        for (int i = 0; i <votelist.size(); i++) {
            VoteUser vote=votelist.get(i);

            VoteContent vc=voteContentMapper.selectById(vote.getVotecontentid());

            Vote v=voteMapper.selectById(entity.getVoteid());

            Programa programa=programaService.getById(vote.getDocchannelid());

            //栏目名称
            String channelName=programa!=null?programa.getName():"";

            voteUserVo votevo=new voteUserVo();
            votevo.setId(vote.getId());
            votevo.setCreateTime(vote.getCreateTime());
            votevo.setDocchannelName(channelName);
            votevo.setCrUser(vote.getCrUser());
            votevo.setVotecontenttitle(vc.getTitle());
            votevo.setVotetitle(v.getTitle());
            votevo.setPhoto("");
            list.add(votevo);
        }
        Pager pager= ValueHelper.changePage(page);
        pager.setRecords(list);
        pager.setTotal(counts);

        return pager;
    }

    @Override
    public VoteFromVo voteFrom(VoteFromVo vo) {

       if(vo.getId()==null||vo.getId()==0L){//添加投票标题
           //添加投票
           voteMapper.insert(vo);

           if(vo.getContentList()!=null){

               for (int i = 0; i < vo.getContentList().size(); i++) {

                   VoteContent voteContent=new VoteContent();
                   voteContent.setVoteid(String.valueOf(vo.getId()));
                   voteContent.setTitle(vo.getContentList().get(i).getTitle());
                   voteContent.setPhoto(vo.getContentList().get(i).getPhoto());

                   voteContentMapper.insert(voteContent);
               }
           }
       }else{//修改投票，每次将投票标题和投票用户全部清空然后才能修改

           //删除投票标题
           voteContentMapper.delete(new PropertyWrapper<>(VoteContent.class).eq("voteid",vo.getId()).getWrapper());

           //删除投票用户
           voteUserMapper.delete(new PropertyWrapper<>(VoteUser.class).eq("voteid",vo.getId()).getWrapper());

           //新增投票信息
           if(vo.getContentList()!=null){

               for (int i = 0; i < vo.getContentList().size(); i++) {

                   VoteContent voteContent=new VoteContent();
                   voteContent.setVoteid(String.valueOf(vo.getId()));
                   voteContent.setTitle(vo.getContentList().get(i).getTitle());
                   voteContent.setPhoto(vo.getContentList().get(i).getPhoto());
                   voteContentMapper.insert(voteContent);
               }
           }

           //修改投票
           voteMapper.updateById(vo);
       }

       vo=this.getvote(vo.getId());

        return vo;
    }

    @Override
    public int votePush(PushSettingVo vo) {
        Vote vote = new Vote();
        vote.setId(vo.getId());
        if(vo.getUsers()!=null){
            vote.setPushtouser(JSON.toJSONString(vo.getUsers()));
        }
        if(vo.getGroups()!=null){
            vote.setPushtogroup(JSON.toJSONString(vo.getGroups()));
        }
        if(vo.getConditions()!=null){
            vote.setPushtocondition(JSON.toJSONString(vo.getConditions()));
        }
        vote.setPushtorange(vo.getRange());
        vote.setTuijianzhe(publicMethodUtil.getGroupNameForCurrentUser());
        vote.setPushsql(pushService.getPushedWorkers(vo));

        return voteMapper.updateById(vote);
    }

    @Override
    public Pager<CommentCenterVo> xinwenlist(Page<Xinwen020> page, CommentCenterVo entity) {

        PropertyWrapper wrapper = new PropertyWrapper(Integral.class);

        wrapper.eq("status","21");
        wrapper.eq("docstatus","0");
        wrapper.eq("siteid",entity.getSiteid());

        if(!StringUtils.isEmpty(entity.getTitle())){
            wrapper.like("title",entity.getTitle());
        }

        if(!StringUtils.isEmpty(entity.getCruser())) {
            wrapper.like("crUser", entity.getCruser());
        }

        if(entity.getCrtime()!=null) {
            wrapper.between("createTime",entity.getCrtime(),new Date(entity.getCrtime().getTime()+(24*60*60*1000)));
        }

        if(!StringUtils.isEmpty(entity.getDocchannelName())){
            wrapper.where(" (select count(*) from CHANNELINFO c where c.channelid=DOCCHANNELID and chnlname like '%"+entity.getDocchannelName()+"%')>0",null);
        }

        List<Xinwen020> xinwenlist = xinwen020Mapper.selectPage((IPage<Xinwen020>) page,wrapper.getWrapper().orderBy(true,false,"toppingorder asc,MODIFY_TIME")).getRecords();
        int counts=xinwen020Mapper.selectCount(wrapper.getWrapper());

        //获取这个人的推荐新闻
        List<XinwenSummaryVo> xinwen020List=customizedService.getPushXinwen(null);
        //新闻id合的字符串
        StringBuilder summaryStr=new StringBuilder();
        for (XinwenSummaryVo s:xinwen020List) {
            summaryStr.append(s.getId()+",");
        }

        List<CommentCenterVo> list=new ArrayList<>();

        //查询新闻表
        for (int i = 0; i <xinwenlist.size(); i++) {
            Xinwen020 xinwen=xinwenlist.get(i);

            Programa programa=programaService.getById(xinwen.getDocchannelid());

            //栏目名称
            String channelName=programa!=null?programa.getName():"";

            //评论数量
            int commentCount=xinwenCommentService.count(new PropertyWrapper<>(XinwenComment.class).eq("docid",xinwen.getId()));

            //点击数量
            int djcounts=xinwenEvaluateService.count(new PropertyWrapper<>(XinwenEvaluate.class).eq("IDVALUE",xinwen.getId()));

            CommentCenterVo comment=new CommentCenterVo();
            comment.setId(xinwen.getId());
            comment.setDocchannelName(channelName);
            comment.setCrtime(xinwen.getCreateTime());
            comment.setTitle(xinwen.getTitle());
            comment.setCruser(xinwen.getCrUser());
            comment.setCommentCount(commentCount);
            comment.setDjcounts(djcounts);
            comment.setFrozentype(xinwen.getFrozentype());

            //是否是推荐新闻
            if(summaryStr.toString().contains(String.valueOf(xinwen.getId()))){
                comment.setTjtype("1");
            }

            list.add(comment);
        }
        Pager pager= ValueHelper.changePage(page);
        pager.setRecords(list);
        pager.setTotal(counts);


        return pager;
    }

    @Override
    public Pager<CommentVo> listcommentVo(Page<XinwenComment> page, CommentVo entity) {

        if(entity.getCreateTime()!=null){
            entity.setCreateTime2(new Date(entity.getCreateTime().getTime()+(24*60*60*1000)));
        }

        long min = (page.getCurrent()-1)*page.getSize();
        long max = page.getCurrent()*page.getSize();

        List<XinwenComment> commentlist = expertMapper.selectPageComment(min,max,entity);
        int counts=expertMapper.selectPageCommentMaxCount(entity);

        //查询评论表
        List<CommentVo> list=new ArrayList<>();

        for (int i = 0; i <commentlist.size(); i++) {
            XinwenComment xinwen=commentlist.get(i);

            Long userid = xinwen.getCreateBy();

            //新闻对象
            Xinwen020 xinwen020=xinwen020Service.getById(xinwen.getDocid());

            List<String> names = new ArrayList<>();

            List<GrpUser> grpUsers = grpUserService.list(new PropertyWrapper<>(GrpUser.class).eq("userId", userid));
            if(grpUsers!=null&&grpUsers.size()>0){
                List<Long> ids = grpUsers.stream().map(m -> m.getGroupId()).collect(Collectors.toList());
                List<Groupinfo> groupinfos = groupService.list(new PropertyWrapper<>(Groupinfo.class).in("id", ids));
                names = groupinfos.stream().map(m -> m.getName()).collect(Collectors.toList());
            }

            String nameValues="";

            if(names!=null){
                for (int j = 0; j < names.size(); j++) {
                    nameValues+=","+names.get(j);
                }
            }

            if(nameValues.length()>1){
                nameValues=nameValues.substring(1);
            }


            CommentVo comment=new CommentVo();
            comment.setId(xinwen.getId());
            comment.setUsercomment(xinwen.getUsercomment());
            comment.setCommentstatus(xinwen.getCommentstatus());
            comment.setXinwentitle(ObjectUtils.isEmpty(xinwen020)?"":xinwen020.getTitle());

            //评论状态
            if(xinwen.getCommentstatus()!=null&&xinwen.getCommentstatus()==1L){
                comment.setCommentstatusStr("已隐藏");
            }else{
                comment.setCommentstatusStr("正常");
            }

            comment.setStatusdate(xinwen.getStatusdate());
            comment.setCrUser(xinwen.getCrUser());
            comment.setCreateTime(xinwen.getCreateTime());
            comment.setOrganize(nameValues);

            list.add(comment);
        }
        Pager pager= ValueHelper.changePage(page);
        pager.setRecords(list);
        pager.setTotal(counts);

        return pager;
    }



    @Override
    public Pager<CommentVo> listEvaluateVo(Page<XinwenEvaluate> page, CommentVo entity) {

        if(entity.getCreateTime()!=null){
            entity.setCreateTime2(new Date(entity.getCreateTime().getTime()+(24*60*60*1000)));
        }

        long min = (page.getCurrent()-1)*page.getSize();
        long max = page.getCurrent()*page.getSize();

        List<XinwenEvaluate> xinwenlist = expertMapper.selectPageEvaluate(min,max,entity);
        int counts=expertMapper.selectPageEvaluateMaxCount(entity);

        //查询评论表
        List<CommentVo> list=new ArrayList<>();

        for (int i = 0; i <xinwenlist.size(); i++) {
            XinwenEvaluate xinwen=xinwenlist.get(i);

            Long userid = xinwen.getCreateBy();

            //新闻对象
            Xinwen020 xinwen020=xinwen020Service.getById(xinwen.getIdvalue());

            List<String> names = new ArrayList<>();

            List<GrpUser> grpUsers = grpUserService.list(new PropertyWrapper<>(GrpUser.class).eq("userId", userid));
            if(grpUsers!=null&&grpUsers.size()>0){
                List<Long> ids = grpUsers.stream().map(m -> m.getGroupId()).collect(Collectors.toList());
                List<Groupinfo> groupinfos = groupService.list(new PropertyWrapper<>(Groupinfo.class).in("id", ids));
                names = groupinfos.stream().map(m -> m.getName()).collect(Collectors.toList());
            }

            String nameValues="";

            if(names!=null){
                for (int j = 0; j < names.size(); j++) {
                    nameValues+=","+names.get(j);
                }
            }

            if(nameValues.length()>1){
                nameValues=nameValues.substring(1);
            }


            CommentVo comment=new CommentVo();
            comment.setId(xinwen.getId());
            comment.setCrUser(xinwen.getCrUser());
            comment.setCreateTime(xinwen.getCreateTime());
            comment.setOrganize(nameValues);
            comment.setXinwentitle(xinwen020.getTitle());

            list.add(comment);
        }
        Pager pager= ValueHelper.changePage(page);
        pager.setRecords(list);
        pager.setTotal(counts);

        return pager;
    }




    @Override
    public Pager<IntegralVo> listIntegralVo(Page<Integral> page, IntegralVo entity) {

        long min = (page.getCurrent() - 1) * page.getSize();
        long max = page.getCurrent() * page.getSize();

        List<IntegralVo> list = expertMapper.selectPageIntegral(min, max, entity);
        int counts = expertMapper.selectPageIntegralMaxCount(entity);


        //查询新闻表
        Pager pager = ValueHelper.changePage(page);
        pager.setRecords(list);
        pager.setTotal(counts);

        return pager;
    }

    @Override
    public int IntegralAdd(Long counts) {

        int result=0;

        //获取用户id
        Long id = SessionUser.getCurrentUser().getId();

        List<Integral> list=integralMapper.selectList(new PropertyWrapper<>(Integral.class).eq("createBy",id).getWrapper());

        if(list!=null&&list.size()>0){
            Integral integral= list.get(0);
            integral.setCounts(integral.getCounts()+counts);
            result=integralMapper.updateById(integral);
        }else{
            Integral integral = new Integral();
            integral.setCounts(0L);
            result=integralMapper.insert(integral);
        }

        return result;
    }

    @Override
    public Pager<yjfkVo> listyjfkVo(Page<Yjfk> page, yjfkVo entity) {
        PropertyWrapper wrapper = new PropertyWrapper(Yjfk.class);

        if(!StringUtils.isEmpty(entity.getYijianneirong())){
            wrapper.or().like("yijianneirong",entity.getYijianneirong());
            wrapper.or().like("yjphone",entity.getYijianneirong());
        }

        if(!StringUtils.isEmpty(entity.getState())){
            if(entity.getState()!=0L){
                wrapper.eq("state",entity.getState());
            }else{
                wrapper.eq("state",entity.getState());
                wrapper.or();
                wrapper.isNull("state");
            }
        }

        if(entity.getDatetime()!=null){
            wrapper.between("createTime",entity.getDatetime()[0],entity.getDatetime()[1]);
        }

        //是答复时间
        if("1".equals(entity.getOrdertype())){
            wrapper.getWrapper().orderBy(true,false,"replydate");
        }else{
            wrapper.getWrapper().orderBy(true,false,"crtime");
        }

        List<Yjfk> list = yjfkMapper.selectPage((IPage<Yjfk>) page,wrapper.getWrapper()).getRecords();
        List<yjfkVo> volist = new ArrayList<>();


        int counts = yjfkMapper.selectCount(wrapper.getWrapper());

        for (int i = 0; i <list.size(); i++) {
            Yjfk yjfk=list.get(i);
            yjfkVo vo=new yjfkVo();

            Lz_WorkerVo replyuser = userImportService.getInfobyExternalid(yjfk.getReplyuserid());

            vo.setId(yjfk.getId());
            vo.setYijianneirong(yjfk.getYijianneirong());
            vo.setYjphone(yjfk.getYjphone());
            vo.setCreateTime(yjfk.getCreateTime());
            vo.setUserid(yjfk.getCrUser());
            vo.setReplyuserid(replyuser!=null?replyuser.getXm():"");
            vo.setReplydate(yjfk.getReplydate());
            vo.setModifyTime(yjfk.getModifyTime());
            vo.setTimingdate(yjfk.getTimingdate());
            vo.setState(yjfk.getState());
            vo.setFen(yjfk.getFen());
            vo.setOpinion(yjfk.getOpinion());
            volist.add(vo);
        }

        Pager pager= ValueHelper.changePage(page);
        pager.setRecords(volist);
        pager.setTotal(counts);

        return pager;
    }


    /**
     * 下载信息数据到excel
     * @param os
     * @param List
     * @param title
     * @param sheetName
     */
    @Override
    public void export(ServletOutputStream os, List<List<String>> List, String[] title, String sheetName) {
        HSSFWorkbook wb=null;
        try {
            wb=new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);

            HSSFRow titleRow = sheet.createRow(0);

            HSSFCellStyle style=wb.createCellStyle();

            //设置标题
            for (int i=0;i<title.length;i++){
                sheet.setColumnWidth(i,512*10);
                HSSFCell cell = titleRow.createCell(i);
                cell.setCellStyle(style);
                cell.setCellValue(title[i]);
            }

            //填充数据
            for (int i = 1; i < List.size()+1; i++) {
                HSSFRow row = sheet.createRow(i);
                for (int j = 0; j < title.length; j++) {
                    row.createCell(j).setCellValue(List.get(i-1).get(j));
                }
            }


            wb.write(os);
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void votedeletes(String votedeletes){

        String path = configModelService.getUploadPath(ConfigModel.dir_pic);

        if(!StringUtils.isEmpty(votedeletes)){

            String [] arr=votedeletes.split(",");

            for (int i = 0; i < arr.length; i++) {
                String id=arr[i];

                Vote vote=voteMapper.selectById(Long.valueOf(id));

                //文件不为空需要删除文件
                if (!StringUtils.isEmpty(vote.getPhoto())) {
                    //删除文件
                    File delfile=new File((path+"/"+vote.getPhoto()).replace("webpic","").replace("//","/"));

                    if(!delfile.exists()){
                        delfile.delete();
                    }
                }

                //删除投票
                voteMapper.deleteById(Long.valueOf(id));

                //获取投票内容列表，删除图片
                List<VoteContent> conList=voteContentMapper.selectList(new PropertyWrapper<>(VoteContent.class).eq("voteid",id).getWrapper());

                for (VoteContent con : conList) {
                    //文件不为空需要删除文件
                    if (!StringUtils.isEmpty(con.getPhoto())) {
                        //删除文件
                        File delfile=new File((path+"/"+con.getPhoto()).replace("webpic","").replace("//","/"));

                        if(!delfile.exists()){
                            delfile.delete();
                        }
                    }
                }

                //删除投票下面的详细投票
                voteContentMapper.delete(new PropertyWrapper<>(VoteContent.class).eq("voteid",id).getWrapper());

                //删除投票下面投票人
                voteUserMapper.delete(new PropertyWrapper<>(VoteUser.class).eq("voteid",id).getWrapper());
            }
        }
    }



    @Override
    public void votethemedeletes(String ids){

        String path = configModelService.getUploadPath(ConfigModel.dir_pic);

        if(!StringUtils.isEmpty(ids)){

            String [] arr=ids.split(",");

            for (int i = 0; i < arr.length; i++) {
                String id=arr[i];

                //删除主题
                votethemeMapper.deleteById(Long.valueOf(id));

                List<Vote> votelist=voteMapper.selectList(new PropertyWrapper(Vote.class).eq("votethemeid",id).getWrapper());

                String voteids="";

                if(votelist!=null&&votelist.size()>0){
                    for (Vote v: votelist) {
                        voteids+=","+v.getId();
                    }
                }

                if(!StringUtils.isEmpty(voteids)){
                    this.votedeletes(voteids.substring(1));
                }
            }
        }
    }


}
