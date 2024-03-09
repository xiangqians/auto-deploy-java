package org.xiangqian.auto.deploy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xiangqian.auto.deploy.entity.RecordEntity;
import org.xiangqian.auto.deploy.util.List;

/**
 * @author xiangqian
 * @date 22:50 2024/03/04
 */
@Mapper
public interface RecordMapper extends BaseMapper<RecordEntity> {

    List<RecordEntity> list(List list, @Param("itemId") Long itemId);

}
