package org.xiangqian.auto.deploy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.xiangqian.auto.deploy.service.IndexService;

/**
 * @author xiangqian
 * @date 21:54 2024/02/29
 */
@Controller
@RequestMapping("/")
public class IndexController extends AbsController {

    @Autowired
    private IndexService service;

    @RequestMapping
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.addObject("itemRecords", service.list());
        modelAndView.setViewName("index");
        return modelAndView;
    }

}
