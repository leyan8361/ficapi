package com.fic.service.security;

import com.fic.service.Enum.ErrorCodeEnum;
import com.fic.service.constants.Constants;
import com.fic.service.entity.TokenBase;
import com.fic.service.mapper.TokenBaseMapper;
import com.fic.service.mapper.UserMapper;
import com.fic.service.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Map;

/**
 *   @Author Xie
 *   @Date 2018/11/23
 *   @Discription: Api 拦截器
**/
public class ShiroAccessControllerFilter extends AccessControlFilter {

    private final Logger log = LoggerFactory.getLogger(ShiroAccessControllerFilter.class);

    @Autowired
    TokenBaseMapper tokenBaseMapper;
    @Autowired
    UserMapper userMapper;

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return false;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        log.debug("do validate token action !");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        for(Map.Entry<String,String> filter: Constants.pathMatchFilterMap.entrySet()){
            if(matcher.match(filter.getKey(),uri)){
                return true;
            }
        }
        String token = request.getHeader(Constants.TOKEN_KEY);
        String userAgent = request.getHeader("User-Agent");
        if(StringUtils.isEmpty(token)){
            request.setAttribute("javax.servlet.error.status_code",ErrorCodeEnum.TOKEN_MISSED_HEADER.getCode());
            request.getRequestDispatcher("/error").forward(request,response);
            return false;
        }
        TokenBase tokenBase = tokenBaseMapper.findByTokenValue(token);
        if(null == tokenBase){
            request.setAttribute("javax.servlet.error.status_code",ErrorCodeEnum.TOKEN_NOT_EXIST.getCode());
            request.getRequestDispatcher("/error").forward(request,response);
            return false;
        }
        if(null == tokenBase.getUserId()){
            request.setAttribute("javax.servlet.error.status_code",ErrorCodeEnum.USER_NOT_EXIST.getCode());
            request.getRequestDispatcher("/error").forward(request,response);
            return false;
        }
        if(!token.equals(tokenBase.getTokenValue())){
            request.setAttribute("javax.servlet.error.status_code",ErrorCodeEnum.TOKEN_NOT_MATCH.getCode());
            request.getRequestDispatcher("/error").forward(request,response);
            return false;
        }
        if(DateUtil.dateToLocalDate(tokenBase.getTokenDate()).plusDays(Constants.TOKEN_VALIDITY_DAYS).isBefore(LocalDate.now())){
            request.setAttribute("javax.servlet.error.status_code",ErrorCodeEnum.TOKEN_INVALID.getCode());
            request.getRequestDispatcher("/error").forward(request,response);
            return false;
        }
        if(StringUtils.isEmpty(userAgent) || !tokenBase.getUserAgent().equals(userAgent)){
            request.setAttribute("javax.servlet.error.status_code",ErrorCodeEnum.USER_AGENT_NOT_MATCH.getCode());
            request.getRequestDispatcher("/error").forward(request,response);
            return false;
        }
        return true;
    }

}
