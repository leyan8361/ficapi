package com.fic.service.security;

import com.fic.service.entity.AdminUser;
import com.fic.service.entity.Authority;
import com.fic.service.entity.Permission;
import com.fic.service.mapper.AdminUserMapper;
import com.fic.service.mapper.AuthorityMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 *   @Author Xie
 *   @Date 2018/11/21
 *   @Discription:
**/
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    AdminUserMapper adminUserMapper;
    @Autowired
    AuthorityMapper authorityMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String username = principals.getPrimaryPrincipal().toString();
        Set<Authority> authorities = authorityMapper.findByUsername(username);
        Set<String> roles = new HashSet<>();
        for (Authority authority : authorities) {
            if(StringUtils.isNotEmpty(authority.getName())){
                roles.add(authority.getName());
            }
            for (Permission p : authority.getPermissions()) {
                authorizationInfo.addStringPermission(authority.getName() + ":" + p.getName());
            }
        }
        authorizationInfo.addRoles(roles);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)throws AuthenticationException {
        String username = (String) token.getPrincipal();
        AdminUser adminUser = adminUserMapper.findByUserName(username);
        if (adminUser == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                adminUser.getUserName(),
                adminUser.getPassword(),
                getName()
        );
        return authenticationInfo;
    }
}
