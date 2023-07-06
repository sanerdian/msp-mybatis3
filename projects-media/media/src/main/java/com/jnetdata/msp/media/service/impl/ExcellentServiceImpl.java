package com.jnetdata.msp.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.manage.column.mapper.ProgramaMapper;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.media.mapper.ExcellentMapper;
import com.jnetdata.msp.media.mapper.JobApiMapper;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.service.ExcellentService;
import com.jnetdata.msp.media.service.UserImportService;
import com.jnetdata.msp.media.util.DateUtils;
import com.jnetdata.msp.media.util.ValueHelper;
import com.jnetdata.msp.media.util.publicMethodUtil;
import com.jnetdata.msp.media.vo.*;
import com.jnetdata.msp.member.limit.mapper.PermissionMapper;
import com.jnetdata.msp.member.limit.model.Permission;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.service.UserService;
import com.jnetdata.msp.tlujy.collect.mapper.CollectMapper;
import com.jnetdata.msp.tlujy.collect.model.Collect;
import com.jnetdata.msp.tlujy.follow.mapper.FollowMapper;
import com.jnetdata.msp.tlujy.follow.model.Follow;
import com.jnetdata.msp.tlujy.investigate.model.Investigate;
import com.jnetdata.msp.tlujy.investigate.service.InvestigateService;
import com.jnetdata.msp.tlujy.vote.mapper.VoteMapper;
import com.jnetdata.msp.tlujy.vote.model.Vote;
import com.jnetdata.msp.tlujy.vote.service.VoteService;
import com.jnetdata.msp.tlujy.vote_content.mapper.VoteContentMapper;
import com.jnetdata.msp.tlujy.vote_content.model.VoteContent;
import com.jnetdata.msp.tlujy.vote_content.service.VoteContentService;
import com.jnetdata.msp.tlujy.vote_user.mapper.VoteUserMapper;
import com.jnetdata.msp.tlujy.vote_user.model.VoteUser;
import com.jnetdata.msp.tlujy.vote_user.service.VoteUserService;
import com.jnetdata.msp.tlujy.votetheme.mapper.VotethemeMapper;
import com.jnetdata.msp.tlujy.votetheme.model.Votetheme;
import com.jnetdata.msp.tlujy.xinwen020.mapper.Xinwen020Mapper;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import com.jnetdata.msp.tlujy.xinwen_comment.mapper.XinwenCommentMapper;
import com.jnetdata.msp.tlujy.xinwen_comment.model.XinwenComment;
import com.jnetdata.msp.tlujy.xinwen_evaluate.mapper.XinwenEvaluateMapper;
import com.jnetdata.msp.tlujy.xinwen_evaluate.model.XinwenEvaluate;
import com.jnetdata.msp.tlujy.xinwen_record.mapper.XinwenRecordMapper;
import com.jnetdata.msp.tlujy.yjfk.mapper.YjfkMapper;
import com.jnetdata.msp.tlujy.yjfk.model.Yjfk;
import com.jnetdata.msp.tlujy.yjfk_hhb.mapper.YjfkHhbMapper;
import com.jnetdata.msp.tlujy.yjfk_hhb.model.YjfkHhb;
import com.jnetdata.msp.tlujy.yjfk_photo.mapper.YjfkPhotoMapper;
import com.jnetdata.msp.tlujy.yjfk_photo.model.YjfkPhoto;
import lombok.SneakyThrows;
import org.apache.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
public class ExcellentServiceImpl extends BaseServiceImpl<JobApiMapper, JobApi> implements ExcellentService {

    @Autowired
    private ExcellentMapper excellentMapper;
    @Autowired
    private XinwenEvaluateMapper xinwenEvaluateMapper;
    @Autowired
    private XinwenCommentMapper xinwenCommentMapper;
    @Autowired
    private Xinwen020Mapper xinwen020Mapper;
    @Autowired
    private UserService userService;
    @Autowired
    private VoteContentService voteContentService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private VoteUserMapper voteUserMapper;
    @Autowired
    private VoteContentMapper voteContentMapper;
    @Autowired
    private VoteMapper voteMapper;
    @Autowired
    private VoteUserService voteUserService;
    @Autowired
    private YjfkMapper yjfkMapper;
    @Autowired
    private YjfkHhbMapper yjfkHhbMapper;
    @Autowired
    private YjfkPhotoMapper yjfkPhotoMapper;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private VotethemeMapper votethemeMapper;
    @Autowired
    private ProgramaMapper programaMapper;
    @Autowired
    private InvestigateService investigateService;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private UserImportService userImportService;
    @Autowired
    private XinwenRecordMapper xinwenRecordMapper;
    @Autowired
    private FollowMapper followMapper;


    SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<Programa> getlmbySiteid(Long siteid,String lmorcd){

        PropertyWrapper pw=new PropertyWrapper<>(Programa.class);
        pw.eq("siteId",siteid).eq("status",0);
        if(StringUtils.isEmpty(lmorcd)){
            pw.isNull("lmorcd");
        }else{
            pw.eq("lmorcd",lmorcd);
        }
        pw.orderBy(Arrays.asList("chnlOrder"),true);

        return programaMapper.selectList(pw.getWrapper());
    }

    @Override
    public List<Programa> getlmbyparentid(Long parentId,String lmorcd){

        PropertyWrapper pw=new PropertyWrapper<>(Programa.class);
        pw.eq("parentId",parentId).eq("status",0);
        if(StringUtils.isEmpty(lmorcd)){
            pw.isNull("lmorcd");
        }else{
            pw.eq("lmorcd",lmorcd);
        }
        pw.orderBy(Arrays.asList("chnlOrder"),true);

        return programaMapper.selectList(pw.getWrapper());
    }


    @SneakyThrows
    @Override
    public Pager<Votetheme> votethemelist(Page<Votetheme> page, VoteThemeVo entity, HttpServletRequest request) {

        String userid = publicMethodUtil.getLoginWorker(request).getExternalid();

        PropertyWrapper wrapper = new PropertyWrapper(Votetheme.class);

        wrapper.eq("pubstatus",1L);

        List<Votetheme> votelist = votethemeMapper.selectPage((IPage<Votetheme>) page,wrapper.getWrapper().orderBy(true,true,"crtime")).getRecords();

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
            vo.setPhoto(votetheme.getPhoto());

            Page<Vote> p = new Page(0,1000);
            voteVo v = new voteVo();
            v.setVotethemeid(String.valueOf(votetheme.getId()));
            v.setLoginid(Long.valueOf(userid));

            Pager<voteVo> l = this.votelist(p,v);

            //没有能看的投票
            if(l.getRecords()==null||l.getRecords().size()==0){
                counts--;
                continue;
            }

            list.add(vo);
        }
        Pager pager= ValueHelper.changePage(page);
        pager.setRecords(list);
        pager.setTotal(counts);

