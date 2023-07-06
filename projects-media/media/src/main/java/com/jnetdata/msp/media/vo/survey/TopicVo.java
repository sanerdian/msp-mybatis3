package com.jnetdata.msp.media.vo.survey;

import com.jnetdata.msp.tlujy.answer.model.Answer;
import com.jnetdata.msp.tlujy.topic.model.Topic;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TopicVo extends Topic {
    List userAnswer= new ArrayList();//当前用户的答题
    List<Answer> answers = new ArrayList<>();
}
