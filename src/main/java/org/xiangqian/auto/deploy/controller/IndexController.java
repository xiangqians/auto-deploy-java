package org.xiangqian.auto.deploy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author xiangqian
 * @date 21:54 2024/02/29
 */
@Controller
@RequestMapping("/")
public class IndexController extends AbsController {

    @RequestMapping
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.setViewName("index");
        return modelAndView;
    }

}