        return pager;

    }

    @Override
    public Votetheme getvotetheme(String id) {
        return votethemeMapper.selectById(id);
    }

    @Override
    public Pager<xinwenVo> xinwenlist(Page<xinwenVo> page, xinwenVo entity,Lz_WorkerVo workerVo) {

        //获取前台登陆人员
        //Lz_WorkerVo workerVo = userImportService.getInfobyExternalid(String.valueOf(entity.getUserid()));

        workerVo.setDwbsm("!"+workerVo.getDwbsm()+"!");
        workerVo.setBmbm("!"+workerVo.getBmbm()+"!");

        if(workerVo.getSfzh()!=null&&workerVo.getSfzh().length()>=16){
            try {
                if(Integer.valueOf(workerVo.getSfzh().substring(14,15))%2==0){//女
                    workerVo.setSex("女");
                }else{
                    workerVo.setSex("男");
                }

                int year = Integer.valueOf(workerVo.getSfzh().substring(3,7));

                workerVo.setAge((new Date().getYear()+1900-year)+"");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        long min = (page.getCurrent()-1)*page.getSize();
        long max = page.getCurrent()*page.getSize();

        long startTime = System.currentTimeMillis();
        List<xinwenVo> list = excellentMapper.selectPageXinwen(min,max,entity,workerVo);
        int counts=excellentMapper.selectPageXinwenMaxCount(entity,workerVo);
        long endTime = System.currentTimeMillis();
        System.out.println("执行SQL: 花费{}ms"+(endTime - startTime));


        //查询新闻表
        Pager pager= ValueHelper.changePage(page);
        pager.setRecords(list);
        pager.setTotal(counts);
        return pager;
    }

    @Override
    public xinwenVo getxinwen(xinwenVo entity) {

        //新闻
        Xinwen020 xinwen020=xinwen020Mapper.selectById(entity.getId());

        xinwenVo vo=new xinwenVo();

        if(xinwen020!=null){
            vo.setId(xinwen020.getId());
            vo.setTitle(xinwen020.getTitle());

            //点赞数
            int evaluatecount = xinwenEvaluateMapper.selectCount(new PropertyWrapper(XinwenEvaluate.class).eq("idvalue",entity.getId()).eq("evaluateobject",1L).getWrapper());
            //是否点赞
            vo.setEvaluatecount((long)evaluatecount);

            //评论数
            int commentcount = xinwenCommentMapper.selectCount(new PropertyWrapper(XinwenComment.class).eq("docid",entity.getId()).getWrapper());
            vo.setCommentcount((long)commentcount);

            //自己点赞数
            int zjevaluatecount = xinwenEvaluateMapper.selectCount(new PropertyWrapper(XinwenEvaluate.class).eq("createBy",entity.getUserid())
                    .eq("idvalue",entity.getId()).eq("evaluateobject","1").getWrapper());
            //是否点赞
            vo.setEvaluatetype(zjevaluatecount>0?"1":"");

            //自己收藏数
            int collectcount = collectMapper.selectCount(new PropertyWrapper(Collect.class).eq("createBy",entity.getUserid())
                    .eq("docid",entity.getId()).eq("state","1").getWrapper());
            //是否收藏
            vo.setCollectbool(collectcount>0?true:false);
            vo.setCrtime(xinwen020.getCreateTime());
            vo.setZhengwen(xinwen020.getZhengwen());
            vo.setSummary(xinwen020.getSummary());
            vo.setHometitle(xinwen020.getHometitle());
            vo.setSubtitle(xinwen020.getSubtitle());
            vo.setFrozentype(xinwen020.getFrozentype());
            vo.setCommentallowed(xinwen020.getCommentallowed().intValue());
            vo.setCommentchecked(xinwen020.getCommentchecked().intValue());
            vo.setCdntype(xinwen020.getCdntype());
        }

        return vo;
    }

    @Override
    public Pager<CommentVo> commentlist(Page<XinwenComment> page, CommentVo entity) {

        PropertyWrapper wrapper = new PropertyWrapper(XinwenComment.class);

        if(!StringUtils.isEmpty(entity.getDocid())){
            wrapper.eq("docid",entity.getDocid());
        }

        if(!StringUtils.isEmpty(entity.getCommentsid())){
            wrapper.eq("commentsid",entity.getCommentsid());
        }

        if(!StringUtils.isEmpty(entity.getCommentstatus())){
            wrapper.eq("commentstatus",entity.getCommentstatus());
        }

        List<XinwenComment> commentlist = xinwenCommentMapper.selectPage((IPage<XinwenComment>) page,wrapper.getWrapper().orderBy(true,false,"crtime")).getRecords();
        int counts=xinwenCommentMapper.selectCount(wrapper.getWrapper());



        //查询评论表
        List<CommentVo> list=new ArrayList<>();

        for (int i = 0; i <commentlist.size(); i++) {
            XinwenComment comment=commentlist.get(i);

            User dfduser = userService.getById(comment.getDfdid());

            XinwenEvaluate xinwenEvaluate=new XinwenEvaluate();
            xinwenEvaluate.setEvaluateobject(2L);
            xinwenEvaluate.setEvaluatetype(1L);
            xinwenEvaluate.setIdvalue(comment.getId());

            int djcount = this.evaluateCount(xinwenEvaluate);

            //查询评论的评论，的点赞
            int brdzcount = xinwenEvaluateMapper.selectCount(new PropertyWrapper(XinwenEvaluate.class).eq("createBy",entity.getUserid())
                    .eq("idvalue",comment.getId())
                    .eq("evaluateobject","2").getWrapper());

            //查询评论下的评论数量
            int commentcount = xinwenCommentMapper.selectCount(new PropertyWrapper(XinwenComment.class)
                    .eq("commentsid",comment.getId()).getWrapper());

            CommentVo vo=new CommentVo();
            vo.setId(comment.getId());
            vo.setUsercomment(comment.getUsercomment());
            vo.setModifyTime(comment.getModifyTime());
            vo.setDjcount(djcount);
            vo.setCommentcount(commentcount);
            vo.setCrTrueName(comment.getCrUser());
            vo.setDfdid(comment.getDfdid());
            vo.setDfdTrueName(dfduser!=null?dfduser.getTrueName():"");
            vo.setCommentsid(comment.getCommentsid());
            vo.setEvaluatetype(brdzcount>0?"1":"0");
            vo.setCrUserheadUrl("");
            vo.setCreateBy(comment.getCreateBy());
            vo.setCommentstatus(comment.getCommentstatus());
            vo.setCreateTime3(comment.getCreateTime());
            vo.setAnonymousflag(comment.getAnonymousflag().intValue());
            list.add(vo);
        }
        Pager pager= ValueHelper.changePage(page);
        pager.setRecords(list);
        pager.setTotal(counts);

        return pager;
    }


    @Override
    public List<Investigate> Investigatelist(String userid) {

        List<Investigate> investigateList = excellentMapper.selectInvestigateList(userid);

        List<Investigate> list = new ArrayList<>();

        for (int i = 0; i < investigateList.size(); i++) {

            Investigate investigate =  investigateList.get(i);

            //如果人员列表不包含再判断人员属性
            if(investigate.getPushtouser()!=null&&!investigate.getPushtouser().contains(String.valueOf(userid))) {
                //根据人员属性不为空
                if (!StringUtils.isEmpty(investigate.getPushsql())) {
                    int count = excellentMapper.checkLz_Worker(String.valueOf(userid), investigate.getPushsql());
                    //大于0证明满足属性
                    if (count > 0) {
                        investigate.setPushsql("");
                        investigate.setPushtouser("");
                        list.add(investigate);
                    }
                } else {
                    investigate.setPushsql("");
                    investigate.setPushtouser("");
                    list.add(investigate);
                }
            }else{
                investigate.setPushsql("");
                investigate.setPushtouser("");
                list.add(investigate);
            }
        }

        return list;
    }

    @Override
    public Pager<voteVo> votelist(Page<Vote> page, voteVo entity) {

        long min = (page.getCurrent()-1)*page.getSize();
        long max = page.getCurrent()*page.getSize();

        List<Vote> votelist = excellentMapper.selectPageVote(min,max,entity);
        int counts=excellentMapper.selectPageVoteMaxCount(entity);
        List<voteVo> list=new ArrayList<>();

        //查询投票表
        for (int i = 0; i <votelist.size(); i++) {
            Vote vote=votelist.get(i);

            voteVo vo=new voteVo();
            vo.setId(vote.getId());
            vo.setTitle(vote.getTitle());
            vo.setStartdate(vote.getStartdate());
            vo.setEnddate(vote.getEnddate());
            vo.setLimittype(vote.getLimittype());
            vo.setLimitcount(vote.getLimitcount());
            vo.setVotethemeid(vote.getVotethemeid());

            int uservoteCount=0;

            PropertyWrapper p=new PropertyWrapper<>(VoteUser.class).eq("voteid",vote.getId()).eq("createBy",entity.getLoginid());

            uservoteCount=voteUserService.count(p);

            vo.setLimitsycount(vote.getLimitcount()-uservoteCount);

            voteVo voteVo3=new voteVo();
            voteVo3.setVoteid(vote.getId());
            voteVo3.setId(entity.getId());
            voteVo3.setLoginid(entity.getLoginid());
            vo.setVoteConetentLPager(this.voteContentlist(new Page(1,100000000),voteVo3));

            //如果人员列表不包含再判断人员属性
            if(vote.getPushtouser()!=null&&!vote.getPushtouser().contains(String.valueOf(entity.getLoginid()))) {
                //根据人员属性不为空
                if (!StringUtils.isEmpty(vote.getPushsql())) {
                    int count = excellentMapper.checkLz_Worker(String.valueOf(entity.getLoginid()), vote.getPushsql());
                    //大于0证明满足属性
                    if (count > 0) {
                        list.add(vo);
                    } else {//否则减少最大值
                        counts--;
                    }
                } else {
                    list.add(vo);
                }
            }else{
                list.add(vo);
            }
        }
        Pager pager= ValueHelper.changePage(page);
        pager.setRecords(list);
        pager.setTotal(counts);

        return pager;
    }

    @Override
    public voteVo getVote(voteVo entity) {

        voteVo vo=new voteVo();

        //投票信息
        Vote vote=voteMapper.selectById(entity.getId());

        vo.setId(vote.getId());
        vo.setTitle(vote.getTitle());
        vo.setStartdate(vote.getStartdate());
        vo.setEnddate(vote.getEnddate());
        vo.setLimittype(vote.getLimittype());
        vo.setLimitcount(vote.getLimitcount());

        int uservoteCount=0;

        PropertyWrapper p=new PropertyWrapper<>(VoteUser.class).eq("voteid",vote.getId()).eq("createBy",entity.getLoginid());

        //限制类型,0只能，1每天，2每周，3每月
        if(vote.getLimittype()==1){
            p.between("crtime",DateUtils.getDayBegin(),DateUtils.getDayEnd());
        }else if(vote.getLimittype()==2){
            p.between("crtime",DateUtils.getBeginDayOfWeek(),DateUtils.getEndDayOfWeek());
        }else if(vote.getLimittype()==3){
            p.between("crtime",DateUtils.getBeginDayOfMonth(),DateUtils.getEndDayOfMonth());
        }

        uservoteCount=voteUserService.count(p);

        vo.setLimitsycount(vote.getLimitcount()-uservoteCount);

        return vo;
    }


    @Override
    public Pager<voteVo> voteContentlist(Page<Vote> page, voteVo entity) throws ParseException {

        PropertyWrapper wrapper = new PropertyWrapper(Vote.class);

        wrapper.eq("voteid",entity.getVoteid());

        List<VoteContent> votelist = voteContentMapper.selectPage((IPage) ValueHelper.changePage(page),wrapper.getWrapper().orderBy(true,false,"crtime")).getRecords();
        int counts=voteContentMapper.selectCount(wrapper.getWrapper());

        List<voteVo> list=new ArrayList<>();

        Vote v=voteMapper.selectById(entity.getVoteid());

        //查询新闻表
        for (int i = 0; i <votelist.size(); i++) {
            VoteContent vote=votelist.get(i);

            //投票标题的总投递数量
            int count=voteUserService.count(new PropertyWrapper<>(VoteUser.class).eq("voteid",vote.getVoteid()));

            //当前投票内容的投递数量
            int voteCount=voteUserService.count(new PropertyWrapper<>(VoteUser.class).eq("votecontentid",vote.getId()).eq("createBy",entity.getLoginid()));

            int uservoteCount=0;

            PropertyWrapper p=new PropertyWrapper<>(VoteUser.class).eq("votecontentid",vote.getId()).eq("createBy",entity.getLoginid());

            //限制类型,0只能，1每天，2每周，3每月
            if(v.getLimittype()==1){
                p.between("crtime",DateUtils.getDayBegin(),DateUtils.getDayEnd());
            }else if(v.getLimittype()==2){
                p.between("crtime",DateUtils.getBeginDayOfWeek(),DateUtils.getEndDayOfWeek());
            }else if(v.getLimittype()==3){
                p.between("crtime",DateUtils.getBeginDayOfMonth(),DateUtils.getEndDayOfMonth());
            }

            uservoteCount=voteUserService.count(p);

            voteVo votevo=new voteVo();
            votevo.setId(vote.getId());
            votevo.setTitle(vote.getTitle());
            votevo.setCreateTime(vote.getCreateTime());
            votevo.setCruser(vote.getCrUser());
            votevo.setRemarks(vote.getRemarks());
            votevo.setVoteCounts(voteCount);
            votevo.setPhoto(vote.getPhoto());

            if(count==0||voteCount==0){
                votevo.setVoteBfb("0%");
            }else{
                votevo.setVoteBfb(Math.round(((double)voteCount/(double) count)*100)+"%");
            }

            votevo.setVotetype(uservoteCount>0?"1":"0");
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

        if(entity.getVotecontentid()!=null){
            wrapper.eq("votecontentid",entity.getVotecontentid());
        }

        if(entity.getVotethemeid()!=null){
            wrapper.where("voteid in(select j.id from jmeta_vote j where j.votethemeid=(select c.id from jmeta_votetheme c where c.id='"+entity.getVotethemeid()+"'))");
        }

        List<VoteUser> votelist = voteUserMapper.selectPage((IPage<VoteUser>) page,wrapper.getWrapper().orderBy(true,false,"crtime")).getRecords();
        int counts=voteUserMapper.selectCount(wrapper.getWrapper());

        List<VoteUser> newlist = votelist.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(comparingLong(VoteUser::getCreateBy))), ArrayList::new)
        );

        List<voteUserVo> list=new ArrayList<>();

        //查询新闻表

        for (int i = 0; i <newlist.size(); i++) {
            VoteUser vote=newlist.get(i);

            voteUserVo votevo=new voteUserVo();
            votevo.setId(vote.getId());
            votevo.setPhoto("");
            votevo.setCrUser(vote.getCrUser());
            list.add(votevo);
        }
        Pager pager= ValueHelper.changePage(page);
        pager.setRecords(list);
        pager.setTotal(counts);

        return pager;
    }


    @Override
    public int evaluateCount(XinwenEvaluate entity){

        QueryWrapper<XinwenEvaluate> evaluate= new PropertyWrapper<>(XinwenEvaluate.class).getWrapper();

        evaluate.setEntity(entity);

        return xinwenEvaluateMapper.selectCount(evaluate);
    }

    @Override
    public int commentCount(XinwenComment entity){

        QueryWrapper<XinwenComment> evaluate= new PropertyWrapper<>(XinwenComment.class).getWrapper();

        evaluate.setEntity(entity);

        return xinwenCommentMapper.selectCount(evaluate);
    }

    @Override
    public int yjfkAdd(String uuid,Yjfk yjfk){

        //添加意见反馈信息
        int res=yjfkMapper.insert(yjfk);

        YjfkPhoto yjfkPhoto=new YjfkPhoto();
        yjfkPhoto.setYjfkid(String.valueOf(yjfk.getId()));

        //将上传的图片yjfkId进行修改
        yjfkPhotoMapper.update(yjfkPhoto,new PropertyWrapper<>(YjfkPhoto.class).eq("yjfkid",uuid).getWrapper());

        //填写意见反馈，默认增加一条意见反馈会话
        YjfkHhb yjfkHhb=new YjfkHhb();
        yjfkHhb.setYjfkid(String.valueOf(yjfk.getId()));
        yjfkHhb.setDfnr(yjfk.getYijianneirong());
        yjfkHhb.setState(0L);
        yjfkHhb.setCreateBy(yjfk.getCreateBy());
        yjfkHhb.setCrUser(yjfk.getCrUser());
        yjfkHhb.setCreateTime(yjfk.getCreateTime());

        yjfkHhbMapper.insert(yjfkHhb);

        return res;
    }

    @Override
    public boolean voteUserAdd(voteUserVo entity){

        //获取投票内容信息
        VoteContent voteContent=voteContentService.getById(entity.getVotecontentid());
        //获取投票信息
        Vote vote=voteService.getById(voteContent.getVoteid());

        long limitcount=vote.getLimitcount();//限制数量

        PropertyWrapper pw=new PropertyWrapper<>(VoteUser.class).eq("voteid",vote.getId()).eq("createBy",entity.getCreateBy());
        PropertyWrapper pw2=new PropertyWrapper<>(VoteUser.class).eq("votecontentid",entity.getVotecontentid()).eq("createBy",entity.getCreateBy());
        //根据类型不同查询条件也不同

        int count=voteUserMapper.selectCount(pw.getWrapper());
        int count2=voteUserMapper.selectCount(pw2.getWrapper());

        if(count>=limitcount||count2>=1){
            return false;
        }

        VoteUser voteUser = new VoteUser();
        voteUser.setVotecontentid(entity.getVotecontentid()+"");
        voteUser.setVoteid(vote.getId()+"");
        voteUser.setCreateTime(new Date());
        voteUser.setCreateBy(entity.getCreateBy());
        voteUser.setCrUser(entity.getCrUser());

        //新增
        voteUserMapper.insert(voteUser);

        return true;
    }

    @Override
    public Pager<Yjfk> yjfkhistory(Page<Yjfk> page,yjfkVo entity){

        PropertyWrapper wrapper = new PropertyWrapper(Vote.class);

        wrapper.eq("userid",entity.getUserid());

        List<Yjfk> yjfklist = yjfkMapper.selectPage((IPage<Yjfk>) page,wrapper.getWrapper().orderBy(true,false,"crtime")).getRecords();
        int counts=yjfkMapper.selectCount(wrapper.getWrapper());

        List<yjfkVo> list=new ArrayList<>();

        //查询意见反馈表

        for (int i = 0; i <yjfklist.size(); i++) {
            Yjfk yjfk=yjfklist.get(i);
            yjfkVo vo=new yjfkVo();

            int jlcount=yjfkHhbMapper.selectCount(new PropertyWrapper(YjfkHhb.class).eq("yjfkid",yjfk.getId()).eq("state","1").getWrapper());

            //图片集合
            List<YjfkPhoto> photolist=yjfkPhotoMapper.selectList(new PropertyWrapper(YjfkPhoto.class).eq("yjfkid",yjfk.getId()).getWrapper());

            vo.setId(yjfk.getId());
            vo.setYijianneirong(yjfk.getYijianneirong());
            vo.setYjphone(yjfk.getYjphone());
            vo.setCreateTime(yjfk.getCreateTime());
            vo.setReplydate(yjfk.getReplydate());
            vo.setModifyTime(yjfk.getModifyTime());
            vo.setTimingdate(yjfk.getTimingdate());
            vo.setState(yjfk.getState());
            vo.setJlcount(jlcount);

            vo.setPhoto((photolist!=null&&photolist.size()>0)?photolist.get(0).getPhoto():"");

            list.add(vo);
        }
        Pager pager= ValueHelper.changePage(page);
        pager.setRecords(list);
        pager.setTotal(counts);

        return pager;
    }

    @Override
    public int deleteComment(CommentVo commentvo){

        PropertyWrapper propertyWrapper=new PropertyWrapper(XinwenComment.class);

        if(commentvo.getDocid()!=null){
            propertyWrapper.eq("docid",commentvo.getDocid());
        }

        if(commentvo.getCommentsid()!=null){
            propertyWrapper.eq("commentsid",commentvo.getCommentsid());
        }

        if(commentvo.getUserid()!=null&&commentvo.getUserid()!=0L){
            propertyWrapper.eq("createBy",commentvo.getUserid());
        }

        int count=xinwenCommentMapper.delete(propertyWrapper.getWrapper());

        return count;
    }

    @Override
    public int deleteEvaluate(CommentVo commentvo){

        PropertyWrapper propertyWrapper=new PropertyWrapper(XinwenEvaluate.class);

        if(commentvo.getDocid()!=null){
            propertyWrapper.eq("idvalue",commentvo.getDocid());
        }

        if(commentvo.getCommentsid()!=null){
            propertyWrapper.eq("idvalue",commentvo.getCommentsid());
        }

        if(commentvo.getUserid()!=null&&commentvo.getUserid()!=0L){
            propertyWrapper.eq("createBy",commentvo.getUserid());
        }

        int count=xinwenEvaluateMapper.delete(propertyWrapper.getWrapper());

        return count;
    }

    @Override
    public int deleteCollect(CommentVo commentvo){

        PropertyWrapper propertyWrapper=new PropertyWrapper(Collect.class);

        if(commentvo.getDocid()!=null){
            propertyWrapper.eq("docid",commentvo.getDocid());
        }

        if(commentvo.getUserid()!=null&&commentvo.getUserid()!=0L){
            propertyWrapper.eq("createBy",commentvo.getUserid());
        }

        int count=collectMapper.delete(propertyWrapper.getWrapper());

        return count;
    }

    @Override
    public List<Permission> listPermission(Permission permission){

        PropertyWrapper pw = new PropertyWrapper(Permission.class);

        if (permission.getOwnerId()!=null) {
            pw.eq("ownerId",permission.getOwnerId());
        }

        if (permission.getPermission()!=null) {
            pw.eq("permission",permission.getPermission());
        }

        List<Permission> list = permissionMapper.selectList(pw.getWrapper());
        return list;
    }

    @Override
    public int listPermissionCount(Permission permission){

        PropertyWrapper pw = new PropertyWrapper(Permission.class);

        if (permission.getOwnerId()!=null) {
            pw.eq("ownerId",permission.getOwnerId());
        }

        if (permission.getPermission()!=null) {
            pw.eq("permission",permission.getPermission());
        }

        return permissionMapper.selectCount(pw.getWrapper());
    }

    //提交投票
    @Override
    public void votetj(List<Long> list,String userid,String xm){

        if(list!=null){

            for (int i = 0; i < list.size(); i++) {
                Long votecontentid = list.get(i);

                //投票内容
                VoteContent voteContent = voteContentService.getById(votecontentid);

                //投票
                Vote vote = voteMapper.selectById(voteContent.getVoteid());

                PropertyWrapper pw=new PropertyWrapper<>(VoteUser.class).eq("voteid",vote.getId()).eq("createBy",userid);
                PropertyWrapper pw2=new PropertyWrapper<>(VoteUser.class).eq("votecontentid",votecontentid).eq("createBy",userid);
                //根据类型不同查询条件也不同

                int count=voteUserMapper.selectCount(pw.getWrapper());
                int count2=voteUserMapper.selectCount(pw2.getWrapper());

                if(count>=vote.getLimitcount()||count2>=1){
                    continue;
                }


                VoteUser voteUser = new VoteUser();
                voteUser.setVotecontentid(votecontentid+"");
                voteUser.setVoteid(vote.getId()+"");
                voteUser.setCreateTime(new Date());
                voteUser.setCreateBy(Long.valueOf(userid));
                voteUser.setCrUser(xm);

                //新增
                voteUserMapper.insert(voteUser);


            }


        }

    }

    @Override
    public TreeVo userZd(String dwbsm) {

        List<TreeVo> list = excellentMapper.userZd(dwbsm);

        return list!=null&&list.size()>0?list.get(0):new TreeVo();
    }

    @Override
    public List<String> bmParentList(String dwbsm) {
        return excellentMapper.bmParentList(dwbsm);
    }



    @Override
    public void updateFollowOrder(Follow record,HttpServletRequest request) {

        //用户id
        String userid = publicMethodUtil.getLoginWorker(request).getExternalid();

        //之前的关注
        Follow xr = followMapper.selectById(record.getId());

        //如果为空放到最后
        if(xr.getOrdernum()==null){
            int count = followMapper.selectCount(new PropertyWrapper<>(Follow.class).eq("ptuserid",userid).getWrapper());

            if(count==0){
                xr.setOrdernum(1L);
                followMapper.updateById(xr);

            }else{
                xr.setOrdernum((long)count+1);
                followMapper.updateById(xr);
            }
        }/*else if(record.getOrdernum()>xr.getOrdernum()){//往小变，中间加一
            excellentMapper.updateXiwenfollowMin(userid,(int)xr.getOrdernum().longValue(),(int)record.getOrdernum().longValue());
            followMapper.updateById(record);
        }else if(xr.getOrdernum()>record.getOrdernum()){//往大变，中间减一
            excellentMapper.updateXiwenfollowMax(userid,(int)record.getOrdernum().longValue(),(int)xr.getOrdernum().longValue());
            followMapper.updateById(record);
        }*/
    }


    @Override
    public void updateFollowOrderArr(Follow[] record,HttpServletRequest request) {
        if(record!=null){
            for (int i = 0; i < record.length; i++) {
                Follow follow = record[i];
                followMapper.updateById(follow);
            }
        }
    }
}
