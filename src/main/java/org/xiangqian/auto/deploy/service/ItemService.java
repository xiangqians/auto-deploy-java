package org.xiangqian.auto.deploy.service;

import org.xiangqian.auto.deploy.entity.ItemEntity;
import org.xiangqian.auto.deploy.entity.RecordEntity;

import java.util.List;

/**
 * @author xiangqian
 * @date 21:38 2024/03/03
 */
public interface ItemService {

    String getRecordMsg(Long itemId, Long recordId, String type);

    org.xiangqian.auto.deploy.util.List<RecordEntity> recordList(org.xiangqian.auto.deploy.util.List list, Long itemId);

    Boolean delById(Long id);

    Boolean updById(ItemEntity vo);

    ItemEntity getById(Long id);

    Boolean add(ItemEntity vo);

    List<ItemEntity> list();

}
