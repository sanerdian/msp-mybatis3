package com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr;

import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.base.TagBaseAttr;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by xujian on 2017/10/20.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DocumentTagAttr extends TagBaseAttr {

    private boolean urlisabs = false;
    private boolean autolink = false;
    private String field = "doctitle";
    private String dateformat = "yyyy-MM-dd HH:mm:ss";
    private String target = "_blank";
    private String truncatedflag = "...";
    private String linkalt;
    private String autoformattype;
    private String classname;
    private String extra;
    private int length;
}
