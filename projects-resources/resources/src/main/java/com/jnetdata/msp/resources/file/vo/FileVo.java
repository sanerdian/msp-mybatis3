package com.jnetdata.msp.resources.file.vo;

import com.jnetdata.msp.resources.file.model.Files;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

@Data
public class FileVo {
    private PageVo1 pager;

    private Files files;
}
