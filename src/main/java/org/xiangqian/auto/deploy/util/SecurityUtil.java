package org.xiangqian.auto.deploy.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.xiangqian.auto.deploy.entity.UserEntity;

/**
 * @author xiangqian
 * @date 15:26 2024/03/02
 */
public class SecurityUtil {

    public static UserEntity getUser() {
        Authentication authentication = getAuthentication();
        if (isLoggedin(authentication)) {
            return (UserEntity) authentication.getPrincipal();
        }
        return null;
    }

    public static boolean isLoggedin() {
        Authentication authentication = getAuthentication();
        return isLoggedin(authentication);
    }

    private static boolean isLoggedin(Authentication authentication) {
        // 匿名用户（表示用户未登录）
        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }

        // 用户已通过身份验证（已登录）
        if (authentication != null && authentication.isAuthenticated()) {
            return true;
        }

        // 用户未登录
        return false;
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
