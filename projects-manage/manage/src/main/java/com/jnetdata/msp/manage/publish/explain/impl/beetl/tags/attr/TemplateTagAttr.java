package com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr;

import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.base.TagBaseAttr;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by xujian on 2017/10/20.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TemplateTagAttr extends TagBaseAttr {
    private String tempname;
}
