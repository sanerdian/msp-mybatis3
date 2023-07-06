package com.jnetdata.msp.media.vo;

import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import lombok.Data;

@Data
public class XinwenSummaryVo extends Xinwen020 {
    Integer isRead;//新闻阅读状态：0未读，1已读
}
