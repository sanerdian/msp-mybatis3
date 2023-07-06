package com.jnetdata.msp.member.group.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.member.group.model.Groupinfo;

import java.util.List;
import java.util.Map;

/**
 * Created by WFJ on 2019/4/1.
 */
public interface GroupService extends BaseService<Groupinfo> {

    Groupinfo  listOr(Long id);

    /*
     * 获取组织树形结构
     * @author hongshou
     * @date 2020/5/26
     * */
    List<Groupinfo> getGroupTree();

    List<Groupinfo> tree();

    List<Groupinfo> tree(Long id);

    List<Groupinfo> tree(List<Long> ids);

    List<Groupinfo> tree(Long id, List<Long> ids);

    List<Groupinfo> list(Groupinfo obj);
}
