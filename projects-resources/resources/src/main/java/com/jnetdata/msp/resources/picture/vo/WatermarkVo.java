package com.jnetdata.msp.resources.picture.vo;

import com.jnetdata.msp.resources.picture.model.Watermark;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by Administrator on 2019/10/9.
 */
@Data
public class WatermarkVo{
    private PageVo1 pager;
    private Watermark entity;
}
