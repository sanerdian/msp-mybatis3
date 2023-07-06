package com.jnetdata.msp.vo;

import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

import java.util.List;


@Data
public class BaseVo<T> {

    private PageVo1 pager;
    private T entity;
}
