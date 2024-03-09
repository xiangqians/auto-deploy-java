package org.xiangqian.auto.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.xiangqian.auto.deploy.entity.UserItemEntity;
import org.xiangqian.auto.deploy.mapper.ItemMapper;
import org.xiangqian.auto.deploy.mapper.UserItemMapper;
import org.xiangqian.auto.deploy.mapper.UserMapper;
import org.xiangqian.auto.deploy.service.UserItemService;
import org.xiangqian.auto.deploy.util.DateUtil;
import org.xiangqian.auto.deploy.vo.UserItemAddVo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xiangqian
 * @date 23:32 2024/03/04
 */
@Service
public class UserItemServiceImpl implements UserItemService {

    @Autowired
    private UserItemMapper mapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public Boolean del(Long userId, Long itemId) {
        checkUserId(userId);
        checkItemId(itemId);
        return mapper.delete(new LambdaQueryWrapper<UserItemEntity>()
                .eq(UserItemEntity::getUserId, userId)
                .eq(UserItemEntity::getItemId, itemId)) > 0;
    }

    @Override
    public Boolean add(Long userId, UserItemAddVo vo) {
        checkUserId(userId);
        Long itemId = vo.getItemId();
        checkItemId(itemId);

        UserItemEntity addEntity = new UserItemEntity();
        addEntity.setUserId(userId);
        addEntity.setItemId(itemId);
        addEntity.setAddTime(DateUtil.toSecond(LocalDateTime.now()));
        return mapper.insert(addEntity) > 0;
    }

    @Override
    public List<UserItemEntity> list(Long userId) {
        checkUserId(userId);
        return mapper.list(userId);
    }

    private void checkItemId(Long itemId) {
        Assert.notNull(itemId, "项目id不能为空");
        Assert.notNull(itemMapper.selectById(itemId), "项目不存在");
    }

    private void checkUserId(Long userId) {
        Assert.notNull(userId, "用户id不能为空");
        Assert.notNull(userMapper.selectById(userId), "用户不存在");
    }

}
