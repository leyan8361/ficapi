package com.fic.service.config;

import com.fic.service.entity.Authority;
import com.fic.service.entity.Permission;
import com.fic.service.mapper.AuthorityMapper;
import com.fic.service.security.*;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *   @Author Xie
 *   @Date 2018/11/21
 *   @Discription:
**/
@Configuration
public class ShiroConfig {

    @Autowired
    AuthorityMapper authorityMapper;

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filterMap = new HashMap();
        filterMap.put("logout",new ShiroLogoutFilter());
        filterMap.put("api_filter",shiroAccessControllerFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        filterChainDefinitionMap.put("/api/v1/**", "api_filter");
        filterChainDefinitionMap.put("/api/v2/**", "api_filter");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/backend/login", "anon");

        shiroFilterFactoryBean.setLoginUrl("/backend/unLogin");
        shiroFilterFactoryBean.setSuccessUrl("/backend/loginSuccess");
        shiroFilterFactoryBean.setUnauthorizedUrl("/backend/unAuthorized");
        Set<Authority> authorities = authorityMapper.findAll();
        authorities.forEach(authority->{
            Set<Permission> permissions = authority.getPermissions();
            permissions.forEach(permission -> {
                filterChainDefinitionMap.put(permission.getUrl()+"**", "authc,roles["+authority.getName()+"],perms["+authority.getName()+":"+permission.getName()+"]");
                System.out.println(permission.getUrl()+"**" + " : authc,roles[" + authority.getName() + "],perms["+authority.getName()+":info]");
            });
        });
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

//    @Bean
//    public HashedCredentialsMatcher hashedCredentialsMatcher() {
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        hashedCredentialsMatcher.setHashAlgorithmName("md5");
//        hashedCredentialsMatcher.setHashIterations(1);
//        return hashedCredentialsMatcher;
//    }

    @Bean
    public ShiroAccessControllerFilter shiroAccessControllerFilter(){
        return new ShiroAccessControllerFilter();
    }

    @Bean
    public ShiroRealm myShiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
//        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }


    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }

    @Bean
    public SessionManager sessionManager() {
        SessionManager mySessionManager = new SessionManager();
        return mySessionManager;
    }

    @Bean
    public ShiroAccessControllerFilter shiroOncePerRequestFilter() {
        return new ShiroAccessControllerFilter();
    }




}
