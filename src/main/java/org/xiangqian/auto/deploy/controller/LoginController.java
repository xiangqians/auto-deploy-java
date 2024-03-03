package org.xiangqian.auto.deploy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author xiangqian
 * @date 17:16 2024/02/29
 */
@Controller
@RequestMapping("/login")
public class LoginController extends AbsController {

    @GetMapping
    public ModelAndView login(ModelAndView modelAndView) {
        modelAndView.setViewName("login");
        return modelAndView;
    }

}
