package com.jnetdata.msp.manage.publish.explain.impl.beetl.tags;

import com.jnetdata.msp.column.model.Channel;
import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.core.model.PublishObj;
import com.jnetdata.msp.manage.publish.core.common.utils.PublishUtil;
import com.jnetdata.msp.manage.publish.core.publish.PublishContentManager;
import com.jnetdata.msp.manage.publish.explain.common.constant.TagsConstant;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.annoation.BeetlTagName;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.core.GeneralTag;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.ChannelsTagAttr;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Service
@Scope("prototype")
@BeetlTagName(TagsConstant.JNET_CHANNELS)
public class ChannelsTag extends GeneralTag<ChannelsTagAttr> {

    @Resource
    PublishContentManager publishContentManager;

    @Resource
    PublishUtil publishUtil;

    @Override
    public void render() {
        try {
            PublishObj obj = this.getPublishObj();
            ChannelsTagAttr attr = this.getAttrs();
            //需要根据  attr.getNum() 的数量获取列表
            Integer num = attr.getNum();
            obj.setNum(num);
            Channel channel = this.getContent();
            List<Channel> channelList;
            if (channel != null) {
                channelList = publishUtil.readChildChannelByAttr(obj, channel.getId(), attr);
            } else {
                channelList = publishUtil.readChildChannelByAttr(obj, null, attr);
            }
            if (channelList != null) {
                int index = 0;
                int size = channelList.size();
                if (num != null) {
                    num = Math.min(size, num);
                } else {
                    num = size;
                }
                for (int i = attr.getStartpos(); i < num; i++) {
                    channel = channelList.get(i);
                    setContent(channel, TagsConstant.JNET_CHANNELS);
                    setContent((index + 1), TagsConstant.JNET_ROWNUM);
                    setContent(channel, TagsConstant.JNET_CHANNEL);
                    index++;
                    this.doBodyRender();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return TagsConstant.JNET_CHANNELS;
    }

    @Override
    public String getPathName() {
        return PublishConstants.TAG_SEPERATE + getName();
    }
}
