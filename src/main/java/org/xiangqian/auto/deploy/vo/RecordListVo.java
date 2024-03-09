package org.xiangqian.auto.deploy.vo;

import lombok.Data;
import org.xiangqian.auto.deploy.entity.RecordEntity;

import java.util.List;

/**
 * @author xiangqian
 * @date 23:08 2024/03/04
 */
@Data
public class RecordListVo {

    // 页
    private Long offset;

    //  LIMIT ${vo.offset}, ${vo.rows}

    // 数据
    private List<RecordEntity> data;

}
