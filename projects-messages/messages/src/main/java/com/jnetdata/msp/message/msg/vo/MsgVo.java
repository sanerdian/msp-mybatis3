package com.jnetdata.msp.message.msg.vo;

import com.jnetdata.msp.message.msg.model.Msg;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by TF on 2019/3/14.
 */
@Data
public class MsgVo {

    private PageVo1 pager;

    private Msg entity;

    private String ids;

    private int status;
}
