package org.xiangqian.auto.deploy.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.xiangqian.auto.deploy.entity.UserEntity;

/**
 * @author xiangqian
 * @date 21:06 2024/02/29
 */
public interface UserService extends UserDetailsService {

    Boolean updById(UserEntity entity);

    ThreadLocal<UserEntity> getThreadLocal();

}
