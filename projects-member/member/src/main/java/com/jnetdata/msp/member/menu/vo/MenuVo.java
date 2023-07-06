package com.jnetdata.msp.member.menu.vo;

import com.jnetdata.msp.member.menu.model.Menu;
import lombok.Data;
import org.thenicesys.web.vo.PageVo;

/**
 * Created by TF on 2019/3/19.
 */
@Data
public class MenuVo {

    private PageVo pager;

    private Menu entity;

    //菜单上升下降状态（true为上升，false为下降）
    private boolean status;


}
