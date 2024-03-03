package org.xiangqian.auto.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiangqian.auto.deploy.entity.GitEntity;
import org.xiangqian.auto.deploy.mapper.GitMapper;
import org.xiangqian.auto.deploy.service.GitService;

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
    public List<GitEntity> list() {
        return mapper.selectList(new LambdaQueryWrapper<GitEntity>());
    }

}
