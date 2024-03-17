package org.xiangqian.auto.deploy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 服务器信息
 *
 * @author xiangqian
 * @date 20:56 2024/03/03
 */
@Data
@TableName("server")
public class ServerEntity {

    private static final long serialVersionUID = 1L;

    // id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 名称
    @TableField("`name`")
    private String name;

    // 主机
    private String host;

    // 端口
    private Integer port;

    // 用户
    private String user;

    // 密码
    private String passwd;

    // 创建时间（时间戳，单位s）
    private Long addTime;

    // 修改时间（时间戳，单位s）
    private Long updTime;

}
