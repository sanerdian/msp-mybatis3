package com.jnetdata.msp.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.flowable.engine.*;
//import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.thenicesys.sql.DruidDataSource;
//
//@Configuration
//@Slf4j
//public class FlowableConfig {
//
//    @Value("${spring.datasource.url}")
//    private String url;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//    @Value("${flowable.datasource.schema}")
//    private String schema;
//
//    @Bean
//    public ProcessEngineConfiguration processEngineConfiguration() {
//
//        DruidDataSource druidDataSource = new DruidDataSource();
//        druidDataSource.setUrl(url);
//        druidDataSource.setUsername(username);
//        druidDataSource.setPassword(password);
//        druidDataSource.setSchemaname(schema);
//
//        // 流程引擎配置
//        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration();
//        cfg.setDataSource(druidDataSource);
//        cfg.setAsyncExecutorActivate(false);
//        // 初始化基础表，不需要的可以改为 DB_SCHEMA_UPDATE_FALSE
//        cfg.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
//        // 默认邮箱配置
//        // 发邮件的主机地址，先用 QQ 邮箱
//        cfg.setMailServerHost("smtp.qq.com");
//        // POP3/SMTP服务的授权码
//        cfg.setMailServerPassword("xxxxxxx");
//        // 默认发件人
//        cfg.setMailServerDefaultFrom("xxx@qq.com");
//        // 设置发件人用户名
//        cfg.setMailServerUsername("管理员");
//        // 解决流程图乱码
//        cfg.setActivityFontName("宋体");
//        cfg.setLabelFontName("宋体");
//        cfg.setAnnotationFontName("宋体");
//        return cfg;
//    }
//
//    // 初始化流程引擎
//    @Primary
//    @Bean(name = "processEngine")
//    public ProcessEngine initProcessEngine() {
//        log.info("=============================ProcessEngineBegin=============================");
//        // 初始化流程引擎对象
//        ProcessEngine processEngine = processEngineConfiguration().buildProcessEngine();
//        log.info("=============================ProcessEngineEnd=============================");
//        return processEngine;
//    }
//
//    //八大接口
//    // 业务流程的定义相关服务
//    @Bean
//    public RepositoryService repositoryService(ProcessEngine processEngine) {
//        return processEngine.getRepositoryService();
//    }
//
//    // 流程对象实例相关服务
//    @Bean
//    public RuntimeService runtimeService(ProcessEngine processEngine) {
//        return processEngine.getRuntimeService();
//    }
//
//    // 流程任务节点相关服务
//    @Bean
//    public TaskService taskService(ProcessEngine processEngine) {
//        return processEngine.getTaskService();
//    }
//
//    // 流程历史信息相关服务
//    @Bean
//    public HistoryService historyService(ProcessEngine processEngine) {
//        return processEngine.getHistoryService();
//    }
//
//    // 管理和维护相关服务
//    @Bean
//    public ManagementService managementService(ProcessEngine processEngine) {
//        return processEngine.getManagementService();
//    }
//
//    // 用户以及组管理相关服务
//    @Bean
//    public IdentityService identityService(ProcessEngine processEngine) {
//        return processEngine.getIdentityService();
//    }
//
//    // 表单引擎相关服务
//    @Bean
//    public FormService formService(ProcessEngine processEngine) {
//        return processEngine.getFormService();
//    }
//
//    // 动态流程服务
//    @Bean
//    public DynamicBpmnService dynamicBpmnService(ProcessEngine processEngine){
//        return processEngine.getDynamicBpmnService();
//    }
//
//}
public class FlowableConfig {}