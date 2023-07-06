package com.jnetdata.msp.media.vo;

import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class XinwenDetailVo extends Xinwen020 {
    int isEvaluated;//当前用户是否已评价过（1已点赞或踩，0未点赞或踩）
    Integer agreeNum;//点赞的数量
    Integer disagreeNum;////踩的数量
    List<XinwenCommentVo> commentvos= new ArrayList<>();//评论列表(审核通过的！或自己发表的评论！)
}
