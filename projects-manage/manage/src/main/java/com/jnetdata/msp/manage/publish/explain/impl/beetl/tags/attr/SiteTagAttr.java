package com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr;

import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.base.TagBaseAttr;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by xujian on 2017/10/20.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SiteTagAttr extends TagBaseAttr {
    private String target = "_blank";
    private boolean autolink = false;
    private int length;
    private String id = "owner";
    private String field = "name";
    private String dateformat = "yyyy-MM-dd HH:mm:ss";
    private String truncatedflag = "...";
    private boolean urlisabs = false;
}
