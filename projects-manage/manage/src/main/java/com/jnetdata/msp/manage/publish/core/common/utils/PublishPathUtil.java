package com.jnetdata.msp.manage.publish.core.common.utils;

import com.jnetdata.msp.column.model.Channel;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishContextCache;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/8/20 14:42
 */
public class PublishPathUtil {

    /**
     * 提供方法便于后续扩展
     * 当前有未用到参数,后续可能由用
     *
     * @return
     */
    public String readChannelPath(PublishContextCache publishContext, Channel columnUtil) {
        return String.join(columnUtil.getChnlDataPath());
    }

    /**
     * 提供方法便于后续扩展
     * 当前有未用到参数,后续可能由用
     *
     * @return
     */
    public String readWebsitePath(PublishContextCache publishContext, Site site) {
        return site.getDataPath();

    }
}
