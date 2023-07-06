package com.jnetdata.msp.resources.audio.vo;

import com.jnetdata.msp.resources.audio.model.Audio;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

@Data
public class AudioVo {
    private PageVo1 pager;

    private Audio audio;
}
