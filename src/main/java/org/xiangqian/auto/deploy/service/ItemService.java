package org.xiangqian.auto.deploy.service;

import org.xiangqian.auto.deploy.entity.ItemEntity;

import java.util.List;

/**
 * @author xiangqian
 * @date 21:38 2024/03/03
 */
public interface ItemService {

    Boolean delById(Long id);

    Boolean updById(ItemEntity vo);

    ItemEntity getById(Long id);

    Boolean add(ItemEntity vo);

    List<ItemEntity> list();

}