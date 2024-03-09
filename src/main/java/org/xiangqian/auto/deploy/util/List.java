package org.xiangqian.auto.deploy.util;

import lombok.Data;

/**
 * @author xiangqian
 * @date 22:33 2024/03/04
 */
@Data
public class List<T> {
    // 索引值
    private Integer offset;

    // 行数
    private Integer rows;

    // 数据列表
    private java.util.List<T> data;

    // 索引集
    private java.util.List<Integer> offsets;

    public List() {
        this(1, 10);
    }

    public List(Integer offset, Integer rows) {
        this.offset = offset;
        this.rows = rows;
    }
}
