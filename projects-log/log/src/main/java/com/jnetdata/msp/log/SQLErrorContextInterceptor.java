package com.jnetdata.msp.log;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.jdbc.PreparedStatementLogger;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.Properties;


@Intercepts({
        @Signature(type = StatementHandler.class, method = "parameterize", args = {Statement.class}) })
public class SQLErrorContextInterceptor implements Interceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object obj=invocation.getArgs()[0];
        System.out.println("Before StatementHandler. parameterize()...");
        System.out.println("Before sql:"+obj.toString());
        invocation.proceed();
        System.out.println("Aflter StatementHandler .parameterize()...");
        System.out.println("Aflter sql:"+obj.toString());

        System.out.println("sql:"+obj.toString());
        //方式1：直接使用statment对象的 toString方法
        // ErrorContext.instance().sql(obj.toString());
        //方式2 ： 反射获取PreparedStatementLogger父类的PreparedStatementLogger方法
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        Object handler=Proxy.getInvocationHandler(obj);
        PreparedStatementLogger pstLogger=(PreparedStatementLogger)handler;
        Class clz=pstLogger.getClass().getSuperclass();
        Method m=clz.getDeclaredMethod("getParameterValueString");
        m.setAccessible(true);
        String logStr=(String) m.invoke(pstLogger);
        System.out.println(logStr);
        ErrorContext.instance().sql( boundSql.getSql()+" parameters:"+logStr);
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String dialect = properties.getProperty("dialect");
        logger.info("mybatis intercept dialect:{}", dialect);

    }

}