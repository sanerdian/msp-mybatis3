package com.jnetdata.msp.metadata.subscribe.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.metadata.subscribe.model.Subscribeinfo;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.vo.PageVo1;

import java.util.List;


public interface SubscribeService extends BaseService<Subscribeinfo> {
    Subscribeinfo selectidone(Subscribeinfo vo);

    int selectall(List<Subscribeinfo> ids);

    List<Subscribeinfo> selectalluser(Long userid,Long classtreeid);

    int selectclassid(Long s, Long userid,Long classtreeid);

    Page<Subscribeinfo> selectPageall(Page pager, Long classid);
}
