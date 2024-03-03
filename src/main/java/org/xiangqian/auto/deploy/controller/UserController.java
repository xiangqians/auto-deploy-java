package org.xiangqian.auto.deploy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.xiangqian.auto.deploy.service.UserService;
import org.xiangqian.auto.deploy.util.AttributeName;
import org.xiangqian.auto.deploy.util.DateUtil;
import org.xiangqian.auto.deploy.util.SessionUtil;
import org.xiangqian.auto.deploy.vo.user.UserAddVo;
import org.xiangqian.auto.deploy.vo.user.UserResetPasswdVo;

import java.time.LocalDateTime;

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

    @PostMapping("/add")
    public RedirectView add(UserAddVo vo) {
        try {
            service.add(vo);
        } catch (Exception e) {
            log.error("", e);
            SessionUtil.setVo(vo);
            SessionUtil.setError(e.getMessage());
            return new RedirectView("/user/add?error&t=" + DateUtil.toSecond(LocalDateTime.now()));
        }
        return redirectListView();
    }

    @GetMapping("/add")
    public ModelAndView addView(ModelAndView modelAndView) {
        modelAndView.setViewName("user/add");
        return modelAndView;
    }

    @DeleteMapping("/{id}")
    public RedirectView del(@PathVariable Long id) {
        service.del(id);
        return redirectListView();
    }

    @PutMapping("/unlock/{id}")
    public RedirectView unlock(@PathVariable Long id) {
        service.unlock(id);
        return redirectListView();
    }

    @PutMapping("/lock/{id}")
    public RedirectView lock(@PathVariable Long id) {
        service.lock(id);
        return redirectListView();
    }

    @PutMapping("/resetPasswd")
    public RedirectView resetPasswd(UserResetPasswdVo vo) {
        service.resetPasswd(vo);
        return redirectListView();
    }

    @GetMapping("/list")
    public ModelAndView list(ModelAndView modelAndView) {
        modelAndView.addObject(AttributeName.VOS, service.list());
        modelAndView.setViewName("user/list");
        return modelAndView;
    }

    private RedirectView redirectListView() {
        return new RedirectView("/user/list?t=" + DateUtil.toSecond(LocalDateTime.now()));
    }

}
