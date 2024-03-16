package org.xiangqian.auto.deploy.controller;

import jakarta.servlet.http.HttpSession;
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
import org.xiangqian.auto.deploy.util.DateUtil;
import org.xiangqian.auto.deploy.util.List;

import java.time.LocalDateTime;

/**
 * @author xiangqian
 * @date 21:46 2024/03/03
 */
@Slf4j
@Controller
@RequestMapping("/item")
public class ItemController extends AbsController {

    @Autowired
    private ItemService service;

    @Autowired
    private GitService gitService;

    @Autowired
    private ServerService serverService;

    @GetMapping("/{itemId}/record/{recordId}/{type}")
    public ModelAndView getRecordMsg(ModelAndView modelAndView, @PathVariable Long itemId, @PathVariable Long recordId, @PathVariable String type) {
        modelAndView.addObject("type", type);
        modelAndView.addObject("msg", service.getRecordMsg(itemId, recordId, type));
        modelAndView.setViewName("item/record/list");
        return modelAndView;
    }

    @GetMapping("/{itemId}/record/list")
    public ModelAndView recordList(ModelAndView modelAndView, @PathVariable Long itemId, List list) {
        list = service.recordList(list, itemId);
        modelAndView.addObject("item", service.getById(itemId));
        modelAndView.addObject("offset", list.getOffset());
        modelAndView.addObject("rows", list.getRows());
        modelAndView.addObject("records", list.getData());
        modelAndView.addObject("offsets", list.getOffsets());
        modelAndView.setViewName("item/record/list");
        return modelAndView;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView delById(@PathVariable Long id) {
        service.delById(id);
        return redirectList();
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView updById(HttpSession session, ItemEntity vo) {
        try {
            service.updById(vo);
        } catch (Exception e) {
            log.error("", e);
            setVoAttribute(session, vo);
            setErrorAttribute(session, e.getMessage());
            return new RedirectView("/item/" + vo.getId() + "?t=" + DateUtil.toSecond(LocalDateTime.now()));
        }
        return redirectList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView updById(ModelAndView modelAndView, @PathVariable Long id) {
        modelAndView.addObject("vo", service.getById(id));
        modelAndView.addObject("gitVos", gitService.list());
        modelAndView.addObject("serverVos", serverService.list());
        modelAndView.setViewName("item/addOrUpd");
        return modelAndView;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView add(HttpSession session, ItemEntity vo) {
        try {
            service.add(vo);
        } catch (Exception e) {
            log.error("", e);
            setVoAttribute(session, vo);
            setErrorAttribute(session, e.getMessage());
            return new RedirectView("/item/add?t=" + DateUtil.toSecond(LocalDateTime.now()));
        }
        return redirectList();
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView add(ModelAndView modelAndView) {
        modelAndView.addObject("gitVos", gitService.list());
        modelAndView.addObject("serverVos", serverService.list());
        modelAndView.setViewName("item/addOrUpd");
        return modelAndView;
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView list(ModelAndView modelAndView) {
        modelAndView.addObject("vos", service.list());
        modelAndView.setViewName("item/list");
        return modelAndView;
    }

    private RedirectView redirectList() {
        return new RedirectView("/item/list?t=" + DateUtil.toSecond(LocalDateTime.now()));
    }

}
