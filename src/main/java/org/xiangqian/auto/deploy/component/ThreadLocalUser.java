package org.xiangqian.auto.deploy.component;

import org.springframework.stereotype.Component;
import org.xiangqian.auto.deploy.entity.UserEntity;

/**
 * 线程本地用户服务
 *
 * @author xiangqian
 * @date 22:08 2024/03/04
 */
@Component
public class ThreadLocalUser {

    private ThreadLocal<UserEntity> threadLocal;

    public ThreadLocalUser() {
        this.threadLocal = new ThreadLocal<>();
    }

    public UserEntity getAndDel() {
        UserEntity entity = get();
        if (entity != null) {
            del();
        }
        return entity;
    }

    public Boolean del() {
        threadLocal.remove();
        return true;
    }

    public Boolean set(UserEntity entity) {
        threadLocal.set(entity);
        return true;
    }

    public UserEntity get() {
        return threadLocal.get();
    }

}
