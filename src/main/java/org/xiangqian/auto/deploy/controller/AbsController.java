package org.xiangqian.auto.deploy.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.xiangqian.auto.deploy.util.DateUtil;
import org.xiangqian.auto.deploy.util.JvmUtil;
import org.xiangqian.auto.deploy.util.OsUtil;

import java.time.LocalDateTime;

/**
 * @author xiangqian
 * @date 17:27 2024/03/02
 */
public abstract class AbsController {

    // request域
    public static final String SERVLET_PATH = "servletPath";
    public static final String TIMESTAMP = "timestamp";

    // session域
    // 是否已登陆
    public static final String IS_LOGGEDIN = "isLoggedin";
    // 是否是管理员角色
    public static final String IS_ADMIN_ROLE = "isAdminRole";

    // session域
    public static final String USER = "user";

    // request域
    public static final String OS = "os";
    public static final String JVM = "jvm";

    // request域
    public static final String VO = "vo";
    public static final String VOS = "vos";
    public static final String ERROR = "error";

    // 在每个请求之前设置ModelAndView值
    @ModelAttribute
    public void modelAttribute(ModelAndView modelAndView, HttpServletRequest request, HttpSession session) {
        modelAndView.addObject(SERVLET_PATH, request.getServletPath());
        modelAndView.addObject(TIMESTAMP, DateUtil.toSecond(LocalDateTime.now()));

        if (getLoggedinAttribute(session)) {
            modelAndView.addObject(OS, OsUtil.getOsInfo());
            modelAndView.addObject(JVM, JvmUtil.getJvmInfo());
        }

        Object vo = session.getAttribute(VO);
        if (vo != null) {
            session.removeAttribute(VO);
            modelAndView.addObject(VO, vo);
        }

        Object vos = session.getAttribute(VOS);
        if (vos != null) {
            session.removeAttribute(VOS);
            modelAndView.addObject(VOS, vos);
        }

        Object error = session.getAttribute(ERROR);
        if (error != null) {
            session.removeAttribute(ERROR);
            modelAndView.addObject(ERROR, error);
        }
    }

    public static boolean getLoggedinAttribute(HttpSession session) {
        Object loggedin = session.getAttribute(IS_LOGGEDIN);
        if (loggedin == null) {
            return false;
        }
        return (boolean) loggedin;
    }

    public static void setLoggedinAttribute(HttpSession session, boolean loggedin) {
        session.setAttribute(IS_LOGGEDIN, loggedin);
    }

    public static void setAdminRoleAttribute(HttpSession session, boolean adminRole) {
        session.setAttribute(IS_ADMIN_ROLE, adminRole);
    }

    public static void setUserAttribute(HttpSession session, Object user) {
        session.setAttribute(USER, user);
    }

    protected void setVoAttribute(ModelAndView modelAndView, Object value) {
        modelAndView.addObject(VO, value);
    }

    public static void setVoAttribute(HttpSession session, Object value) {
        session.setAttribute(VO, value);
    }

    protected void setVosAttribute(ModelAndView modelAndView, Object value) {
        modelAndView.addObject(VOS, value);
    }

    public static void setVosAttribute(HttpSession session, Object value) {
        session.setAttribute(VOS, value);
    }

    protected void setErrorAttribute(ModelAndView modelAndView, Object value) {
        modelAndView.addObject(ERROR, value);
    }

    public static void setErrorAttribute(HttpSession session, Object value) {
        session.setAttribute(ERROR, value);
    }

    protected RedirectView redirectView(String url, Object vo, Object vos, Object error) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        setVoAttribute(session, vo);
        setVosAttribute(session, vos);
        setErrorAttribute(session, error);
        return new RedirectView(url);
    }

}
