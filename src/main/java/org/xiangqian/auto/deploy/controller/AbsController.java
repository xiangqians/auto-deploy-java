package org.xiangqian.auto.deploy.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.xiangqian.auto.deploy.util.AttributeName;
import org.xiangqian.auto.deploy.util.DateUtil;

import java.time.LocalDateTime;

/**
 * @author xiangqian
 * @date 17:27 2024/03/02
 */
public abstract class AbsController {

    // 在每个请求之前设置ModelAndView值
    @ModelAttribute
    public void modelAttribute(ModelAndView modelAndView, HttpServletRequest request, HttpSession session) {
        modelAndView.addObject("servletPath", request.getServletPath());
        modelAndView.addObject("timestamp", DateUtil.toSecond(LocalDateTime.now()));

        Object vo = getVoAttribute(session);
        if (vo != null) {
            delVoAttribute(session);
            modelAndView.addObject(AttributeName.VO, vo);
        }

        Object error = getErrorAttribute(session);
        if (error != null) {
            delErrorAttribute(session);
            modelAndView.addObject(AttributeName.ERROR, error);
        }
    }

    protected final void delVoAttribute(HttpSession session) {
        session.removeAttribute(AttributeName.VO);
    }

    protected final void setVoAttribute(HttpSession session, Object value) {
        session.setAttribute(AttributeName.VO, value);
    }

    protected final Object getVoAttribute(HttpSession session) {
        return session.getAttribute(AttributeName.VO);
    }

    protected final void delErrorAttribute(HttpSession session) {
        session.removeAttribute(AttributeName.ERROR);
    }

    protected final void setErrorAttribute(HttpSession session, Object value) {
        session.setAttribute(AttributeName.ERROR, value);
    }

    protected final Object getErrorAttribute(HttpSession session) {
        return session.getAttribute(AttributeName.ERROR);
    }

}
