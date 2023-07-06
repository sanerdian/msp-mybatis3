package com.jnetdata.msp.media.service;

import com.jnetdata.msp.media.vo.XinwenDetailVo;
import com.jnetdata.msp.media.vo.XinwenSummaryVo;
import com.jnetdata.msp.media.vo.commentManage.CommentQueryVo;
import com.jnetdata.msp.tlujy.xinwen_comment.model.XinwenComment;
import org.thenicesys.data.api.Pager;

import java.util.List;

public interface CustomizedService {
    /*查询轮播图*/
    List<XinwenSummaryVo> xinwenLunbotu(Long siteid);
    /*按栏目查询新闻*/
    List<XinwenSummaryVo> xinwenByColumn(Long columnid);
    /*查询新闻详情，包括评论、评价*/
    XinwenDetailVo xinwenById(Long docid);
    /*添加新闻的评价，点赞/踩*/
    Long addXinwenEvaluate(Long docid, Integer type);
    /**删除新闻的评价，只能删用户自己对指定新闻的评价（点赞/踩）！
     * @return*/
    int deleteXinwenEvaluate(Long docid);
    /*添加新闻的评论*/
    Long addXinwenComment(XinwenComment xinwenComment);
    /*删除指定的评论，同时删除该评论对应的评价（点赞/踩）*/
    int deleteXinwenComment(Long commentid);
    /*添加评论的评价（对该评论进行点赞/踩）*/
    Long addCommentEvaluate(Long commentid, Integer type);
    /*删除指定评论的评价，只能删除自己的评价（点赞/踩）*/
    int deleteCommentEvaluate(Long commentid);

    /**
     * 获取当前登录用户的推送新闻列表
     * @return
     */
    List<XinwenSummaryVo> getPushXinwen(Long userid);

    Pager<XinwenComment> pageXinwenComment(CommentQueryVo vo);
}
