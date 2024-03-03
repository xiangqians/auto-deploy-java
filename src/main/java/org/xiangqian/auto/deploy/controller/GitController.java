package org.xiangqian.auto.deploy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.xiangqian.auto.deploy.entity.GitEntity;
import org.xiangqian.auto.deploy.service.GitService;
import org.xiangqian.auto.deploy.util.AttributeName;
import org.xiangqian.auto.deploy.util.DateUtil;
import org.xiangqian.auto.deploy.util.SessionUtil;

import java.time.LocalDateTime;

/**
 * @author xiangqian
 * @date 18:30 2024/03/03
 */
@Slf4j
@Controller
@RequestMapping("/git")
@PreAuthorize("hasRole('ADMIN')")
public class GitController extends AbsController {

    @Autowired
    private GitService service;

    @DeleteMapping("/{id}")
    public RedirectView delById(@PathVariable Long id) {
        service.delById(id);
        return redirectListView();
    }

    @PutMapping
    public RedirectView updById(GitEntity vo) {
        try {
            service.updById(vo);
        } catch (Exception e) {
            log.error("", e);
            SessionUtil.setVo(vo);
            SessionUtil.setError(e.getMessage());
            return new RedirectView("/git/" + vo.getId() + "?error&t=" + DateUtil.toSecond(LocalDateTime.now()));
        }
        return redirectListView();
    }

    @GetMapping("/{id}")
    public ModelAndView updById(ModelAndView modelAndView, @PathVariable Long id) {
        modelAndView.addObject(AttributeName.VO, service.getById(id));
        modelAndView.setViewName("git/addOrUpd");
        return modelAndView;
    }

    @PostMapping
    public RedirectView add(GitEntity vo) {
        try {
            service.add(vo);
        } catch (Exception e) {
            log.error("", e);
            SessionUtil.setVo(vo);
            SessionUtil.setError(e.getMessage());
            return new RedirectView("/git/add?error&t=" + DateUtil.toSecond(LocalDateTime.now()));
        }
        return redirectListView();
    }

    @GetMapping("/add")
    public ModelAndView add(ModelAndView modelAndView) {
        modelAndView.setViewName("git/addOrUpd");
        return modelAndView;
    }

    @GetMapping("/list")
    public ModelAndView list(ModelAndView modelAndView) {
        modelAndView.addObject(AttributeName.VOS, service.list());
        modelAndView.setViewName("git/list");
        return modelAndView;
    }

    private RedirectView redirectListView() {
        return new RedirectView("/git/list?t=" + DateUtil.toSecond(LocalDateTime.now()));
    }

}
