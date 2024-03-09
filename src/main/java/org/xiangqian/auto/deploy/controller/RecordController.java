package org.xiangqian.auto.deploy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.xiangqian.auto.deploy.entity.ItemEntity;
import org.xiangqian.auto.deploy.service.ItemService;
import org.xiangqian.auto.deploy.service.RecordService;
import org.xiangqian.auto.deploy.util.AttributeName;
import org.xiangqian.auto.deploy.util.List;

import java.util.Map;

/**
 * @author xiangqian
 * @date 22:49 2024/03/04
 */
@Slf4j
@Controller
@RequestMapping("/item/{itemId}/record")
@PreAuthorize("hasRole('ADMIN')")
public class RecordController extends AbsController {

    @Autowired
    private RecordService service;

    @Autowired
    private ItemService itemService;

    @GetMapping("/list")
    public ModelAndView list(ModelAndView modelAndView, @PathVariable Long itemId, List list) {
        ItemEntity item = itemService.getById(itemId);
        list = service.list(list, itemId);
        modelAndView.addObject(AttributeName.VO, Map.of(
                "item", item,
                "offset", list.getOffset(),
                "rows", list.getRows(),
                "data", list.getData(),
                "offsets", list.getOffsets()));
        modelAndView.setViewName("item/record/list");
        return modelAndView;
    }

}
