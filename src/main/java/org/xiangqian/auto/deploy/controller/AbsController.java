package org.xiangqian.auto.deploy.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.xiangqian.auto.deploy.util.AttributeName;
import org.xiangqian.auto.deploy.util.DateUtil;
import org.xiangqian.auto.deploy.util.SecurityUtil;

import java.time.LocalDateTime;

/**
 * @author xiangqian
 * @date 17:27 2024/03/02
 */
public abstract class AbsController {

    // 在每个请求之前设置ModelAndView值
    @ModelAttribute
    public void modelAttribute(ModelAndView modelAndView, HttpServletRequest request) {
        boolean isLoggedin = SecurityUtil.isLoggedin();
        modelAndView.addObject(AttributeName.IS_LOGGEDIN, isLoggedin);
        if (isLoggedin) {
            modelAndView.addObject(AttributeName.USER, SecurityUtil.getUser());
        }
        modelAndView.addObject(AttributeName.SERVLET_PATH, request.getServletPath());
        modelAndView.addObject(AttributeName.TIMESTAMP, DateUtil.toSecond(LocalDateTime.now()));
    }

}
