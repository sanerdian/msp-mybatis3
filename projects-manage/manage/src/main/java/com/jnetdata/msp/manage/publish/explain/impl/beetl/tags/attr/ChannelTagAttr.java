package com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr;

import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.base.TagBaseAttr;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by xujian on 2017/10/20.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChannelTagAttr extends TagBaseAttr {
    private String id;
    private String target = "_blank";
    private boolean autolink = false;
    private String field = "name";
    private String dateformat = "yyyy-MM-dd HH:mm:ss";
    private int length;
    private boolean urlisabs = false;
    private Integer childindex;
    private String classname;
    private String extra;
    private String autoformattype;
    private String truncatedflag = "...";
}
