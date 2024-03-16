package org.xiangqian.auto.deploy.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.xiangqian.auto.deploy.entity.UserEntity;
import org.xiangqian.auto.deploy.entity.UserItemEntity;
import org.xiangqian.auto.deploy.vo.UserAddVo;
import org.xiangqian.auto.deploy.vo.UserItemAddVo;
import org.xiangqian.auto.deploy.vo.UserItemDelVo;
import org.xiangqian.auto.deploy.vo.UserResetPasswdVo;

import java.util.List;

/**
 * @author xiangqian
 * @date 21:06 2024/02/29
 */
public interface UserService extends UserDetailsService {

    Boolean delItem(UserItemDelVo vo);

    Boolean addItem(UserItemAddVo vo);

    List<UserItemEntity> itemList(Long userId);

    Boolean updById(UserEntity vo);

    UserEntity getById(Long id);

    Boolean delById(Long id);

    Boolean unlock(Long id);

    Boolean lock(Long id);

    Boolean resetPasswd(UserResetPasswdVo vo);

    Boolean add(UserAddVo vo);

    List<UserEntity> list();

    void setThreadBinding(UserEntity entity);

    UserEntity getThreadBinding();

    void delThreadBinding();

    UserEntity getAndDelThreadBinding();

}
