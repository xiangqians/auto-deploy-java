package org.xiangqian.auto.deploy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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
import java.util.HashMap;
import java.util.Map;

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

    // /////////////////------------------------------------------------------------

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView updById(@PathVariable Long id, ItemEntity vo) {
        try {
            vo.setId(id);
            service.updById(vo);
            return redirectListView(null);
        } catch (Exception e) {
            log.error("", e);
            return redirectView("/item/" + vo.getId() + "?t=" + DateUtil.toSecond(LocalDateTime.now()), vo, null, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Object updById(ModelAndView modelAndView, @PathVariable Long id) {
        try {
            Object item = getVoAttribute(modelAndView);
            if (item == null) {
                item = service.getById(id);
                Assert.notNull(item, "项目信息不存在");
            }

            Map<String, Object> map = new HashMap<>(3, 1f);
            map.put("item", item);
            map.put("gits", gitService.list());
            map.put("servers", serverService.list());
            setVoAttribute(modelAndView, map);
            modelAndView.setViewName("item/addOrUpd");
            return modelAndView;
        } catch (Exception e) {
            log.error("", e);
            return redirectListView(e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView add(ItemEntity vo) {
        try {
            service.add(vo);
            return redirectListView(null);
        } catch (Exception e) {
            log.error("", e);
            return redirectView("/item?t=" + DateUtil.toSecond(LocalDateTime.now()), vo, null, e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView add(ModelAndView modelAndView) {
        try {
            Map<String, Object> map = new HashMap<>(3, 1f);
            Object item = getVoAttribute(modelAndView);
            map.put("item", item);
            map.put("gits", gitService.list());
            map.put("servers", serverService.list());
            setVoAttribute(modelAndView, map);
        } catch (Exception e) {
            log.error("", e);
            setErrorAttribute(modelAndView, e.getMessage());
        }
        modelAndView.setViewName("item/addOrUpd");
        return modelAndView;
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView list(ModelAndView modelAndView) {
        try {
            setVosAttribute(modelAndView, service.list());
        } catch (Exception e) {
            log.error("", e);
            setErrorAttribute(modelAndView, e.getMessage());
        }
        modelAndView.setViewName("item/list");
        return modelAndView;
    }

    private RedirectView redirectListView(Object error) {
        return redirectView("/item/list?t=" + DateUtil.toSecond(LocalDateTime.now()), null, null, error);
    }

}
