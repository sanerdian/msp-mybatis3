package com.jnetdata.msp.config;

import com.jnetdata.msp.member.shiro.impl.MobilePhoneNumberAuthRealm;
import com.jnetdata.msp.member.shiro.impl.MyModularRealmAuthenticator;
import com.jnetdata.msp.member.shiro.impl.RetryLimitHashedCredentialsMatcher;
import com.jnetdata.msp.member.shiro.impl.UserNamePasswordAuthRealm;
import lombok.val;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisClusterManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import redis.clients.jedis.JedisCluster;

import java.util.*;

@Configuration
@EnableConfigurationProperties({ShiroProperties.class,RedisProperties.class})
public class ShiroConfig {

    @Bean
    public RedisProperties redisConfig(){
        return new RedisProperties();
    }

    /**
     * <pre>
     * 凭证(credential)匹配器，用于判断提供的凭证是否和系统保存的凭证匹配
     * 比如：提供的用户名和密码， 是否和数据库保存的用户名和密码匹配
     * </pre>
     * @return
     */
    @Bean
    public CredentialsMatcher credentialsMatcher() {
        RetryLimitHashedCredentialsMatcher matcher = null;
        if(redisConfig().isUsed()){
            matcher = new RetryLimitHashedCredentialsMatcher(redisCacheManager());
        }else{
            matcher = new RetryLimitHashedCredentialsMatcher(cacheManager());
        }
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(1);
        return matcher;
    }

    /**
     * <pre>
     * 用户名+密码 认证（登录）
     * </pre>
     * @return
     */
    @Bean
    public UserNamePasswordAuthRealm userNamePasswordAuthRealm() {
        val userNamePasswordAuthRealm = new UserNamePasswordAuthRealm();
        userNamePasswordAuthRealm.setCredentialsMatcher(credentialsMatcher());
        return userNamePasswordAuthRealm;
    }

    /**
     * <pre>
     * 手机号 认证（登录）
     * </pre>
     * @return
     */
    @Bean
    public MobilePhoneNumberAuthRealm mobilePhoneNumberAuthRealm() {
        return new MobilePhoneNumberAuthRealm();
    }

    /**
     * <pre>
     * 内存缓存管理
     * </pre>
     * @return
     */
    @Bean
    public MemoryConstrainedCacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    /**
     * <pre>
     * 模块化认证器（让应用支持多种认证方法）
     * </pre>
     * @return
     */
    @Bean
    public MyModularRealmAuthenticator myModularRealmAuthenticator() {
        MyModularRealmAuthenticator myModularRealmAuthenticator = new MyModularRealmAuthenticator();
        // 设置（多种认证方法）认证策略：至少一种认证成功
        myModularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return myModularRealmAuthenticator;
    }

    /**
     * <pre>
     * 继承Authenticator, Authorizer, SessionManager，集成应用程序所有Subject（用户）的安全操作
     * 通常应用程序使用SecurityUtils.getSubject()对当前用户操作
     * </pre>
     * @return
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(myModularRealmAuthenticator());
        securityManager.setSessionManager(sessionManager());
//        securityManager.setCacheManager(cacheManager());
        List<Realm> realms = new ArrayList<>();
        realms.add(userNamePasswordAuthRealm());
        securityManager.setRealms(realms);
        return securityManager;
    }

    @Bean
    public SimpleCookie cookie(){
        SimpleCookie cookie = new SimpleCookie("JSESSIONID"); //  cookie的name,对应的默认是 JSESSIONID
        cookie.setPath("/");        //  path为 / 用于多个系统共享JSESSIONID
        cookie.setHttpOnly(true);
//        cookie.setSameSite(Cookie.SameSiteOptions.NONE);
//        cookie.setSecure(true);
        return cookie;
    }

    /**
     * EnterpriseCacheSessionDAO shiro sessionDao层的实现；
     * 提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     */
    @Bean
    public EnterpriseCacheSessionDAO enterCacheSessionDAO() {
        EnterpriseCacheSessionDAO enterCacheSessionDAO = new EnterpriseCacheSessionDAO();
        //添加缓存管理器
        //enterCacheSessionDAO.setCacheManager(ehCacheManager());
        //添加ehcache活跃缓存名称（必须和ehcache缓存名称一致）
        enterCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        return enterCacheSessionDAO;
    }

