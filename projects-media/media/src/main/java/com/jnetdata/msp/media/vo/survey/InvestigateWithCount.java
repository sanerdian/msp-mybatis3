package com.jnetdata.msp.media.vo.survey;

import com.jnetdata.msp.tlujy.investigate.model.Investigate;
import lombok.Data;

@Data
public class InvestigateWithCount extends Investigate {
    String countPush;//推送数量
    String countHs;//回收数量
}
