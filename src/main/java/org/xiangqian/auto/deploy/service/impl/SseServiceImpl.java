package org.xiangqian.auto.deploy.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.xiangqian.auto.deploy.service.IndexService;
import org.xiangqian.auto.deploy.service.SseService;
import org.xiangqian.auto.deploy.util.JsonUtil;
import org.xiangqian.auto.deploy.util.JvmUtil;
import org.xiangqian.auto.deploy.util.OsUtil;
import org.xiangqian.auto.deploy.util.SecurityUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author xiangqian
 * @date 10:57 2024/03/10
 */
@Slf4j
@Service
public class SseServiceImpl implements SseService, Runnable, ApplicationRunner {

    @Autowired
    private IndexService indexService;

    private List<SseEntry> entryList = Collections.synchronizedList(new ArrayList<>(64));

    @Data
    @AllArgsConstructor
    public static class SseEntry {
        private Authentication authentication;
        private Set<String> names;
        private SseEmitter emitter;
    }

    @Override
    public SseEmitter sse(String names) {
        SseEmitter emitter = new SseEmitter();

        // 设置超时时间，避免长时间连接没有数据时导致超时
        emitter.onTimeout(emitter::complete);

        // 添加到集合
        entryList.add(new SseEntry(SecurityUtil.getAuthentication(), Arrays.stream(names.split(",")).collect(Collectors.toSet()), emitter));

        return emitter;
    }

    // 在另一个线程中定时推送事件
    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
                if (CollectionUtils.isEmpty(entryList)) {
                    continue;
                }

                int index = 0;
                int size = entryList.size();
                while (index < size) {
                    SseEntry entry = entryList.get(index);
                    Set<String> names = entry.getNames();
                    SseEmitter emitter = entry.getEmitter();
                    try {
                        for (String name : names) {
                            Object data = switch (name) {
                                case "jvm" -> JvmUtil.getJvmInfo();
                                case "os" -> OsUtil.getOsInfo();
                                case "index" -> {
                                    SecurityUtil.setAuthentication(entry.getAuthentication());
                                    yield indexService.list();
                                }
                                default -> null;
                            };
                            if (data != null) {
                                emitter.send(SseEmitter.event().id(UUID.randomUUID().toString()).name(name).data(JsonUtil.serializeAsString(data)));
                            }
                        }
                        index++;
                    } catch (Exception e) {
//                        log.error("", e);
                        // 发生错误时完成事件流
                        emitter.completeWithError(e);

                        try {
                            entryList.remove(index);
                        } catch (Exception e1) {
                        }
                    }
                    size = entryList.size();
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(this).start();
    }

}
