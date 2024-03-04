package org.xiangqian.auto.deploy.vo;

import lombok.Data;

/**
 * @author xiangqian
 * @date 17:12 2024/03/03
 */
@Data
public class UserAddVo {

    // 用户名
    private String name;

    // 用户昵称
    private String nickname;

    // 密码
    private String passwd;

}
