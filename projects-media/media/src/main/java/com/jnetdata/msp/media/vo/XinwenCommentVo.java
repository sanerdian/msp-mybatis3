package com.jnetdata.msp.media.vo;

import com.jnetdata.msp.tlujy.xinwen_comment.model.XinwenComment;
import lombok.Data;
@Data
public class XinwenCommentVo extends XinwenComment {
    int isEvaluated;//当前用户是否已评价过（1已点赞或踩，0未点赞或踩）
    Integer agreeNum;//点赞的数量
    Integer disagreeNum;////踩的数量
}
