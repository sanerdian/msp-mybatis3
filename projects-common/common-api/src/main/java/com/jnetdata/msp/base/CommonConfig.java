package com.jnetdata.msp.base;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class CommonConfig {

    @Value("${spring.datasource.url}")
    public static String url;

    @Value("${spring.datasource.username}")
    public static String username;

    @Value("${spring.datasource.password}")
    public static String password;

    @Value("${spring.datasource.jdbcDialect}")
    public static String jdbcDialect;

    @Value("${spring.datasource.driverClassName}")
    public static String driverName;
}
