package com.jnetdata.msp.column.service.impl;

import com.jnetdata.msp.column.mapper.ChannelMapper;
import com.jnetdata.msp.column.model.Channel;
import com.jnetdata.msp.column.service.ChannelService;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.List;
import java.util.Map;

/**
 * Created by WFJ on 2019/4/26.
 */
@Service
public class ChannelServiceImpl extends BaseServiceImpl<ChannelMapper, Channel> implements ChannelService {

    @Autowired
    private ChannelMapper pbs;

    @Override
    protected PropertyWrapper<Channel> makeSearchWrapper(Map<String, Object> condition) {
        Object conditionIds = condition.get("conditionIds");
        PropertyWrapper<Channel> wrapper = createWrapperUtil(condition)
                .eq("status")
                .eq("parentId")
                .eq("siteId")
                .like("name")
                .getWrapper();
        if (conditionIds != null && ((List<Long>) conditionIds).size() > 0) {
            wrapper.and(i -> i.in(conditionIds != null, "CHANNELID", (List<Long>) conditionIds).or().eq("CRNUMBER", SessionUser.getCurrentUser().getId()));
        } else if (!SecurityUtils.getSubject().isPermitted("column:list")) {
            wrapper.eq("createBy", SessionUser.getCurrentUser().getId());
        }
        return wrapper;
    }
    /**
     * TODO xuning 待实现
     *  查询栏目下的全部子栏目(全部层级,子,孙等)
     *  栏目必须有模板概览和细览id,如无则直接忽略该栏目下子栏目
     *  如栏目有多级,则每个层级均如此
     *  内部数据按页面展示的栏目顺序  从上(及子栏目) 至下
     *  @param chnlId
     *  @author hongshou
     *  @date 2022/8/20
     *  @return
     */
    @Override
    public List<Channel> selectChildChannel(Long chnlId) {
        return pbs.selectChildChannel(chnlId,chnlId);
    }
}
