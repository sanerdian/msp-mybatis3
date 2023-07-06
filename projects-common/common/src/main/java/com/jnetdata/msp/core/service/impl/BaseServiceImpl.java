package com.jnetdata.msp.core.service.impl;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.jnetdata.msp.core.service.BaseService;
import org.apache.ibatis.session.SqlSession;
import org.thenicesys.data.api.ConditionContainer;
import org.thenicesys.data.api.util.ConditionMap;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.mybatis.impl.PropertyWrapperUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/28.
 * @author
 */
public class BaseServiceImpl <M extends BaseMapper<T> & org.thenicesys.mybatis.BaseMapper<T>, T> extends org.thenicesys.mybatis.impl.BaseServiceImpl<M, T> implements BaseService<T> {

    private int doBatchInsert(String sqlStatement, Iterable<Object> params, int batchSize) {

        int count = 0;
        int i = 1;
        SqlSession batchSqlSession = sqlSessionBatch();
        try {
            for(Object param : params) {
                count += 1;
                batchSqlSession.insert(sqlStatement, param);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }catch(Exception e) {
            throw new RuntimeException(i+"", e);
        }finally {
            batchSqlSession.clearCache();
            batchSqlSession.close();
        }
        return count;
    }

    protected int batchInsert(String mapperMethod, Iterable<Object> params, int batchSize) {
        Class<T> mapperClass = currentMapperClass();
        String sqlStatement = mapperClass.getName() + "." + mapperMethod;
        return doBatchInsert(sqlStatement, params, batchSize);
    }

    @Override
    protected PropertyWrapper<T> makeSearchWrapper(ConditionContainer condition) {
        return makeSearchWrapper((Map<String, Object>)condition);
    }

    protected PropertyWrapper<T> makeSearchWrapper(Map<String, Object> condition) {
        throw new RuntimeException("没有实现");
    }

    protected PropertyWrapperUtil<T> createWrapperUtil(Map<String, Object> condition) {
        return new PropertyWrapperUtil<>(currentModelClass(), (ConditionContainer)condition);
    }

    protected PropertyWrapper<T> setBasePw(PropertyWrapperUtil<T>  pwu,Map<String, Object> condition) {
        PropertyWrapper<T> pw = pwu.eq("createBy")
                .eq("crUser")
                .ge("createTime")
                .eq("modifyBy")
                .ge("modifyTime")
                .eq("modifyUser")
                .getWrapper()
                .le(condition.get("createTimeBt2") != null && !condition.get("createTimeBt2").equals(""), "createTime", condition.get("createTimeBt2"))
                .le(condition.get("modifyTimeBt2") != null && !condition.get("modifyTimeBt2").equals(""), "modifyTime", condition.get("modifyTimeBt2"));
        return pw;
    }

    protected Class<T> currentMapperClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 0);
    }

    @Override
    public List<T> list(ConditionMap entity) {
        return search(entity);
    }
    @Override
    public List<T> list(T entity) {
        return search(ConditionMap.of(entity));
    }

}
