package org.xiangqian.auto.deploy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xiangqian.auto.deploy.service.RecordService;

/**
 * @author xiangqian
 * @date 22:49 2024/03/04
 */
@Slf4j
@Controller
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService service;

}
