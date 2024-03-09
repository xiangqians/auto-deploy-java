package org.xiangqian.auto.deploy.util;

/**
 * @author xiangqian
 * @date 11:49 2024/03/09
 */
public class OsUtil {

    /**
     * 人性化字节
     * <p>
     * 1B(Byte) = 8b(bit)
     * 1KB = 1024B
     * 1MB = 1024KB
     * 1GB = 1024MB
     * 1TB = 1024GB
     *
     * @param b Byte
     * @return
     */
    public static String humanByte(Long b) {
        if (b == null || b < 0) {
            return "-B";
        }

        if (b == 0) {
            return "0B";
        }

        StringBuilder builder = new StringBuilder();

        // GB
        long gb = b / (1024 * 1024 * 1024);
        if (gb > 0) {
            builder.append(gb).append("GB");
            b = b % (1024 * 1024 * 1024);
        }

        // MB
        long mb = b / (1024 * 1024);
        if (mb > 0) {
            builder.append(mb).append("MB");
            b = b % (1024 * 1024);
            return builder.toString();
        }

        // KB
        long kb = b / 1024;
        if (kb > 0) {
            builder.append(kb).append("KB");
            b = b % 1024;
            return builder.toString();
        }

        // B
        if (b > 0) {
            builder.append(b).append("B");
        }

        return builder.toString();
    }

}
