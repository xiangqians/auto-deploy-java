package org.xiangqian.auto.deploy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author xiangqian
 * @date 17:09 2024/03/02
 */
@Controller
@RequestMapping("/user")
public class UserController extends AbsController {

    @GetMapping("/list")
//    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView list(ModelAndView modelAndView) {
        modelAndView.setViewName("user/list");
        return modelAndView;
    }

}
