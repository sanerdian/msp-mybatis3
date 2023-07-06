package com.jnetdata.msp.media.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.vo.*;
import com.jnetdata.msp.member.limit.model.Permission;
import com.jnetdata.msp.tlujy.follow.model.Follow;
import com.jnetdata.msp.tlujy.investigate.model.Investigate;
import com.jnetdata.msp.tlujy.vote.model.Vote;
import com.jnetdata.msp.tlujy.vote_user.model.VoteUser;
import com.jnetdata.msp.tlujy.votetheme.model.Votetheme;
import com.jnetdata.msp.tlujy.xinwen_comment.model.XinwenComment;
import com.jnetdata.msp.tlujy.xinwen_evaluate.model.XinwenEvaluate;
import com.jnetdata.msp.tlujy.yjfk.model.Yjfk;
import org.apache.http.ParseException;
import org.thenicesys.data.api.Pager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 19912 on 2020/8/15.
 */
public interface ExcellentService extends BaseService<JobApi> {

    //app获取新闻列表
    Pager<xinwenVo> xinwenlist(Page<xinwenVo> page, xinwenVo entity,Lz_WorkerVo lz_workerVo);

    Pager<Votetheme> votethemelist(Page<Votetheme> page, VoteThemeVo entity, HttpServletRequest request);

    Votetheme getvotetheme(String id);

    List<Programa> getlmbySiteid(Long siteid,String lmorcd);

    List<Programa> getlmbyparentid(Long parentId,String lmorcd);

    //app获取新闻详情
    xinwenVo getxinwen(xinwenVo vo);

    //app获取评论列表
    Pager<CommentVo> commentlist(Page<XinwenComment> page, CommentVo entity);

    //获取投票列表
    Pager<voteVo> votelist(Page<Vote> page, voteVo entity);

    //获取投票列表
    List<Investigate> Investigatelist(String userid);

    //获取投票详情
    voteVo getVote(voteVo entity);

    //app投票内容列表
    Pager<voteVo> voteContentlist(Page<Vote> page, voteVo entity) throws ParseException;

    //app获取谁给投票详情投过票
    Pager<voteUserVo> voteUserlist(Page<VoteUser> page, voteUserVo entity);

    //获取点赞数量
    int evaluateCount(XinwenEvaluate entity);

    //获取评论数量
    int commentCount(XinwenComment entity);

    int yjfkAdd(String uuid,Yjfk yjfk);

    //添加投票
    boolean voteUserAdd(voteUserVo entity);

    //意见反馈历史记录
    Pager<Yjfk> yjfkhistory(Page<Yjfk> page,yjfkVo entity);

    //根据新闻id/评论id删除评论
    int deleteComment(CommentVo commentVo);

    //根据新闻id/评论id删除点赞
    int deleteEvaluate(CommentVo commentVo);

    //根据新闻id删除收藏
    int deleteCollect(CommentVo commentvo);

    List<Permission> listPermission(Permission permission);

    int listPermissionCount(Permission permission);

    void votetj(List<Long> list,String userid,String xm);

    TreeVo userZd(String dwbsm);

    List<String> bmParentList (String dwbsm);

    void updateFollowOrder(Follow record, HttpServletRequest request);

    void updateFollowOrderArr(Follow[] record, HttpServletRequest request);

}
