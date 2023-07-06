package com.jnetdata.msp.media.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.jnetdata.msp.tlujy.vote.model.Vote;
import com.jnetdata.msp.tlujy.vote_content.model.VoteContent;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class VoteFromVo extends Vote{

    private List<VoteContent> contentList;

}
