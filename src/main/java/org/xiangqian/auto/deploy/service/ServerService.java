package org.xiangqian.auto.deploy.service;

import org.xiangqian.auto.deploy.entity.ServerEntity;

import java.util.List;

/**
 * @author xiangqian
 * @date 21:01 2024/03/03
 */
public interface ServerService {

    Boolean delById(Long id);

    Boolean updById(ServerEntity vo);

    ServerEntity getById(Long id);

    Boolean add(ServerEntity vo);

    List<ServerEntity> list();

}
