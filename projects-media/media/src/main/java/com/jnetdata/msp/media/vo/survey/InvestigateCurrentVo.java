package com.jnetdata.msp.media.vo.survey;

import com.alibaba.fastjson.annotation.JSONField;
import com.jnetdata.msp.tlujy.investigate.model.Investigate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class InvestigateCurrentVo extends Investigate {
    Integer answered;//已答题目数
    Integer total;//题目总数
    Integer isCommit;//0未提交1已提交
    Long secondRest;//答题剩余时间（秒）
//    Integer answerStatus;//题目是否已答完（0未答完，1已答完）
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date dtLastAnswer;
}
