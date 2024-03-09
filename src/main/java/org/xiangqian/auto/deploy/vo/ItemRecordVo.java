package org.xiangqian.auto.deploy.vo;

import lombok.Data;
import org.xiangqian.auto.deploy.entity.RecordEntity;

/**
 * @author xiangqian
 * @date 21:40 2024/03/04
 */
@Data
public class ItemRecordVo extends RecordEntity {

    // 仓库地址
    private String repoUrl;

    // 分支名
    private String branch;

}
