package org.xiangqian.auto.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.xiangqian.auto.deploy.entity.UserEntity;
import org.xiangqian.auto.deploy.mapper.UserMapper;
import org.xiangqian.auto.deploy.service.UserService;

import java.util.Collections;

/**
 * @author xiangqian
 * @date 17:06 2024/02/29
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = mapper.selectOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getName, username));
        if (entity == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(entity.getName(), // 用户名
                entity.getPasswd(), // 用户密码
                true, // 用户账号是否可用
                true, // 用户账号是否未过期
                true, // 用户凭证（密码）是否未过期
                entity.getLock() == 0, // 用户账号是否未被锁定
                Collections.emptyList()); // 用户拥有的权限
    }

}
