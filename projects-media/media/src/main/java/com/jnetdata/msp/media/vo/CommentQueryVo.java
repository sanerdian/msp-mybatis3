package com.jnetdata.msp.media.vo;

import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by 19912 on 2020/8/15.
 */
@Data
public class CommentQueryVo {

    private PageVo1 pager;

    private CommentVo entity;
}
