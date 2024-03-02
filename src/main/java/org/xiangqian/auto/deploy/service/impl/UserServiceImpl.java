package org.xiangqian.auto.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.xiangqian.auto.deploy.entity.UserEntity;
import org.xiangqian.auto.deploy.mapper.UserMapper;
import org.xiangqian.auto.deploy.service.UserService;
import org.xiangqian.auto.deploy.util.DateUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xiangqian
 * @date 17:06 2024/02/29
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;

    @Getter
    private ThreadLocal<UserEntity> threadLocal = new ThreadLocal<>();

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity entity = mapper.selectOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getName, userName));
        if (entity == null) {
            throw new UsernameNotFoundException(userName);
        }

        threadLocal.set(entity);

        // 未被锁定
        if (entity.getLocked() == 0) {
            // 未被限时锁定
            if (entity.isNonLimitedTimeLocked()) {
                if (entity.getTryCount() >= 3) {
                    UserEntity updEntity = new UserEntity();
                    updEntity.setId(entity.getId());
                    updEntity.setTryCount(0);
                    updEntity.setUpdTime(DateUtil.toSecond(LocalDateTime.now()));
                    mapper.updateById(updEntity);

                    entity.setTryCount(updEntity.getTryCount());
                    entity.setUpdTime(updEntity.getUpdTime());
                }
            } else {
                entity.setLocked(1);
            }
        }

        return entity;
    }

    @Override
    public List<UserEntity> list() {
        return mapper.selectList(new LambdaQueryWrapper<UserEntity>());
    }

    @Override
    public Boolean updById(UserEntity entity) {
        return mapper.updateById(entity) > 0;
    }

}
