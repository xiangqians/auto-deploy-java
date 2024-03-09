package org.xiangqian.auto.deploy.service;

import org.xiangqian.auto.deploy.entity.RecordEntity;
import org.xiangqian.auto.deploy.util.List;

/**
 * @author xiangqian
 * @date 22:49 2024/03/04
 */
public interface RecordService {

    List<RecordEntity> list(List list, Long itemId);

}
