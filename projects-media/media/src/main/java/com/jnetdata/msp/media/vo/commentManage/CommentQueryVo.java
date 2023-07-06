package com.jnetdata.msp.media.vo.commentManage;

import lombok.Data;
import org.thenicesys.web.vo.PageVo1;
@Data
public class CommentQueryVo {
    private PageVo1 pager;
    private CommentQueryParam entity;
}
