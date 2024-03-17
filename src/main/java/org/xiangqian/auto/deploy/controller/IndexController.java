package org.xiangqian.auto.deploy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.xiangqian.auto.deploy.service.IndexService;

/**
 * @author xiangqian
 * @date 21:54 2024/02/29
 */
@Slf4j
@Controller
@RequestMapping("/")
public class IndexController extends AbsController {

    @Autowired
    private IndexService service;

    @RequestMapping
    public ModelAndView index(ModelAndView modelAndView) {
        try {
            setVosAttribute(modelAndView, service.list());
        } catch (Exception e) {
            log.error("", e);
            setErrorAttribute(modelAndView, e.getMessage());
        }
        modelAndView.setViewName("index");
        return modelAndView;
    }

}
