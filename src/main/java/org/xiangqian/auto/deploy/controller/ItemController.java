package org.xiangqian.auto.deploy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.xiangqian.auto.deploy.entity.ItemEntity;
import org.xiangqian.auto.deploy.service.GitService;
import org.xiangqian.auto.deploy.service.ItemService;
import org.xiangqian.auto.deploy.service.ServerService;
import org.xiangqian.auto.deploy.util.AttributeName;
import org.xiangqian.auto.deploy.util.DateUtil;
import org.xiangqian.auto.deploy.util.SessionUtil;

import java.time.LocalDateTime;

/**
 * @author xiangqian
 * @date 21:46 2024/03/03
 */
@Slf4j
@Controller
@RequestMapping("/item")
@PreAuthorize("hasRole('ADMIN')")
public class ItemController extends AbsController {

    @Autowired
    private ItemService service;

    @Autowired
    private GitService gitService;

    @Autowired
    private ServerService serverService;

    @DeleteMapping("/{id}")
    public RedirectView delById(@PathVariable Long id) {
        service.delById(id);
        return redirectList();
    }

    @PutMapping
    public RedirectView updById(ItemEntity vo) {
        try {
            service.updById(vo);
        } catch (Exception e) {
            log.error("", e);
            SessionUtil.setVo(vo);
            SessionUtil.setError(e.getMessage());
            return new RedirectView("/item/" + vo.getId() + "?error&t=" + DateUtil.toSecond(LocalDateTime.now()));
        }
        return redirectList();
    }

    @GetMapping("/{id}")
    public ModelAndView updById(ModelAndView modelAndView, @PathVariable Long id) {
        modelAndView.addObject(AttributeName.VO, service.getById(id));
        modelAndView.addObject("gitVos", gitService.list());
        modelAndView.addObject("serverVos", serverService.list());
        modelAndView.setViewName("item/addOrUpd");
        return modelAndView;
    }

    @PostMapping
    public RedirectView add(ItemEntity vo) {
        try {
            service.add(vo);
        } catch (Exception e) {
            log.error("", e);
            SessionUtil.setVo(vo);
            SessionUtil.setError(e.getMessage());
            return new RedirectView("/item/add?error&t=" + DateUtil.toSecond(LocalDateTime.now()));
        }
        return redirectList();
    }

    @GetMapping("/add")
    public ModelAndView add(ModelAndView modelAndView) {
        modelAndView.addObject("gitVos", gitService.list());
        modelAndView.addObject("serverVos", serverService.list());
        modelAndView.setViewName("item/addOrUpd");
        return modelAndView;
    }

    @GetMapping("/list")
    public ModelAndView list(ModelAndView modelAndView) {
        modelAndView.addObject(AttributeName.VOS, service.list());
        modelAndView.setViewName("item/list");
        return modelAndView;
    }

    private RedirectView redirectList() {
        return new RedirectView("/item/list?t=" + DateUtil.toSecond(LocalDateTime.now()));
    }

}
