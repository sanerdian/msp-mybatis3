package com.jnetdata.msp.cs1.test_test1_test2.service.impl;

import com.jnetdata.msp.cs1.test_test1_test2.model.TestTest1Test2;
import com.jnetdata.msp.cs1.test_test1_test2.mapper.TestTest1Test2Mapper;
import com.jnetdata.msp.cs1.test_test1_test2.service.TestTest1Test2Service;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.function.Consumer;


/**
 * <p>
 * TEST_TEST1_TEST2 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2023-03-08
 */
@Service
public class TestTest1Test2ServiceImpl extends BaseServiceImpl<TestTest1Test2Mapper, TestTest1Test2> implements TestTest1Test2Service {


    private String andOr = "and";
    @Override
    public PropertyWrapper<TestTest1Test2> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapper<TestTest1Test2> pw = createWrapperUtil(condition)
        //0
        //id
        //0
        //docchannelid
        //0
        //docstatus
        //0
        //singletempkate
        //0
        //siteid
        //0
        //docvalid
        //0
        //docpubtime
        //0
        //operuser
        //0
        //opertime
        //0
        //doctitle
        //0
        //docreltime
        //0
        //docpuburl
        //0
        //linkurl
        //0
        //classinfoid
        //0
        //status
        //0
        //companyid
        //0
        //websiteid
        //0
        //columnid
        //0
        //seqencing
        //0
        //flowId
        //0
        //flowUser
        //0
        //quotainfo
        //0
        //copyfromid
        .eq("createBy")
        .getWrapper();
        if (condition.get("columnid") != null) {
            pw.and(m -> m.eq("columnid",condition.get("columnid")).or().like("quotainfo",condition.get("columnid") + ":"));
        }

        if(condition.get("pubIds")!=null){
            List<Long> pubIds = (List<Long>)condition.get("pubIds");
            pw.in(!pubIds.isEmpty(),"id",pubIds);
        }

        return pw;
    }

    @Override
    public TestTest1Test2 getEntityAndJoinsById(Long id){
        TestTest1Test2 entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(TestTest1Test2 entity, TestTest1Test2 vo) {
        if(vo == null) vo = new TestTest1Test2();
    }

    @Override
    public void getListJoin(List<TestTest1Test2> list, TestTest1Test2 vo) {
        for (TestTest1Test2 entity : list) {
            getJoin(entity, vo);
        }
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<TestTest1Test2>> queryWrapperConsumer = w -> {
                for(String str:condition.get(proName).toString().split(" ")){
                    if(StringUtils.isNotEmpty(str)){
                        if(andOr.equals("or" )) w.or();
                        w.like(pw.getColumn(proName),str);
                    }
                }
            };
            pw.and(queryWrapperConsumer);
        }
    }
}
