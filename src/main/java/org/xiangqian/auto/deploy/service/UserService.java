package org.xiangqian.auto.deploy.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.xiangqian.auto.deploy.entity.UserEntity;
import org.xiangqian.auto.deploy.vo.user.ResetPasswdVo;

import java.util.List;

/**
 * @author xiangqian
 * @date 21:06 2024/02/29
 */
public interface UserService extends UserDetailsService {

    Boolean del(Long id);

    Boolean unlock(Long id);

    Boolean lock(Long id);

    Boolean resetPasswd(ResetPasswdVo vo);

    List<UserEntity> list();

    Boolean updById(UserEntity entity);

    ThreadLocal<UserEntity> getThreadLocal();

}
