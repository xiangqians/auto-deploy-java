package org.xiangqian.auto.deploy.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.xiangqian.auto.deploy.entity.ItemEntity;
import org.xiangqian.auto.deploy.entity.UserItemEntity;
import org.xiangqian.auto.deploy.service.ItemService;
import org.xiangqian.auto.deploy.service.UserItemService;
import org.xiangqian.auto.deploy.service.UserService;
import org.xiangqian.auto.deploy.util.AttributeName;
import org.xiangqian.auto.deploy.util.DateUtil;
import org.xiangqian.auto.deploy.vo.UserItemAddVo;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiangqian
 * @date 23:32 2024/03/04
 */
@Slf4j
@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/user/{userId}/item")
public class UserItemController extends AbsController {

    @Autowired
    private UserItemService service;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @DeleteMapping("/{itemId}")
    public RedirectView del(@PathVariable Long userId, @PathVariable Long itemId) {
        service.del(userId, itemId);
        return redirectList(userId);
    }

    @PostMapping
    public RedirectView add(@PathVariable Long userId, HttpSession session, UserItemAddVo vo) {
        try {
            service.add(userId, vo);
        } catch (Exception e) {
            log.error("", e);
            setErrorAttribute(session, e.getMessage());
        }
        return redirectList(userId);
    }

    @GetMapping("/list")
    public ModelAndView list(ModelAndView modelAndView, @PathVariable Long userId) {
        modelAndView.addObject(AttributeName.USER, userService.getById(userId));

        List<UserItemEntity> userItems = service.list(userId);
        modelAndView.addObject(AttributeName.VOS, userItems);

        Set<Long> itemIds = Optional.ofNullable(userItems).filter(CollectionUtils::isNotEmpty)
                .map(userItems0 -> userItems0.stream().map(UserItemEntity::getItemId).collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        List<ItemEntity> items = itemService.list();
        if (CollectionUtils.isNotEmpty(itemIds) && CollectionUtils.isNotEmpty(items)) {
            Iterator<ItemEntity> iterator = items.iterator();
            while (iterator.hasNext()) {
                ItemEntity item = iterator.next();
                if (itemIds.contains(item.getId())) {
                    iterator.remove();
                }
            }
        }
        modelAndView.addObject(AttributeName.ITEMS, items);

        modelAndView.setViewName("user/item/list");
        return modelAndView;
    }

    private RedirectView redirectList(Long userId) {
        return new RedirectView("/user/" + userId + "/item/list?t=" + DateUtil.toSecond(LocalDateTime.now()));
    }

}
