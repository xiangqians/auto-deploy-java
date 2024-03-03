package org.xiangqian.auto.deploy.service;

import org.xiangqian.auto.deploy.entity.GitEntity;

import java.util.List;

/**
 * @author xiangqian
 * @date 18:31 2024/03/03
 */
public interface GitService {

    Boolean delById(Long id);

    Boolean updById(GitEntity vo);

    GitEntity getById(Long id);

    Boolean add(GitEntity vo);

    List<GitEntity> list();

}
