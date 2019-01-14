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
    @Value("${server.mao.yan.url}")
    String maoYanUrl;
    @Value("${contact.address}")
    String contactAddress;
    @Value("${ok.api.key}")
    String okApiKey;
    @Value("${ok.api.secret.key}")
    String okApiSecretKey;
    @Value("${ok.server.url}")
    String okServerUrl;

    public String getMaoYanUrl() {
        return maoYanUrl;
    }

    public void setMaoYanUrl(String maoYanUrl) {
        this.maoYanUrl = maoYanUrl;
    }

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

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getOkApiKey() {
        return okApiKey;
    }

    public void setOkApiKey(String okApiKey) {
        this.okApiKey = okApiKey;
    }

    public String getOkApiSecretKey() {
        return okApiSecretKey;
    }

    public void setOkApiSecretKey(String okApiSecretKey) {
        this.okApiSecretKey = okApiSecretKey;
    }

    public String getOkServerUrl() {
        return okServerUrl;
    }

    public void setOkServerUrl(String okServerUrl) {
        this.okServerUrl = okServerUrl;
    }
}
