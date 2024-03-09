package org.xiangqian.auto.deploy.service;

import org.xiangqian.auto.deploy.entity.UserItemEntity;
import org.xiangqian.auto.deploy.vo.UserItemAddVo;

import java.util.List;

/**
 * @author xiangqian
 * @date 23:32 2024/03/04
 */
public interface UserItemService {

    Boolean del(Long userId, Long itemId);

    Boolean add(Long userId, UserItemAddVo vo);

    List<UserItemEntity> list(Long userId);

}
