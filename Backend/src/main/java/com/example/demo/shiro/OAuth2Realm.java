package com.example.demo.shiro;

import com.example.demo.model.User;
import com.example.demo.service.TokenService;
import com.example.demo.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Shiro 认证类
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo();
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();
        if (accessToken.equals("undefined")) {
            throw new IncorrectCredentialsException("token是undefined!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        //根据accessToken，查询用户信息
        Integer userId = tokenService.queryByToken(accessToken);
        //token失效
        if (userId == null) {
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }

        //查询用户信息
        User user = userService.getById(userId);

        // 续期
        tokenService.refreshToken(userId, accessToken);
        return new SimpleAuthenticationInfo(user, accessToken, getName());
    }

}
