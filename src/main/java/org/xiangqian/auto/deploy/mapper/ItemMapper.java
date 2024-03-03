package org.xiangqian.auto.deploy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xiangqian.auto.deploy.entity.ItemEntity;

import java.util.List;

/**
 * @author xiangqian
 * @date 21:39 2024/03/03
 */
@Mapper
public interface ItemMapper extends BaseMapper<ItemEntity> {

    List<ItemEntity> list();

}
