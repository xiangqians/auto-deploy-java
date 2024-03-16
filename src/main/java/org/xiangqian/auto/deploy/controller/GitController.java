package org.xiangqian.auto.deploy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.xiangqian.auto.deploy.entity.GitEntity;
import org.xiangqian.auto.deploy.service.GitService;
import org.xiangqian.auto.deploy.util.DateUtil;

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
        Object error = null;
        try {
            service.delById(id);
        } catch (Exception e) {
            log.error("", e);
            error = e.getMessage();
        }
        return redirectListView(error);
    }

    @PutMapping("/{id}")
    public RedirectView updById(@PathVariable Long id, GitEntity vo) {
        try {
            vo.setId(id);
            service.updById(vo);
        } catch (Exception e) {
            log.error("", e);
            return redirectView("/git/" + vo.getId() + "?t=" + DateUtil.toSecond(LocalDateTime.now()), vo, null, e.getMessage());
        }
        return redirectListView(null);
    }

    @GetMapping("/{id}")
    public Object updById(ModelAndView modelAndView, @PathVariable Long id) {
        try {
            Object vo = getVoAttribute(modelAndView);
            if (vo == null) {
                GitEntity entity = service.getById(id);
                Assert.notNull(entity, "Git信息不存在");
                setVoAttribute(modelAndView, entity);
            }
        } catch (Exception e) {
            log.error("", e);
            return redirectListView(e.getMessage());
        }
        modelAndView.setViewName("git/addOrUpd");
        return modelAndView;
    }

    @PostMapping
    public RedirectView add(GitEntity vo) {
        try {
            service.add(vo);
        } catch (Exception e) {
            log.error("", e);
            return redirectView("/git?t=" + DateUtil.toSecond(LocalDateTime.now()), vo, null, e.getMessage());
        }
        return redirectListView(null);
    }

    @GetMapping
    public ModelAndView add(ModelAndView modelAndView) {
        modelAndView.setViewName("git/addOrUpd");
        return modelAndView;
    }

    @GetMapping("/list")
    public ModelAndView list(ModelAndView modelAndView) {
        try {
            setVosAttribute(modelAndView, service.list());
        } catch (Exception e) {
            log.error("", e);
            setErrorAttribute(modelAndView, e.getMessage());
        }
        modelAndView.setViewName("git/list");
        return modelAndView;
    }

    private RedirectView redirectListView(Object error) {
        return redirectView("/git/list?t=" + DateUtil.toSecond(LocalDateTime.now()), null, null, error);
    }

}
