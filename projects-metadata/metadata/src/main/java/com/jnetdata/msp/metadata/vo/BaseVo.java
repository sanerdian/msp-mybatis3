package com.jnetdata.msp.metadata.vo;

import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

@Data
public class BaseVo<T> {

    private PageVo1 pager;
    private T entity;
}
