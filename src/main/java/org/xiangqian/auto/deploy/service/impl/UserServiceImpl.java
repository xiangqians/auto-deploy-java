package org.xiangqian.auto.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.xiangqian.auto.deploy.entity.UserEntity;
import org.xiangqian.auto.deploy.mapper.UserMapper;
import org.xiangqian.auto.deploy.service.UserService;

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
        return entity;
    }

}