    @Bean
    public DefaultWebSessionManager sessionManager(){
        ShiroSessionManager sessionManager = new ShiroSessionManager();
        sessionManager.setGlobalSessionTimeout(redisConfig().getTimeout()==0?30*60*1000:redisConfig().getTimeout());    // 设置session超时
        sessionManager.setDeleteInvalidSessions(true);      // 删除无效session
//        sessionManager.setSessionIdCookie(cookie());            // 设置JSESSIONID
        sessionManager.setSessionDAO(sessionDAO());         // 设置sessionDAO
        if(!redisConfig().isUsed()) {
            sessionManager.setCacheManager(cacheManager());
        }
        return sessionManager;
    }

    //配置cacheManager
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        if(redisConfig().getIsCluster() == 1){
            redisCacheManager.setRedisManager(redisClusterManager());
        }else{
            redisCacheManager.setRedisManager(redisManager());
        }
        return redisCacheManager;
    }

    //配置redisManager
    public RedisManager redisManager() {
        RedisProperties redisProperties = redisConfig();
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisProperties.getAllHost());
        redisManager.setTimeout(redisProperties.getTimeout());
        redisManager.setPassword(redisProperties.getPassword());
        return redisManager;
    }

    //配置redisManager
    public RedisClusterManager redisClusterManager() {
        RedisProperties redisProperties = redisConfig();
        RedisClusterManager redisClusterManager = new RedisClusterManager();
        redisClusterManager.setHost(redisProperties.getHost());
        redisClusterManager.setTimeout(redisProperties.getTimeout());
        redisClusterManager.setPassword(redisProperties.getPassword());
        return redisClusterManager;
    }

    //配置redisSessionDAO
    @Bean
    public AbstractSessionDAO sessionDAO() {
        if(redisConfig().isUsed()){
            RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
            if(redisConfig().getIsCluster() == 1){
                redisSessionDAO.setRedisManager(redisClusterManager());
            }else{
                redisSessionDAO.setRedisManager(redisManager());
            }
            return redisSessionDAO;
        }else{
            return new MemorySessionDAO();
        }
    }


    /**
     * <pre>
     * Shiro过滤器配置
     * 配置 loginUrl, successUrl, unauthorizedUrl
     * 以及setFilterChainDefinitionMap（url -> shiro过滤器)
     * </pre>
     * @param securityManager
     * @param shiroProperties
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, ShiroProperties shiroProperties) {
        CustomShiroFilterFactoryBean shiroFilterFactoryBean = new CustomShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //登录
        shiroFilterFactoryBean.setLoginUrl(shiroProperties.getLoginUrl());
//        //首页
//        shiroFilterFactoryBean.setSuccessUrl(shiroProperties.getSuccessUrl());
        //错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl(shiroProperties.getAnauthorizedUrl());

        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        for(String filterChainDefinition : shiroProperties.getFilterChainDefinitions()) {
            String[] split = filterChainDefinition.split("=");
            filterChainDefinitionMap.put(split[0].trim(), split[1].trim());
        }
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * <p>
     *  Bean post processor for Spring that automatically calls the <tt>init()</tt> and/or
     *  <tt>destroy()</tt> methods on Shiro objects that implement the {@link org.apache.shiro.util.Initializable}
     *  or {@link org.apache.shiro.util.Destroyable} interfaces, respectfully.
     *  </p>
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        LifecycleBeanPostProcessor lifecycleBeanPostProcessor = new LifecycleBeanPostProcessor();
        return lifecycleBeanPostProcessor;
    }

    /**
     * Bean post processor 启用Shiro相关的注解
     */
    @Bean
    @ConditionalOnMissingBean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
