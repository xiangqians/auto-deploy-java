package org.xiangqian.auto.deploy.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author xiangqian
 * @date 17:33 2024/03/03
 */
public class SessionUtil {

    public static void delVo() {
        HttpSession session = getSession();
        session.removeAttribute(AttributeName.VO);
    }

    public static Object getVo() {
        HttpSession session = getSession();
        return session.getAttribute(AttributeName.VO);
    }

    public static void setVo(Object vo) {
        HttpSession session = getSession();
        session.setAttribute(AttributeName.VO, vo);
    }

    public static void delError() {
        HttpSession session = getSession();
        session.removeAttribute(AttributeName.ERROR);
    }

    public static Object getError() {
        HttpSession session = getSession();
        return session.getAttribute(AttributeName.ERROR);
    }

    public static void setError(Object error) {
        HttpSession session = getSession();
        session.setAttribute(AttributeName.ERROR, error);
    }

    private static HttpSession getSession() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return request.getSession();
    }

}
