package org.xiangqian.auto.deploy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.xiangqian.auto.deploy.service.GitService;
import org.xiangqian.auto.deploy.util.AttributeName;

/**
 * @author xiangqian
 * @date 18:30 2024/03/03
 */
@Controller
@RequestMapping("/git")
@PreAuthorize("hasRole('ADMIN')")
public class GitController extends AbsController {

    @Autowired
    private GitService service;

    @GetMapping("/list")
    public ModelAndView list(ModelAndView modelAndView) {
        modelAndView.addObject(AttributeName.VOS, service.list());
        modelAndView.setViewName("git/list");
        return modelAndView;
    }

}
