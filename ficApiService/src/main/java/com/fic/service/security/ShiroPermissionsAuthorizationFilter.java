package com.fic.service.security;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *   @Author Xie
 *   @Date 2018/11/22
 *   @Discription: 权限拦截
**/
public class ShiroPermissionsAuthorizationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) {
        return !isLoginRequest(request, response) && isPermissive(mappedValue);
    }

}
