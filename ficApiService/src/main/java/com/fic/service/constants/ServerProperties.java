package com.fic.service.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author Xie
 * @Date 2018/11/28
 * @Discription:
 **/
@ConfigurationProperties
public class ServerProperties {

    @Value("${resource.view.url.prefix}")
    String prefix;
    @Value("${server.wallet.url}")
    String walletUrl;
    @Value("${server.wallet.userpaper}")
    String storeLocation;

    public String getWalletUrl() {
        return walletUrl;
    }

    public void setWalletUrl(String walletUrl) {
        this.walletUrl = walletUrl;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

}
