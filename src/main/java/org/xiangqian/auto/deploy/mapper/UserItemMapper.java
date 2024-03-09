package org.xiangqian.auto.deploy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xiangqian.auto.deploy.entity.UserItemEntity;

import java.util.List;

/**
 * @author xiangqian
 * @date 23:30 2024/03/04
 */
@Mapper
public interface UserItemMapper extends BaseMapper<UserItemEntity> {

    List<UserItemEntity> list(@Param("userId") Long userId);

}
