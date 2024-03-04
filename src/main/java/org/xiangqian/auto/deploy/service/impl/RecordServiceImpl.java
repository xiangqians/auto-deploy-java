package org.xiangqian.auto.deploy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiangqian.auto.deploy.mapper.RecordMapper;
import org.xiangqian.auto.deploy.service.RecordService;

/**
 * @author xiangqian
 * @date 22:49 2024/03/04
 */
@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordMapper mapper;


}
