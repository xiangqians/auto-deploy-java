package org.xiangqian.auto.deploy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiangqian.auto.deploy.entity.RecordEntity;
import org.xiangqian.auto.deploy.mapper.RecordMapper;
import org.xiangqian.auto.deploy.service.RecordService;
import org.xiangqian.auto.deploy.util.List;

/**
 * @author xiangqian
 * @date 22:49 2024/03/04
 */
@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordMapper mapper;

    @Override
    public List<RecordEntity> list(List list, Long itemId) {
        return mapper.list(list, itemId);
    }

}
