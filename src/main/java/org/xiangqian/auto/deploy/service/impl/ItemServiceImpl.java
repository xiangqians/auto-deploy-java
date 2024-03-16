package org.xiangqian.auto.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.xiangqian.auto.deploy.entity.ItemEntity;
import org.xiangqian.auto.deploy.entity.RecordEntity;
import org.xiangqian.auto.deploy.entity.UserEntity;
import org.xiangqian.auto.deploy.entity.UserItemEntity;
import org.xiangqian.auto.deploy.mapper.ItemMapper;
import org.xiangqian.auto.deploy.mapper.UserItemMapper;
import org.xiangqian.auto.deploy.service.ItemService;
import org.xiangqian.auto.deploy.util.DateUtil;
import org.xiangqian.auto.deploy.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author xiangqian
 * @date 21:38 2024/03/03
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper mapper;

    @Autowired
    private UserItemMapper userItemMapper;

    @Override
    public String getRecordMsg(Long itemId, Long recordId, String type) {
        Assert.notNull(itemId, "项目id不能为空");
        Assert.notNull(recordId, "记录id不能为空");
        Assert.notNull(type, "类型不能为空");

        return null;
    }

    @Override
    public org.xiangqian.auto.deploy.util.List<RecordEntity> recordList(org.xiangqian.auto.deploy.util.List list, Long itemId) {
        Assert.notNull(itemId, "项目id不能为空");
        UserEntity user = SecurityUtil.getUser();
        if (user.isAdminRole()) {
            return mapper.recordList(list, itemId);
        }

        UserItemEntity userItem = userItemMapper.selectOne(new LambdaQueryWrapper<UserItemEntity>()
                .select(UserItemEntity::getItemId)
                .eq(UserItemEntity::getUserId, user.getId())
                .eq(UserItemEntity::getItemId, itemId)
                .last("LIMIT 1"));
        if (userItem == null) {
            list.setData(Collections.emptyList());
            return list;
        }
        return mapper.recordList(list, itemId);
    }

    @Override
    public Boolean delById(Long id) {
        Assert.notNull(id, "项目id不能为空");
        return mapper.deleteById(id) > 0;
    }

    @Override
    public Boolean updById(ItemEntity vo) {
        Assert.notNull(vo.getId(), "项目id不能为空");
        return addOrUpd(vo);
    }

    @Override
    public ItemEntity getById(Long id) {
        Assert.notNull(id, "项目id不能为空");
        return mapper.selectById(id);
    }

    @Override
    public Boolean add(ItemEntity vo) {
        vo.setId(null);
        return addOrUpd(vo);
    }

    @Override
    public List<ItemEntity> list() {
        return mapper.list();
    }

    private Boolean addOrUpd(ItemEntity vo) {
        Long id = vo.getId();

        String name = StringUtils.trim(vo.getName());
        Assert.isTrue(StringUtils.isNotEmpty(name), "名称不能为空");

        Long gitId = vo.getGitId();
        Assert.notNull(gitId, "Git id不能为空");
        Assert.isTrue(gitId > 0, "Git id不能小于0");

        String uri = StringUtils.trim(vo.getUri());
        Assert.isTrue(StringUtils.isNotEmpty(uri), "Git仓库地址不能为空");

        String branch = StringUtils.trim(vo.getBranch());
        Assert.isTrue(StringUtils.isNotEmpty(branch), "Git分支名不能为空");

        Long serverId = vo.getServerId();
        Assert.notNull(serverId, "服务器id不能为空");
        Assert.isTrue(serverId > 0, "服务器id不能小于0");

        String script = StringUtils.trim(vo.getScript());
        Assert.isTrue(StringUtils.isNotEmpty(script), "自动部署脚本不能为空");

        String secret = StringUtils.trimToEmpty(vo.getSecret());

        ItemEntity entity = new ItemEntity();
        entity.setName(name);
        entity.setGitId(gitId);
        entity.setUri(uri);
        entity.setBranch(branch);
        entity.setServerId(serverId);
        entity.setScript(script);
        entity.setSecret(secret);
        if (id == null) {
            entity.setAddTime(DateUtil.toSecond(LocalDateTime.now()));
            return mapper.insert(entity) > 0;
        } else {
            entity.setId(id);
            entity.setUpdTime(DateUtil.toSecond(LocalDateTime.now()));
            return mapper.updateById(entity) > 0;
        }
    }

}
