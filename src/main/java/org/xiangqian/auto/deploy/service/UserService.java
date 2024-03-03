package org.xiangqian.auto.deploy.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.xiangqian.auto.deploy.entity.UserEntity;
import org.xiangqian.auto.deploy.vo.user.UserAddVo;
import org.xiangqian.auto.deploy.vo.user.UserResetPasswdVo;

import java.util.List;

/**
 * @author xiangqian
 * @date 21:06 2024/02/29
 */
public interface UserService extends UserDetailsService {

    Boolean add(UserAddVo vo);

    Boolean delById(Long id);

    Boolean unlock(Long id);

    Boolean lock(Long id);

    Boolean resetPasswd(UserResetPasswdVo vo);

    List<UserEntity> list();

    Boolean updById(UserEntity entity);

    ThreadLocal<UserEntity> getThreadLocal();

}
