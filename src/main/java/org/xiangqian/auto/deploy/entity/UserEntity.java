package org.xiangqian.auto.deploy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.xiangqian.auto.deploy.util.DateUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * @author xiangqian
 * @date 21:33 2024/02/29
 */
@Data
@TableName("user")
public class UserEntity implements UserDetails {

    private static final long serialVersionUID = 1L;

    // 用户id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 用户名
    @TableField("`name`")
    private String name;

    // 昵称
    private String nickname;

    // 密码
    private String passwd;

    // 是否已锁定，0-否，1-是
    private Integer locked;

    // 尝试输入密码次数，超过3次账号将会被锁定
    private Integer tryCount;

    // 上一次登录ip
    private String lastLoginIp;

    // 上一次登录时间（时间戳，单位s）
    private Long lastLoginTime;

    // 当前次登录ip
    private String currentLoginIp;

    // 当前登录时间（时间戳，单位s）
    private Long currentLoginTime;

    // 创建时间（时间戳，s）
    private Long addTime;

    // 修改时间（时间戳，单位s）
    private Long updTime;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        return Objects.equals(name, ((UserEntity) object).name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    // 是否未被限时锁定
    public boolean isNonLimitedTimeLocked() {
        return tryCount < 3 || // 连续输错密码小于3次
                Duration.ofSeconds(DateUtil.toSecond(LocalDateTime.now()) - updTime).toHours() >= 24; // 锁定24小时
    }

    // 人性化限时时间
    public String humanLimitedTime() {
        Duration duration = Duration.ofSeconds(Duration.ofHours(24).toSeconds() - (DateUtil.toSecond(LocalDateTime.now()) - updTime));

        // 小时
        long hour = duration.toHours();
        if (hour > 0) {
            return hour + "小时";
        }

        // 分钟
        long minute = duration.toMinutes();
        if (minute > 0) {
            return minute + "分钟";
        }

        // 秒
        long second = duration.toSeconds();
        return second + "秒";
    }

    /**
     * 获取用户名
     *
     * @return
     */
    @Override
    public String getUsername() {
        return name;
    }

    /**
     * 获取用户密码
     *
     * @return
     */
    @Override
    public String getPassword() {
        return passwd;
    }

    /**
     * 用户账号是否可用
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * 用户账号是否未过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 用户凭证（密码）是否未过期
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 用户账号是否未被锁定
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return locked == 0;
    }

    /**
     * 用户拥有的权限
     * {@link SecurityExpressionRoot#defaultRolePrefix}
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if ("admin".equals(name)) {
            return Set.of(new SimpleGrantedAuthority("ROLE_" + "ADMIN"));
        }
        return Collections.emptySet();
    }

}
