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
        modelAndView.addObject("servletPath", request.getServletPath());
        modelAndView.addObject("timestamp", DateUtil.toSecond(LocalDateTime.now()));

        boolean isLoggedin = SecurityUtil.isLoggedin();
        modelAndView.addObject("isLoggedin", isLoggedin);
        if (isLoggedin) {
            modelAndView.addObject("user", SecurityUtil.getUser());
        }

        Object vo = SessionUtil.getVo();
        if (vo != null) {
            SessionUtil.delVo();
            modelAndView.addObject(AttributeName.VO, vo);
        }

        Object error = SessionUtil.getError();
        if (error != null) {
            SessionUtil.delError();
            modelAndView.addObject(AttributeName.ERROR, error);
        }
    }

}
