package com.jnetdata.msp.resources.video.vo;

import com.jnetdata.msp.resources.video.model.Video;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

@Data
public class VideoVo {
    private PageVo1 pager;

    private Video video;
}
