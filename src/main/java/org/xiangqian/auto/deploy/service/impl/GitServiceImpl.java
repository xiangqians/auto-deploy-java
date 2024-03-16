package org.xiangqian.auto.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.xiangqian.auto.deploy.entity.GitEntity;
import org.xiangqian.auto.deploy.mapper.GitMapper;
import org.xiangqian.auto.deploy.service.GitService;
import org.xiangqian.auto.deploy.util.DateUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xiangqian
 * @date 18:31 2024/03/03
 */
@Service
public class GitServiceImpl implements GitService {

    @Autowired
    private GitMapper mapper;

    @Override
    public Boolean delById(Long id) {
        Assert.notNull(id, "Git id不能为空");
        return mapper.deleteById(id) > 0;
    }

    @Override
    public Boolean updById(GitEntity vo) {
        Assert.notNull(vo.getId(), "Git id不能为空");
        return addOrUpd(vo);
    }

    @Override
    public GitEntity getById(Long id) {
        Assert.notNull(id, "Git id不能为空");
        return mapper.selectById(id);
    }

    @Override
    public Boolean add(GitEntity vo) {
        vo.setId(null);
        return addOrUpd(vo);
    }

    @Override
    public List<GitEntity> list() {
        return mapper.selectList(new LambdaQueryWrapper<GitEntity>());
    }

    private boolean addOrUpd(GitEntity vo) {
        Long id = vo.getId();

        String name = StringUtils.trim(vo.getName());
        Assert.isTrue(StringUtils.isNotEmpty(name), "名称不能为空");

        String user = StringUtils.trim(vo.getUser());
        Assert.isTrue(StringUtils.isNotEmpty(user), "用户不能为空");

        String passwd = StringUtils.trim(vo.getPasswd());
        Assert.isTrue(StringUtils.isNotEmpty(passwd), "密码不能为空");

        GitEntity entity = new GitEntity();
        entity.setName(name);
        entity.setUser(user);
        entity.setPasswd(passwd);
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
