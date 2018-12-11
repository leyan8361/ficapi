package com.fic.service.utils;

import org.apache.shiro.SecurityUtils;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
public class SecurityUtil {

    public static String getCurrentUser() {
        String loginName = (String) SecurityUtils.getSubject().getPrincipal();
        return loginName;
    }
}
