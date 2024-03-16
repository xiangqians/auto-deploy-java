package org.xiangqian.auto.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.xiangqian.auto.deploy.entity.ServerEntity;
import org.xiangqian.auto.deploy.mapper.ServerMapper;
import org.xiangqian.auto.deploy.service.ServerService;
import org.xiangqian.auto.deploy.util.DateUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xiangqian
 * @date 21:01 2024/03/03
 */
@Service
public class ServerServiceImpl implements ServerService {

    @Autowired
    private ServerMapper mapper;

    @Override
    public Boolean delById(Long id) {
        Assert.notNull(id, "服务器id不能为空");
        return mapper.deleteById(id) > 0;
    }

    @Override
    public Boolean updById(ServerEntity vo) {
        Assert.notNull(vo.getId(), "服务器id不能为空");
        return addOrUpd(vo);
    }

    @Override
    public ServerEntity getById(Long id) {
        Assert.notNull(id, "服务器id不能为空");
        return mapper.selectById(id);
    }

    @Override
    public Boolean add(ServerEntity vo) {
        vo.setId(null);
        return addOrUpd(vo);
    }

    @Override
    public List<ServerEntity> list() {
        return mapper.selectList(new LambdaQueryWrapper<ServerEntity>());
    }

    private boolean addOrUpd(ServerEntity vo) {
        Long id = vo.getId();

        String name = StringUtils.trim(vo.getName());
        Assert.isTrue(StringUtils.isNotEmpty(name), "名称不能为空");

        String host = StringUtils.trim(vo.getHost());
        Assert.isTrue(StringUtils.isNotEmpty(host), "主机不能为空");

        Integer port = vo.getPort();
        Assert.notNull(port, "端口不能为空");
        Assert.isTrue(port > 0 && port < 65535, "端口范围 (0, 65535)");

        String user = StringUtils.trim(vo.getUser());
        Assert.isTrue(StringUtils.isNotEmpty(user), "用户不能为空");

        String passwd = StringUtils.trim(vo.getPasswd());
        Assert.isTrue(StringUtils.isNotEmpty(passwd), "密码不能为空");

        ServerEntity entity = new ServerEntity();
        entity.setName(name);
        entity.setHost(host);
        entity.setPort(port);
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
