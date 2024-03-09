package org.xiangqian.auto.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiangqian.auto.deploy.entity.UserEntity;
import org.xiangqian.auto.deploy.entity.UserItemEntity;
import org.xiangqian.auto.deploy.mapper.IndexMapper;
import org.xiangqian.auto.deploy.mapper.UserItemMapper;
import org.xiangqian.auto.deploy.service.IndexService;
import org.xiangqian.auto.deploy.util.SecurityUtil;
import org.xiangqian.auto.deploy.vo.ItemRecordVo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiangqian
 * @date 21:35 2024/03/04
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private IndexMapper mapper;

    @Autowired
    private UserItemMapper userItemMapper;

    @Override
    public List<ItemRecordVo> list() {
        UserEntity user = SecurityUtil.getUser();
        if (user.isAdminRole()) {
            return mapper.list(null);
        }

        List<UserItemEntity> userItems = userItemMapper.selectList(new LambdaQueryWrapper<UserItemEntity>()
                .select(UserItemEntity::getItemId)
                .eq(UserItemEntity::getUserId, user.getId()));
        if (CollectionUtils.isEmpty(userItems)) {
            return Collections.emptyList();
        }
        return mapper.list(userItems.stream().map(UserItemEntity::getItemId).collect(Collectors.toSet()));
    }

}
