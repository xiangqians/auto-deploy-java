package org.xiangqian.auto.deploy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xiangqian
 * @date 21:33 2024/02/29
 */
@Data
@TableName("user")
public class UserEntity {

    private static final long serialVersionUID = 1L;

    // 用户id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 用户名
    @TableField("`name`")
    private String name;

    // 昵称
    private String nickname;

    // 密码
    private String passwd;

    // 是否锁定，0-正常，1-锁定
    @TableField("`lock`")
    private Integer lock;

    // 尝试输入密码次数，超过3次账号将会被锁定
    private Integer tryCount;

    // 上一次登录ip
    private String lastLoginIp;

    // 上一次登录时间（时间戳，单位s）
    private Long lastLoginTime;

    // 当前次登录ip
    private String currentLoginIp;

    // 当前登录时间（时间戳，单位s）
    private Long currentLoginTime;

    // 创建时间（时间戳，s）
    private Long addTime;

    // 修改时间（时间戳，单位s）
    private Long updTime;

}
