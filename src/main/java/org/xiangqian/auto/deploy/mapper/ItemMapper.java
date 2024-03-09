package org.xiangqian.auto.deploy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xiangqian.auto.deploy.entity.ItemEntity;
import org.xiangqian.auto.deploy.entity.RecordEntity;

import java.util.List;

/**
 * @author xiangqian
 * @date 21:39 2024/03/03
 */
@Mapper
public interface ItemMapper extends BaseMapper<ItemEntity> {

    org.xiangqian.auto.deploy.util.List<RecordEntity> recordList(org.xiangqian.auto.deploy.util.List list, @Param("itemId") Long itemId);

    List<ItemEntity> list();

}
