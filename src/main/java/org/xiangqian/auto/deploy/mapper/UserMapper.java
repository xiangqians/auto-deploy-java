package org.xiangqian.auto.deploy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xiangqian.auto.deploy.entity.UserEntity;

import java.util.List;

/**
 * @author xiangqian
 * @date 21:41 2024/02/29
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    List<UserEntity> list();

}