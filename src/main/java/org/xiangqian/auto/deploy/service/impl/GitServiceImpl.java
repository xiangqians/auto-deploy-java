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
        check(vo);

        GitEntity addEntity = new GitEntity();
        addEntity.setId(vo.getId());
        addEntity.setName(vo.getName());
        addEntity.setType(vo.getType());
        addEntity.setUser(vo.getUser());
        addEntity.setPasswd(vo.getPasswd());
        addEntity.setKey(vo.getKey());
        addEntity.setUpdTime(DateUtil.toSecond(LocalDateTime.now()));
        return mapper.updateById(addEntity) > 0;
    }

    @Override
    public GitEntity getById(Long id) {
        Assert.notNull(id, "Git id不能为空");
        return mapper.selectById(id);
    }

    @Override
    public Boolean add(GitEntity vo) {
        check(vo);

        GitEntity addEntity = new GitEntity();
        addEntity.setName(vo.getName());
        addEntity.setType(vo.getType());
        addEntity.setUser(vo.getUser());
        addEntity.setPasswd(vo.getPasswd());
        addEntity.setKey(vo.getKey());
        addEntity.setAddTime(DateUtil.toSecond(LocalDateTime.now()));
        return mapper.insert(addEntity) > 0;
    }

    @Override
    public List<GitEntity> list() {
        return mapper.selectList(new LambdaQueryWrapper<GitEntity>());
    }

    private void check(GitEntity vo) {
        String name = StringUtils.trim(vo.getName());
        Assert.isTrue(StringUtils.isNotEmpty(name), "名称不能为空");
        vo.setName(name);

        Integer type = vo.getType();
        Assert.notNull(type, "授权类型不能为空");
        Assert.isTrue(type == 1 || type == 2, "授权类型错误");

        // 用户名和密码
        if (type == 1) {
            String user = StringUtils.trim(vo.getUser());
            Assert.isTrue(StringUtils.isNotEmpty(user), "用户不能为空");

            String passwd = StringUtils.trim(vo.getPasswd());
            Assert.isTrue(StringUtils.isNotEmpty(passwd), "密码不能为空");

            vo.setUser(user);
            vo.setPasswd(passwd);
            vo.setKey(null);
        }
        // key
        else if (type == 2) {
            String key = StringUtils.trim(vo.getKey());
            Assert.isTrue(StringUtils.isNotEmpty(key), "key不能为空");

            vo.setUser(null);
            vo.setPasswd(null);
            vo.setKey(key);
        }
    }

}
