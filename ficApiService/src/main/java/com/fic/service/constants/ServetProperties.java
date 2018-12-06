package com.fic.service.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author Xie
 * @Date 2018/11/28
 * @Discription:
 **/
@ConfigurationProperties
public class ServetProperties {

    @Value("${resource.view.url.prefix}")
    String prefix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

}
