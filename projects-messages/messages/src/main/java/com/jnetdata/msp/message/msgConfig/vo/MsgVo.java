package com.jnetdata.msp.message.msgConfig.vo;

import com.jnetdata.msp.message.msgConfig.model.Msg;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by Admin on 2019/3/11.
 */
@Data
public class MsgVo {
    private PageVo1 pager;

    private Msg entity;
}
