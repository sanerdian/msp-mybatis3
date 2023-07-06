package com.jnetdata.msp.media.vo.commentManage;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentQueryParam {
    Long siteid;
    String doctitle;

    Integer commentstatus;
    List<Date> createTime;
    String id;
}
