package org.xiangqian.auto.deploy.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.xiangqian.auto.deploy.entity.UserEntity;
import org.xiangqian.auto.deploy.entity.UserItemEntity;

import java.util.List;

/**
 * @author xiangqian
 * @date 21:06 2024/02/29
 */
public interface UserService extends UserDetailsService {

    Boolean delItem(UserItemEntity vo);

    Boolean addItem(UserItemEntity vo);

    List<UserItemEntity> itemList(Long userId);

    Boolean updCurrent(UserEntity vo);

    Boolean updById(UserEntity vo);

    UserEntity getById(Long id);

    Boolean delById(Long id);

    Boolean unlock(Long id);

    Boolean lock(Long id);

    Boolean resetPasswd(UserEntity vo);

    Boolean add(UserEntity vo);

    List<UserEntity> list();

    void setThreadBinding(UserEntity entity);

    UserEntity getThreadBinding();

    void delThreadBinding();

    UserEntity getAndDelThreadBinding();

}
