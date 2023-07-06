package com.jnetdata.msp.manage.publish.explain.impl.beetl.config;

import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.beetl.core.tag.TagFactory;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.util.Map;

/**
 *
 */
@Configuration
public class TagConfiguration {

    //@Lazy
    @Bean(name = "tagsConfig")
    public BeetlGroupUtilConfiguration getTagsConfig(@Qualifier("tagFactorys") Map<String, TagFactory> tagFactorys) {

        //Configuration config = org.beetl.core.Configuration.defaultConfiguration();
        BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
        ResourcePatternResolver patternResolver = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());
        try {
            StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
            beetlGroupUtilConfiguration.setConfigFileResource(patternResolver.getResource("classpath:tags.properties"));
            beetlGroupUtilConfiguration.setResourceLoader(resourceLoader);
            beetlGroupUtilConfiguration.setTagFactorys(tagFactorys);
            beetlGroupUtilConfiguration.init();
            return beetlGroupUtilConfiguration;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean(name = "groupTemplate")
    public GroupTemplate getGroupTemplate(
            @Qualifier("tagsConfig") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
        return beetlGroupUtilConfiguration.getGroupTemplate();
    }
}
