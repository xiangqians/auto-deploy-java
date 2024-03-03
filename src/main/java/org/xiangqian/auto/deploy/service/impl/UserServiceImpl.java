package org.xiangqian.auto.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.xiangqian.auto.deploy.entity.UserEntity;
import org.xiangqian.auto.deploy.mapper.UserMapper;
import org.xiangqian.auto.deploy.service.UserService;
import org.xiangqian.auto.deploy.util.DateUtil;
import org.xiangqian.auto.deploy.vo.user.UserAddVo;
import org.xiangqian.auto.deploy.vo.user.UserResetPasswdVo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xiangqian
 * @date 17:06 2024/02/29
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SessionRegistry sessionRegistry;

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
                    mapper.updateById(updEntity);
                    entity.setTryCount(updEntity.getTryCount());
                }
            } else {
                entity.setLocked(1);
            }
        }

        return entity;
    }

    @Override
    public Boolean add(UserAddVo vo) {
        String name = StringUtils.trim(vo.getName());
        String nickname = StringUtils.trim(vo.getNickname());
        String passwd = StringUtils.trim(vo.getPasswd());
        Assert.isTrue(StringUtils.isNotEmpty(name), "用户名不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(nickname), "昵称不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(passwd), "密码不能为空");

        UserEntity entity = mapper.selectOne(new LambdaQueryWrapper<UserEntity>()
                .select(UserEntity::getId)
                .eq(UserEntity::getName, name)
                .last("LIMIT 1"));
        Assert.isNull(entity, "用户名已存在");

        UserEntity addEntity = new UserEntity();
        addEntity.setName(name);
        addEntity.setNickname(nickname);
        addEntity.setPasswd(passwordEncoder.encode(passwd));
        addEntity.setAddTime(DateUtil.toSecond(LocalDateTime.now()));

        return mapper.insert(addEntity) > 0;
    }

    @Override
    public Boolean delById(Long id) {
        Assert.notNull(id, "用户id不能为空");

        if (mapper.deleteById(id) > 0) {
            expireNow(id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean unlock(Long id) {
        Assert.notNull(id, "用户id不能为空");

        UserEntity updEntity = new UserEntity();
        updEntity.setId(id);
        updEntity.setLocked(0);
        updEntity.setTryCount(0);

        return mapper.updateById(updEntity) > 0;
    }

    @Override
    public Boolean lock(Long id) {
        Assert.notNull(id, "用户id不能为空");

        UserEntity updEntity = new UserEntity();
        updEntity.setId(id);
        updEntity.setLocked(1);

        if (mapper.updateById(updEntity) > 0) {
            expireNow(id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean resetPasswd(UserResetPasswdVo vo) {
        Long id = vo.getId();
        String passwd = vo.getPasswd();
        Assert.notNull(id, "用户id不能为空");
        Assert.notNull(passwd, "密码不能为空");

        UserEntity updEntity = new UserEntity();
        updEntity.setId(id);
        updEntity.setPasswd(passwordEncoder.encode(passwd));

        if (mapper.updateById(updEntity) > 0) {
            expireNow(id);
            return true;
        }
        return false;
    }

    @Override
    public List<UserEntity> list() {
        return mapper.selectList(new LambdaQueryWrapper<UserEntity>().ne(UserEntity::getName, "admin"));
    }

    @Override
    public Boolean updById(UserEntity entity) {
        return mapper.updateById(entity) > 0;
    }

    /**
     * 使指定用户session过期
     *
     * @param id
     */
    private void expireNow(Long id) {
        UserEntity entity = mapper.selectById(id);
        if (entity == null) {
            return;
        }

        List<SessionInformation> sessions = sessionRegistry.getAllSessions(entity,
                false); // 是否包括过期的会话
        if (CollectionUtils.isNotEmpty(sessions)) {
            sessions.forEach(SessionInformation::expireNow);
        }
    }

}
