package org.xiangqian.auto.deploy.util;

/**
 * 属性名
 *
 * @author xiangqian
 * @date 19:52 2024/03/01
 */
public interface AttributeName {

    // 是否已登陆
    String IS_LOGGEDIN = "isLoggedin";

    // 是否是管理员角色
    String IS_ADMIN_ROLE = "isAdminRole";

    // 用户
    String USER = "user";

    // 项目集
    @Deprecated
    String ITEMS = "items";

    String VO = "vo";

    @Deprecated
    String VOS = "vos";

    // 错误，-》加到vo
    @Deprecated
    String ERROR = "error";

}
