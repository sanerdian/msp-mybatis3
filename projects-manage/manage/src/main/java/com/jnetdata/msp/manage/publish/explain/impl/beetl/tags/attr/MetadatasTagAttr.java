package com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr;

import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.base.TagBaseAttr;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by xujian on 2017/10/20.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MetadatasTagAttr extends TagBaseAttr {
    private String id;
    private Integer childindex;
    private Integer from;
    private Integer num;
    private int startpos = 0;
    private String order;
    private String where;
    private boolean allChnl;
    private String chnlIds;
    private boolean page;
    private Integer pageSize;
}
