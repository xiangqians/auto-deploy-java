package org.xiangqian.auto.deploy.util;

import jakarta.servlet.http.HttpSession;

/**
 * @author xiangqian
 * @date 17:33 2024/03/03
 */
public class SessionUtil {

    public static void delError(HttpSession session) {
        session.removeAttribute(AttributeName.ERROR);
    }

    public static Object getError(HttpSession session) {
        return session.getAttribute(AttributeName.ERROR);
    }

    public static void setError(HttpSession session, Object error) {
        session.setAttribute(AttributeName.ERROR, error);
    }

}
