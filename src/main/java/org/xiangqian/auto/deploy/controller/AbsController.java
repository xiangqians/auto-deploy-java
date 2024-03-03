package org.xiangqian.auto.deploy.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.xiangqian.auto.deploy.util.AttributeName;
import org.xiangqian.auto.deploy.util.DateUtil;
import org.xiangqian.auto.deploy.util.SecurityUtil;
import org.xiangqian.auto.deploy.util.SessionUtil;

import java.time.LocalDateTime;

/**
 * @author xiangqian
 * @date 17:27 2024/03/02
 */
public abstract class AbsController {

    // 在每个请求之前设置ModelAndView值
    @ModelAttribute
    public void modelAttribute(ModelAndView modelAndView, HttpServletRequest request, HttpSession session) {
        boolean isLoggedin = SecurityUtil.isLoggedin();
        modelAndView.addObject("isLoggedin", isLoggedin);
        if (isLoggedin) {
            modelAndView.addObject("user", SecurityUtil.getUser());
        }
        modelAndView.addObject("servletPath", request.getServletPath());
        modelAndView.addObject("timestamp", DateUtil.toSecond(LocalDateTime.now()));

        Object error = SessionUtil.getError(session);
        if (error != null) {
            setError(modelAndView, error);
            SessionUtil.delError(session);
        }
    }

    protected final void setError(ModelAndView modelAndView, Object error) {
        modelAndView.addObject(AttributeName.ERROR, error);
    }

}
