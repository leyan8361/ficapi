package com.fic.service.constants;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author Xie
 * @Date 2018/11/28
 * @Discription:
 **/
@ConfigurationProperties(prefix = "upload.file.path", ignoreUnknownFields = false)
public class UploadProperties {

    private String win = "";
    private String linux = "";

    @Autowired
    ServerProperties servetProperties;


    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getLinux() {
        return linux;
    }

    public void setLinux(String linux) {
        this.linux = linux;
    }

    public String getCurrentUploadPath() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return win;
        }
        return linux;
    }


    public String getUrl(String path){
        if(StringUtils.isEmpty(path))return null;
        return servetProperties.getPrefix()+path;
    }


}
