package org.xiangqian.auto.deploy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xiangqian.auto.deploy.vo.IndexVo;

import java.util.List;

/**
 * @author xiangqian
 * @date 23:29 2024/03/03
 */
@Mapper
public interface IndexMapper extends BaseMapper {

    List<IndexVo> list();

}
