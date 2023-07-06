package com.jnetdata.msp.resources.picture.vo;

import com.jnetdata.msp.resources.picture.model.Picture;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

@Data
public class PictureVo {
    private PageVo1 pager;

    private Picture picture;
}
