package org.xiangqian.auto.deploy.util;

import lombok.Data;

/**
 * @author xiangqian
 * @date 10:20 2024/03/10
 */
public class JvmUtil {

    public static JvmInfo getJvmInfo() {
        JvmInfo info = new JvmInfo();
        info.setVendor(System.getProperty("java.vendor"));
        info.setVersion(System.getProperty("java.version"));
        info.setMaxMemory(OsUtil.humanByte(Runtime.getRuntime().maxMemory(), "MB"));
        info.setFreeMemory(OsUtil.humanByte(Runtime.getRuntime().freeMemory(), "MB"));
        return info;
    }

    // JVM信息
    @Data
    public static class JvmInfo {
        // JVM厂商
        private String vendor;

        // JVM版本
        private String version;

        // JVM最大内存
        private String maxMemory;

        // JVM空闲内存
        private String freeMemory;
    }

}
