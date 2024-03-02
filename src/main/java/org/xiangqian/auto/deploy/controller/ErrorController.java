package org.xiangqian.auto.deploy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author xiangqian
 * @date 18:09 2024/03/02
 */
@Controller
@RequestMapping("/error")
public class ErrorController extends AbsController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping
    public ModelAndView error(ModelAndView modelAndView) {
        modelAndView.setViewName("error");
        return modelAndView;
    }

}
