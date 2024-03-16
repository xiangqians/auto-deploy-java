package org.xiangqian.auto.deploy.service;

import org.xiangqian.auto.deploy.entity.GitEntity;
import org.xiangqian.auto.deploy.vo.GitAddVo;
import org.xiangqian.auto.deploy.vo.GitUpdVo;

import java.util.List;

/**
 * @author xiangqian
 * @date 18:31 2024/03/03
 */
public interface GitService {

    Boolean delById(Long id);

    Boolean updById(GitUpdVo vo);

    GitEntity getById(Long id);

    Boolean add(GitAddVo vo);

    List<GitEntity> list();

}
