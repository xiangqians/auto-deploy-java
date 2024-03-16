package org.xiangqian.auto.deploy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.xiangqian.auto.deploy.entity.ServerEntity;
import org.xiangqian.auto.deploy.service.ServerService;
import org.xiangqian.auto.deploy.util.DateUtil;

import java.time.LocalDateTime;

/**
 * @author xiangqian
 * @date 20:59 2024/03/03
 */
@Slf4j
@Controller
@RequestMapping("/server")
@PreAuthorize("hasRole('ADMIN')")
public class ServerController extends AbsController {

    @Autowired
    private ServerService service;

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
    public RedirectView updById(@PathVariable Long id, ServerEntity vo) {
        try {
            vo.setId(id);
            service.updById(vo);
        } catch (Exception e) {
            log.error("", e);
            return redirectView("/server/" + vo.getId() + "?t=" + DateUtil.toSecond(LocalDateTime.now()), vo, null, e.getMessage());
        }
        return redirectListView(null);
    }

    @GetMapping("/{id}")
    public Object updById(ModelAndView modelAndView, @PathVariable Long id) {
        try {
            Object vo = getVoAttribute(modelAndView);
            if (vo == null) {
                ServerEntity entity = service.getById(id);
                Assert.notNull(entity, "服务器信息不存在");
                setVoAttribute(modelAndView, entity);
            }
        } catch (Exception e) {
            log.error("", e);
            return redirectListView(e.getMessage());
        }
        modelAndView.setViewName("server/addOrUpd");
        return modelAndView;
    }

    @PostMapping
    public RedirectView add(ServerEntity vo) {
        try {
            service.add(vo);
        } catch (Exception e) {
            log.error("", e);
            return redirectView("/server?t=" + DateUtil.toSecond(LocalDateTime.now()), vo, null, e.getMessage());
        }
        return redirectListView(null);
    }

    @GetMapping
    public ModelAndView add(ModelAndView modelAndView) {
        modelAndView.setViewName("server/addOrUpd");
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
        modelAndView.setViewName("server/list");
        return modelAndView;
    }

    private RedirectView redirectListView(Object error) {
        return redirectView("/server/list?t=" + DateUtil.toSecond(LocalDateTime.now()), null, null, error);
    }

}
