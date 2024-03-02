package org.xiangqian.auto.deploy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.xiangqian.auto.deploy.service.UserService;
import org.xiangqian.auto.deploy.util.AttributeName;

/**
 * @author xiangqian
 * @date 17:09 2024/03/02
 */
@Controller
@RequestMapping("/user")
public class UserController extends AbsController {

    @Autowired
    private UserService service;

    @GetMapping("/list")
//    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView list(ModelAndView modelAndView) {
        modelAndView.addObject(AttributeName.USERS, service.list());
        modelAndView.setViewName("user/list");
        return modelAndView;
    }

}
