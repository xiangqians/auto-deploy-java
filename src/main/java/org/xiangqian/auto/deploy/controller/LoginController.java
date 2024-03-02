package org.xiangqian.auto.deploy.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.xiangqian.auto.deploy.util.AttributeName;

import java.util.Optional;

/**
 * @author xiangqian
 * @date 17:16 2024/02/29
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public ModelAndView login(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        String loginError = Optional.ofNullable(session.getAttribute(AttributeName.LOGIN_ERROR)).map(Object::toString).orElse(null);
        if (loginError != null) {
            session.removeAttribute(AttributeName.LOGIN_ERROR);
            modelAndView.addObject(AttributeName.LOGIN_ERROR, loginError);
        }
        modelAndView.setViewName("login");
        return modelAndView;
    }

}
