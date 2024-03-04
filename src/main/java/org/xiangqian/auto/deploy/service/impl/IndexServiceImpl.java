package org.xiangqian.auto.deploy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiangqian.auto.deploy.mapper.IndexMapper;
import org.xiangqian.auto.deploy.service.IndexService;
import org.xiangqian.auto.deploy.vo.IndexVo;

import java.util.List;

/**
 * @author xiangqian
 * @date 21:35 2024/03/04
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private IndexMapper mapper;

    @Override
    public List<IndexVo> list() {
        return mapper.list();
    }

}
