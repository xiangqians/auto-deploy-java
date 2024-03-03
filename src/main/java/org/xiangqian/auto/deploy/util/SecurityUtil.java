package org.xiangqian.auto.deploy.util;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.xiangqian.auto.deploy.entity.UserEntity;

import java.util.Collection;

/**
 * @author xiangqian
 * @date 15:26 2024/03/02
 */
public class SecurityUtil {

    public static boolean isAdminRole() {
        UserEntity entity = getUser();
        if (entity == null) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = entity.getAuthorities();
        if (CollectionUtils.isEmpty(authorities)) {
            return false;
        }

        return authorities.contains(createAdminRoleAuthority());
    }

    /**
     * 创建管理员角色权限
     * {@link SecurityExpressionRoot#defaultRolePrefix}
     *
     * @return
     */
    public static GrantedAuthority createAdminRoleAuthority() {
        return new SimpleGrantedAuthority("ROLE_ADMIN");
    }

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
