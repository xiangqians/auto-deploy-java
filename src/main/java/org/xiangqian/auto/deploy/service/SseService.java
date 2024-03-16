package org.xiangqian.auto.deploy.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author xiangqian
 * @date 10:56 2024/03/10
 */
public interface SseService {

    /**
     * @param names 事件名称集，多个以逗号分隔
     * @return
     */
    SseEmitter sse(String names);

}
