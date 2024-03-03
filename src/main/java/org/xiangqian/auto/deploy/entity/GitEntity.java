package org.xiangqian.auto.deploy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xiangqian
 * @date 18:26 2024/03/03
 */
@Data
@TableName("git")
public class GitEntity {

    private static final long serialVersionUID = 1L;

    // id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 名称
    @TableField("`name`")
    private String name;

    // 授权类型：1-用户名和密码，2-key
    private Integer type;

    // 用户
    private String user;

    // 密码
    private String passwd;

    // key
    @TableField("`key`")
    private String key;

    // 创建时间（时间戳，s）
    private Long addTime;

    // 修改时间（时间戳，单位s）
    private Long updTime;

}
