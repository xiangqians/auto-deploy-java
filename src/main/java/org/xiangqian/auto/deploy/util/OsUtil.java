package org.xiangqian.auto.deploy.util;

import lombok.Data;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.function.Consumer;

/**
 * @author xiangqian
 * @date 11:49 2024/03/09
 */
public class OsUtil {

    public static final boolean WINDOWS;
    public static final boolean LINUX;

    static {
        // 操作系统信息
        OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
        // 操作系统名称
        String name = osMXBean.getName().toUpperCase();
        WINDOWS = name.contains("WINDOWS");
        LINUX = name.contains("LINUX");
    }

    /**
     * @param command
     * @param consumer
     * @return exitValue
     * @throws Exception
     */
    public static int execute(String command, Consumer<String> consumer) throws Exception {
        String charsetName = null;
        if (WINDOWS) {
            // Windows NT: ?
            // Windows 95: ?
            // Windows 10: cmd /c
            command = "cmd /c " + command;
            charsetName = "GBK";

        } else if (LINUX) {
            command = "/bin/sh -c " + command;
            charsetName = "UTF-8";

        } else {
            throw new UnsupportedOperationException("目前暂不支持此操作系统：" + System.getProperty("os.name"));
        }

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);

            // 输入流
            InputStream inputStream = process.getInputStream();

            // 异常输入流
            InputStream errorStream = process.getErrorStream();

            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(new SequenceInputStream(inputStream, errorStream), charsetName));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    consumer.accept(line);
                }
            } finally {
                IOUtils.closeQuietly(bufferedReader);
            }

            // 等待外部进程处理完成，并获取外部进程的返回值
            int exitValue = process.waitFor();
            return exitValue;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    public static OsInfo getOsInfo() {
        OsInfo info = new OsInfo();

        // 操作系统信息
        OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();

        // 操作系统名称（操作系统名称 + 操作系统架构）
        info.setName(osMXBean.getName() + " " + osMXBean.getArch().toUpperCase());

        if (osMXBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsMXBean = (com.sun.management.OperatingSystemMXBean) osMXBean;
            info.setCpuLoad(String.format("%.2f%%", sunOsMXBean.getCpuLoad() * 100));
            info.setTotalMemory(OsUtil.humanByte(sunOsMXBean.getTotalMemorySize(), "MB"));
            info.setFreeMemory(OsUtil.humanByte(sunOsMXBean.getFreeMemorySize(), "MB"));
        }
        return info;
    }

    // 操作系统信息
    @Data
    public static class OsInfo {
        // 操作系统名称
        private String name;

        // 操作系统CPU负载
        private String cpuLoad;

        // 操作系统总内存
        private String totalMemory;

        // 操作系统空闲内存
        private String freeMemory;
    }

    /**
     * 人性化字节
     * <p>
     * 1B(Byte) = 8b(bit)
     * 1KB = 1024B
     * 1MB = 1024KB
     * 1GB = 1024MB
     * 1TB = 1024GB
     *
     * @param b    Byte
     * @param unit 单位：GB、MB、KB、B
     * @return
     */
    public static String humanByte(Long b, String unit) {
        if (b == null || b <= 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        // GB
        long gb = b / (1024 * 1024 * 1024);
        if (gb > 0) {
            builder.append(gb).append("GB");
            b = b % (1024 * 1024 * 1024);
            if ("GB".equals(unit)) {
                return builder.toString();
            }
        }

        // MB
        long mb = b / (1024 * 1024);
        if (mb > 0) {
            builder.append(mb).append("MB");
            b = b % (1024 * 1024);
            if ("MB".equals(unit)) {
                return builder.toString();
            }
        }

        // KB
        long kb = b / 1024;
        if (kb > 0) {
            builder.append(kb).append("KB");
            b = b % 1024;
            if ("KB".equals(unit)) {
                return builder.toString();
            }
        }

        // B
        if (b > 0) {
            builder.append(b).append("B");
        }

        return builder.toString();
    }

}
