package org.xiangqian.auto.deploy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xiangqian.auto.deploy.entity.RecordEntity;

import java.util.List;
import java.util.Set;

/**
 * @author xiangqian
 * @date 23:29 2024/03/03
 */
@Mapper
public interface IndexMapper extends BaseMapper {

    List<RecordEntity> list(@Param("itemIds") Set<Long> itemIds);

}
