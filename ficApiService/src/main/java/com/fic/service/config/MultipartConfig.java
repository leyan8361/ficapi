package com.fic.service.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 *   @Author Xie
 *   @Date 2018/11/28
 *   @Discription:
**/
@Configuration
public class MultipartConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("10120KB");
        factory.setMaxRequestSize("10120KB");
        return factory.createMultipartConfig();
    }

}
