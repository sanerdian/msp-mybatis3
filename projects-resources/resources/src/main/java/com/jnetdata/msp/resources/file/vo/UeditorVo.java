package com.jnetdata.msp.resources.file.vo;

import com.jnetdata.msp.resources.file.model.Files;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

@Data
public class UeditorVo {
    private String size;
    private String source;
    private String state;
    private String title;
    private String url;
}
