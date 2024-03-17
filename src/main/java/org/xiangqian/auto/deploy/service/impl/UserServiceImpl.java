package org.xiangqian.auto.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.xiangqian.auto.deploy.controller.AbsController;
import org.xiangqian.auto.deploy.entity.ItemEntity;
import org.xiangqian.auto.deploy.entity.UserEntity;
import org.xiangqian.auto.deploy.entity.UserItemEntity;
import org.xiangqian.auto.deploy.mapper.ItemMapper;
import org.xiangqian.auto.deploy.mapper.UserItemMapper;
import org.xiangqian.auto.deploy.mapper.UserMapper;
import org.xiangqian.auto.deploy.service.UserService;
import org.xiangqian.auto.deploy.util.DateUtil;
import org.xiangqian.auto.deploy.util.SecurityUtil;
import org.xiangqian.auto.deploy.vo.UserItemAddVo;
import org.xiangqian.auto.deploy.vo.UserItemDelVo;
import org.xiangqian.auto.deploy.vo.UserResetPasswdVo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xiangqian
 * @date 17:06 2024/02/29
 */
@Service
public class UserServiceImpl implements UserService {

    private ThreadLocal<UserEntity> threadLocal = new ThreadLocal<>();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private UserItemMapper userItemMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity entity = mapper.selectOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getName, userName).last("LIMIT 1"));
        if (entity == null) {
            throw new UsernameNotFoundException(userName);
        }

        // 将用户信息设置到线程本地
        setThreadBinding(entity);

        // 未被锁定
        if (entity.getLocked() == 0) {
            // 未被限时锁定
            if (entity.isNonLimitedTimeLocked()) {
                // 如果尝试输入密码次数超过3次，归零
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
    public Boolean delItem(UserItemDelVo vo) {
        Long userId = vo.getUserId();
        Assert.notNull(userId, "用户id不能为空");

        Long itemId = vo.getItemId();
        Assert.notNull(itemId, "项目id不能为空");

        return userItemMapper.delete(new LambdaQueryWrapper<UserItemEntity>()
                .eq(UserItemEntity::getUserId, userId)
                .eq(UserItemEntity::getItemId, itemId)) > 0;
    }

    @Override
    public Boolean addItem(UserItemAddVo vo) {
        Long userId = vo.getUserId();
        Assert.notNull(userId, "用户id不能为空");
        Assert.notNull(mapper.selectOne(new LambdaQueryWrapper<UserEntity>().select(UserEntity::getId).eq(UserEntity::getId, userId).last("LIMIT 1")), "用户不存在");

        Long itemId = vo.getItemId();
        Assert.notNull(itemId, "项目id不能为空");
        Assert.notNull(itemMapper.selectOne(new LambdaQueryWrapper<ItemEntity>().select(ItemEntity::getId).eq(ItemEntity::getId, itemId).last("LIMIT 1")), "项目不存在");

        UserItemEntity addEntity = new UserItemEntity();
        addEntity.setUserId(userId);
        addEntity.setItemId(itemId);
        addEntity.setAddTime(DateUtil.toSecond(LocalDateTime.now()));
        return userItemMapper.insert(addEntity) > 0;
    }

    @Override
    public List<UserItemEntity> itemList(Long userId) {
        Assert.notNull(userId, "用户id不能为空");
        return userItemMapper.list(userId);
    }

    @Override
    public synchronized Boolean updCurrent(UserEntity vo) {
        UserEntity entity = SecurityUtil.getUser();
        Long id = entity.getId();
        vo.setId(id);
        if (entity.isAdminRole()) {
            vo.setName(entity.getName());
        }

        Assert.notNull(id, "用户id不能为空");

        String origPasswd = StringUtils.trim(vo.getOrigPasswd());
        Assert.isTrue(StringUtils.isNotEmpty(origPasswd), "原密码不能为空");

        String newPasswd = StringUtils.trim(vo.getNewPasswd());
        String reNewPasswd = StringUtils.trim(vo.getReNewPasswd());
        Assert.isTrue(StringUtils.equals(newPasswd, reNewPasswd), "新密码两次输入不一致");
        vo.setPasswd(newPasswd);

        Assert.isTrue(passwordEncoder.matches(origPasswd, entity.getPassword()), "原密码不正确");

        return addOrUpd(vo);
    }

    @Override
    public synchronized Boolean updById(UserEntity entity) {
        Assert.notNull(entity.getId(), "用户id不能为空");
        return mapper.updateById(entity) > 0;
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
    public UserEntity getById(Long id) {
        Assert.notNull(id, "用户id不能为空");
        return mapper.selectById(id);
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
        Assert.notNull(id, "用户id不能为空");

        String passwd = StringUtils.trim(vo.getPasswd());
        Assert.isTrue(StringUtils.isNotEmpty(passwd), "密码不能为空");

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
    public synchronized Boolean add(UserEntity vo) {
        return addOrUpd(vo);
    }

    @Override
    public List<UserEntity> list() {
        return mapper.list();
    }

    @Override
    public void setThreadBinding(UserEntity entity) {
        threadLocal.set(entity);
    }

    @Override
    public UserEntity getThreadBinding() {
        return threadLocal.get();
    }

    @Override
    public UserEntity getAndDelThreadBinding() {
        UserEntity entity = getThreadBinding();
        if (entity != null) {
            delThreadBinding();
        }
        return entity;
    }

    @Override
    public void delThreadBinding() {
        threadLocal.remove();
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
                // 是否包括过期的会话
                false);
        if (CollectionUtils.isNotEmpty(sessions)) {
            sessions.forEach(SessionInformation::expireNow);
        }
    }

    private Boolean addOrUpd(UserEntity vo) {
        Long id = vo.getId();

        String name = StringUtils.trim(vo.getName());
        Assert.isTrue(StringUtils.isNotEmpty(name), "用户名不能为空");
        Assert.isTrue(name.length() <= 60, "用户名长度不能超过60个字符");

        String nickname = StringUtils.trim(vo.getNickname());
        Assert.isTrue(StringUtils.isNotEmpty(nickname), "昵称不能为空");
        Assert.isTrue(nickname.length() <= 60, "昵称长度不能超过60个字符");

        String passwd = StringUtils.trim(vo.getPasswd());
        Assert.isTrue(StringUtils.isNotEmpty(passwd), "密码不能为空");
        Assert.isTrue(passwd.length() <= 120, "密码长度不能超过120个字符");

        UserEntity storedEntity = mapper.selectOne(new LambdaQueryWrapper<UserEntity>()
                .select(UserEntity::getId)
                .eq(UserEntity::getName, name)
                .last("LIMIT 1"));
        Assert.isTrue(storedEntity == null || storedEntity.getId().equals(id), "用户名已存在");

        UserEntity entity = new UserEntity();
        entity.setName(name);
        entity.setNickname(nickname);
        entity.setPasswd(passwordEncoder.encode(passwd));

        if (id == null) {
            entity.setAddTime(DateUtil.toSecond(LocalDateTime.now()));
            return mapper.insert(entity) > 0;
        } else {
            entity.setId(id);
            entity.setUpdTime(DateUtil.toSecond(LocalDateTime.now()));
            if (mapper.updateById(entity) > 0) {
                UserEntity user = AbsController.getUserAttribute(AbsController.getSession());
                if (user.getId().equals(id)) {
                    user.setName(entity.getName());
                    user.setNickname(entity.getNickname());
                    user.setPasswd(entity.getPasswd());
                }
                return true;
            }
            return false;
        }
    }

}
