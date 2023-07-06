package com.jnetdata.msp.metadata.gitaccount.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.gitaccount.mapper.GitaccountMapper;
import com.jnetdata.msp.metadata.gitaccount.model.Gitaccount;
import com.jnetdata.msp.metadata.gitaccount.service.GitaccountService;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyj
 * @since 2019-09-23
 */
@Service
public class GitaccountServiceImpl extends BaseServiceImpl<GitaccountMapper, Gitaccount> implements GitaccountService {
    @Override
    protected PropertyWrapper<Gitaccount> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                        .eq("id")
                                .like("account")
                                .like("password")
                    .getWrapper();
    }
}
