package org.xiangqian.auto.deploy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户-项目信息
 *
 * @author xiangqian
 * @date 23:29 2024/03/04
 */
@Data
@TableName("user_item")
public class UserItemEntity {
    private static final long serialVersionUID = 1L;

    // 用户id
    private Long userId;

    // 项目id
    private Long itemId;

    // 项目名称
    @TableField(exist = false)
    private String itemName;

    // 创建时间（时间戳，s）
    private Long addTime;

}
