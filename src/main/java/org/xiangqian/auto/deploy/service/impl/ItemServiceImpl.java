package org.xiangqian.auto.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.xiangqian.auto.deploy.entity.ItemEntity;
import org.xiangqian.auto.deploy.mapper.ItemMapper;
import org.xiangqian.auto.deploy.service.ItemService;
import org.xiangqian.auto.deploy.util.DateUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xiangqian
 * @date 21:38 2024/03/03
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper mapper;

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

        String repoUrl = StringUtils.trim(vo.getRepoUrl());
        Assert.isTrue(StringUtils.isNotEmpty(repoUrl), "仓库地址不能为空");

        String branch = StringUtils.trim(vo.getBranch());
        Assert.isTrue(StringUtils.isNotEmpty(branch), "分支名不能为空");

        Long serverId = vo.getServerId();
        Assert.notNull(serverId, "服务器id不能为空");
        Assert.isTrue(serverId > 0, "服务器id不能小于0");

        String script = StringUtils.trim(vo.getScript());
        Assert.isTrue(StringUtils.isNotEmpty(script), "自动部署脚本不能为空");

        String secret = StringUtils.trim(vo.getSecret());
        Assert.isTrue(StringUtils.isNotEmpty(secret), "Webhook密钥不能为空");

        ItemEntity entity = new ItemEntity();
        entity.setName(name);
        entity.setGitId(gitId);
        entity.setRepoUrl(repoUrl);
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