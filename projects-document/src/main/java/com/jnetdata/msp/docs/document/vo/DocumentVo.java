package com.jnetdata.msp.docs.document.vo;

import com.jnetdata.msp.docs.document.model.Document;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

/**
 * @author Administrator
 */
@Data
public class DocumentVo {

    private PageVo1 pager;

    private Document entity;

}
