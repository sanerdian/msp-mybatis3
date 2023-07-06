package com.jnetdata.msp.member.user.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.member.user.mapper.UserRelationMapper;
import com.jnetdata.msp.member.user.model.UserRelation;
import com.jnetdata.msp.member.user.service.UserRelationService;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

/**
 * @Author: YUEHAO
 * @Date: 2019/12/3
 */
@Service
public class UserRelationServiceImpl extends BaseServiceImpl<UserRelationMapper,UserRelation> implements UserRelationService{
    @Override
    protected PropertyWrapper<UserRelation> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .eq("userId")
                .getWrapper();
    }
}
