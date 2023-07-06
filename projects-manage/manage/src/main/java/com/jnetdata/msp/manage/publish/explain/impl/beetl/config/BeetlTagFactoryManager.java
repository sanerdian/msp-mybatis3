package com.jnetdata.msp.manage.publish.explain.impl.beetl.config;

import com.jnetdata.msp.manage.publish.explain.impl.beetl.annoation.BeetlTagName;
import org.beetl.core.tag.Tag;
import org.beetl.core.tag.TagFactory;
import org.beetl.ext.spring.SpringBeanTagFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/8/21 16:08
 */
@Component
public class BeetlTagFactoryManager implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean(name = "tagFactorys")
    public Map<String, TagFactory> tagFactorys() {
        Map<String, TagFactory> tags = new HashMap<>();
        Map<String, Tag> beans = applicationContext.getBeansOfType(Tag.class);
        for (String beanName : beans.keySet()) {
            // 读取自定义标签名
            BeetlTagName tagAnno = beans.get(beanName).getClass().getAnnotation(BeetlTagName.class);
            String tagName = tagAnno != null ? tagAnno.value() : beanName;
            tags.put(tagName, toSpringBeanTagFactory(beanName));
        }
        return tags;
    }

    private SpringBeanTagFactory toSpringBeanTagFactory(String beanName) {
        SpringBeanTagFactory springBeanTagFactory = new SpringBeanTagFactory();
        springBeanTagFactory.setName(beanName);
        springBeanTagFactory.setApplicationContext(applicationContext);
        return springBeanTagFactory;
    }
}