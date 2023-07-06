package com.jnetdata.msp.vo;

import com.jnetdata.msp.core.model.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.thenicesys.web.vo.PageVo;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class BaseEntityVo<Page extends PageVo, T extends BaseEntity> {

    private Page pager;

    private T entity;

}
