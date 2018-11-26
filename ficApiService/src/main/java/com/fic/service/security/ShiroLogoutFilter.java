package com.fic.service.security;

import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 *   @Author Xie
 *   @Date 2018/11/23
 *   @Discription:
**/
public class ShiroLogoutFilter extends LogoutFilter {

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        httpResponse.sendRedirect("/backend/logout");
        return true;
    }
}
