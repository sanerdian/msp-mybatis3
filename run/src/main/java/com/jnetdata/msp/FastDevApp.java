package com.jnetdata.msp;

import org.flowable.ui.modeler.conf.ApplicationConfiguration;
import org.flowable.ui.modeler.conf.DatabaseConfiguration;
import org.flowable.ui.modeler.servlet.AppDispatcherServletConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;


@Import({
        ApplicationConfiguration.class,
        AppDispatcherServletConfiguration.class,
        DatabaseConfiguration.class
})
@SpringBootApplication
@EnableScheduling
public class FastDevApp extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(FastDevApp.class, args);
    }
}
