package org.xiangqian.auto.deploy.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.xiangqian.auto.deploy.util.SessionUtil;

import java.util.Optional;

/**
 * @author xiangqian
 * @date 17:16 2024/02/29
 */
@Controller
@RequestMapping("/login")
public class LoginController extends AbsController {

    @GetMapping
    public ModelAndView login(ModelAndView modelAndView, HttpSession session) {
        String error = Optional.ofNullable(SessionUtil.getError(session)).map(Object::toString).orElse(null);
        if (error != null) {
            SessionUtil.delError(session);
            setError(modelAndView, error);
        }
        modelAndView.setViewName("login");
        return modelAndView;
    }

}
