package com.jnetdata.msp.media.vo.survey;

import com.jnetdata.msp.tlujy.investigate.model.Investigate;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class InvestigateVo extends Investigate {
    List<TopicVo> topics= new ArrayList<>();

    Integer isCommit;//0未提交1已提交
}
