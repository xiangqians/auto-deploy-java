package org.xiangqian.auto.deploy.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.xiangqian.auto.deploy.entity.ItemEntity;
import org.xiangqian.auto.deploy.entity.UserEntity;
import org.xiangqian.auto.deploy.entity.UserItemEntity;
import org.xiangqian.auto.deploy.service.ItemService;
import org.xiangqian.auto.deploy.service.UserService;
import org.xiangqian.auto.deploy.util.DateUtil;
import org.xiangqian.auto.deploy.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiangqian
 * @date 17:09 2024/03/02
 */
@Slf4j
@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController extends AbsController {

    @Autowired
    private UserService service;

    @Autowired
    private ItemService itemService;

    @DeleteMapping("/{userId}/item/{itemId}")
    public RedirectView delItem(HttpSession session, @PathVariable Long userId, @PathVariable Long itemId) {
        try {
            UserItemEntity vo = new UserItemEntity();
            vo.setUserId(userId);
            vo.setItemId(itemId);
            service.delItem(vo);
        } catch (Exception e) {
            log.error("", e);
            setErrorAttribute(session, e.getMessage());
        }
        return redirectItemList(userId);
    }

    @PostMapping("/{userId}/item")
    public RedirectView addItem(HttpSession session, @PathVariable Long userId, UserItemEntity vo) {
        try {
            vo.setUserId(userId);
            service.addItem(vo);
        } catch (Exception e) {
            log.error("", e);
            setErrorAttribute(session, e.getMessage());
        }
        return redirectItemList(userId);
    }

    @GetMapping("/{userId}/item/list")
    public ModelAndView itemList(HttpSession session, ModelAndView modelAndView, @PathVariable Long userId) {
        try {
            UserEntity user = service.getById(userId);

            Set<Long> itemIds = null;
            List<UserItemEntity> userItems = service.itemList(userId);
            if (CollectionUtils.isNotEmpty(userItems)) {
                itemIds = userItems.stream().map(UserItemEntity::getItemId).collect(Collectors.toSet());
            }

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

            setVoAttribute(modelAndView, Map.of("user", user,
                    "userItems", userItems,
                    "items", items));
        } catch (Exception e) {
            log.error("", e);
            setErrorAttribute(session, e.getMessage());
        }
        modelAndView.setViewName("user/item/list");
        return modelAndView;
    }

    private RedirectView redirectItemList(Long userId) {
        return new RedirectView("/user/" + userId + "/item/list?t=" + DateUtil.toSecond(LocalDateTime.now()));
    }

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

    @PutMapping("/{id}/unlock")
    public RedirectView unlock(@PathVariable Long id) {
        Object error = null;
        try {
            service.unlock(id);
        } catch (Exception e) {
            log.error("", e);
            error = e.getMessage();
        }
        return redirectListView(error);
    }

    @PutMapping("/{id}/lock")
    public RedirectView lock(@PathVariable Long id) {
        Object error = null;
        try {
            service.lock(id);
        } catch (Exception e) {
            log.error("", e);
            error = e.getMessage();
        }
        return redirectListView(error);
    }

    @PutMapping("/{id}/resetPasswd")
    public RedirectView resetPasswd(HttpSession session, @PathVariable Long id, UserEntity vo) {
        try {
            vo.setId(id);
            service.resetPasswd(vo);
        } catch (Exception e) {
            log.error("", e);
            setErrorAttribute(session, e.getMessage());
        }
        return redirectListView(null);
    }

    @PutMapping("/current")
    public RedirectView updCurrent(UserEntity vo) {
        try {
            service.updCurrent(vo);
            return redirectIndexView(null);
        } catch (Exception e) {
            log.error("", e);
            return redirectView("/user/current?t=" + DateUtil.toSecond(LocalDateTime.now()), vo, null, e.getMessage());
        }
    }

    @GetMapping("/current")
    public Object updCurrent(ModelAndView modelAndView) {
        try {
            Object vo = getVoAttribute(modelAndView);
            if (vo == null) {
                vo = SecurityUtil.getUser();
                Assert.notNull(vo, "用户信息不存在");
            }
            setVoAttribute(modelAndView, vo);
            modelAndView.setViewName("user/addOrUpd");
            return modelAndView;
        } catch (Exception e) {
            log.error("", e);
            return redirectIndexView(e.getMessage());
        }
    }

    @PostMapping
    public RedirectView add(UserEntity vo) {
        try {
            service.add(vo);
        } catch (Exception e) {
            log.error("", e);
            return redirectView("/user?t=" + DateUtil.toSecond(LocalDateTime.now()), vo, null, e.getMessage());
        }
        return redirectListView(null);
    }

    @GetMapping
    public ModelAndView add(ModelAndView modelAndView) {
        modelAndView.setViewName("user/addOrUpd");
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
        modelAndView.setViewName("user/list");
        return modelAndView;
    }

    private RedirectView redirectListView(Object error) {
        return redirectView("/user/list?t=" + DateUtil.toSecond(LocalDateTime.now()), null, null, error);
    }

    private RedirectView redirectIndexView(Object error) {
        return redirectView("/?t=" + DateUtil.toSecond(LocalDateTime.now()), null, null, error);
    }
}
