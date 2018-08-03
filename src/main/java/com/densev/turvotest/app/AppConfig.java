package com.densev.turvotest.app;

import com.densev.turvotest.util.MapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@ComponentScan("com.densev.turvotest")
public class AppConfig {

    @Bean
    public ObjectMapper getMapper(@Autowired MapperProvider mapperProvider) {
        return mapperProvider.getMapper();
    }


    @Bean
    public static PropertyOverrideConfigurer propertyOverrideConfigurer() {
        PropertyOverrideConfigurer propertyOverrideConfigurer = new PropertyOverrideConfigurer();
        propertyOverrideConfigurer.setLocation(new ClassPathResource("application.properties"));
        propertyOverrideConfigurer.setIgnoreResourceNotFound(true);
        propertyOverrideConfigurer.setIgnoreInvalidKeys(true);
        return propertyOverrideConfigurer;
    }

}
