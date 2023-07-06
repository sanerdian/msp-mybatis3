package com.jnetdata.msp.swagger;

import com.jnetdata.msp.swagger.model.Swaggerconfiguration;
import com.jnetdata.msp.swagger.service.SwaggerconfigurationService;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.util.List;


/**
 * Swagger配置类.
 * http://www.cnblogs.com/softidea/p/6251249.html
 */
@EnableSwagger2
@Component
@Data
//@ConditionalOnProperty(name="swagger2.enable",havingValue="true")
public class SwaggerConfiguration implements ApplicationContextAware {
/*

    public static final String BASE_PACKAGE = "com.jnetdata.msp";
    public static final String SWAGGER_SCAN_CONFIG_PACKAGE = BASE_PACKAGE+".config";
    public static final String SWAGGER_SCAN_LOG_PACKAGE = BASE_PACKAGE+".log";
    public static final String SWAGGER_SCAN_COURSE_PACKAGE = BASE_PACKAGE+".course";
    public static final String SWAGGER_SCAN_MANAGE_PACKAGE = BASE_PACKAGE+".manage";
    public static final String SWAGGER_SCAN_DEMO_PACKAGE = BASE_PACKAGE+".demo";
    public static final String SWAGGER_SCAN_METADATA_PACKAGE = BASE_PACKAGE+".metadata";
    public static final String SWAGGER_SCAN_FLOWABLE_PACKAGE = BASE_PACKAGE+".flowable";
    public static final String SWAGGER_SCAN_RESOURCES_PACKAGE = BASE_PACKAGE+".resources";
    public static final String SWAGGER_SCAN_GENERATED_PACKAGE = BASE_PACKAGE+".generated";
    public static final String SWAGGER_SCAN_GENERATOR_PACKAGE = BASE_PACKAGE+".generator";
    public static final String SWAGGER_SCAN_CHINA_PACKAGE = BASE_PACKAGE+".china";


    @Bean
    public Docket createDemoApi() {
        return createApi(SWAGGER_SCAN_DEMO_PACKAGE, "demo");
    }

    @Bean
    public Docket createCourseApi() {
        return createApi(SWAGGER_SCAN_COURSE_PACKAGE, "course");
    }

    @Bean
    public Docket createConfigApi() {
        return createApi(SWAGGER_SCAN_CONFIG_PACKAGE, "config");
    }

    @Bean
    public Docket createLogApi() {
        return createApi(SWAGGER_SCAN_LOG_PACKAGE, "log");
    }

    @Bean
    public Docket createManageApi() {
        return createApi(SWAGGER_SCAN_MANAGE_PACKAGE, "manage");
    }

    @Bean
    public Docket createMetadataApi() {
        return createApi(SWAGGER_SCAN_METADATA_PACKAGE, "metadata");
    }

    @Bean
    public Docket createFlowableApi() {
        return createApi(SWAGGER_SCAN_FLOWABLE_PACKAGE, "flowable");
    }

    @Bean
    public  Docket createResourcesApi(){
        return  createApi(SWAGGER_SCAN_RESOURCES_PACKAGE,"resources");
    }

    @Bean
    public  Docket createGeneratedApi(){
        return  createApi(SWAGGER_SCAN_GENERATED_PACKAGE,"generated");
    }

    @Bean
    public  Docket createGeneratorApi(){
        return  createApi(SWAGGER_SCAN_GENERATOR_PACKAGE,"generator");
    }

    @Bean
    public  Docket createChinaApi(){
        return  createApi(SWAGGER_SCAN_CHINA_PACKAGE,"china");
    }

*/
    @Autowired
    private SwaggerconfigurationService swaggerconfigurationService;

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @PostConstruct
    public void register(){
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext)context;
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        List<Swaggerconfiguration> list = swaggerconfigurationService.list();
        for (int i = 0; i <= list.size(); i++) {
            if (i == 0) {
                defaultListableBeanFactory.registerSingleton("createAllApi", createApi("com.jnetdata.msp","all"));
            } else {
                defaultListableBeanFactory.registerSingleton(list.get((i-1)).getBeanname(), createApi(list.get((i-1)).getBasepackagename(),list.get((i-1)).getGroupname()));
            }
        }
        /*list.forEach(e->{
        defaultListableBeanFactory.registerSingleton(e.getBeanname(), createApi(e.getBasepackagename(),e.getGroupname()));
        });*/
    }

    private Docket createApi(String basePackage, String groupName) {
        return createApi(basePackage, groupName, "1.0");
    }

    private Docket createApi(String basePackage, String groupName, String version) {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(groupName)
                .description(groupName)
                .version("1.0")
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }


}
