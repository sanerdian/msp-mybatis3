package com.jnetdata.msp.column.service;

import com.jnetdata.msp.column.model.Channel;
import com.jnetdata.msp.core.service.BaseService;
import org.junit.Test;

import java.util.List;

/**
 * Created by WFJ on 2019/4/26.
 */


public interface ChannelService extends BaseService<Channel> {

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
//    public default List<ColumnUtil> selectChildChannel(long chnlId){
//        return null;
//    }
    @Test
    public List<Channel> selectChildChannel(Long chnlId);
}
