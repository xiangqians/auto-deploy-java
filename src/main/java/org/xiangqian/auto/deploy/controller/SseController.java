package org.xiangqian.auto.deploy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.xiangqian.auto.deploy.service.SseService;

/**
 * Server-Sent Events（SSE）是一种用于在客户端和服务器之间建立单向持续连接的技术，主要用于服务器向客户端推送事件流。
 * <p>
 * SSE（Server-Sent Events）：
 * 1、SSE是基于HTTP的单向通信技术，允许服务器向客户端推送事件流。
 * 2、SSE使用标准的HTTP或HTTPS协议进行通信，因此可以通过标准的HTTP服务器和代理进行部署，无需额外的协议升级。
 * 3、SSE只能从服务器向客户端发送数据，客户端无法向服务器发送数据。因此，它适用于服务器向客户端的单向通知或推送场景。
 * 4、SSE在客户端使用EventSource对象来接收服务器推送的事件，服务器可以随时向客户端发送新的事件。
 * <p>
 * 大多数现代浏览器都已经支持 SSE 技术，包括以下主流浏览器：
 * 1、Google Chrome
 * 2、Mozilla Firefox
 * 3、Safari
 * 4、Microsoft Edge
 * 5、Opera
 * 这些浏览器都提供对 SSE 的良好支持，使得开发者可以在 Web 应用程序中使用 SSE 来实现实时更新和服务器推送功能。
 * <p>
 * 总的来说，绝大多数现代浏览器都支持 SSE 技术，因此你可以放心在你的 Web 应用程序中使用 SSE 来实现服务器推送功能。如果有任何其他问题或疑问，请随时告诉我！
 * <p>
 * SSE Streaming in Spring MVC
 *
 * @author xiangqian
 * @date 09:36 2024/03/10
 */
@RestController
@RequestMapping("/sse")
public class SseController {

    @Autowired
    private SseService service;

    @GetMapping("/{names}")
    public SseEmitter sse(@PathVariable String names) {
        return service.sse(names);
    }

}
