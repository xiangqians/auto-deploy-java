package org.xiangqian.auto.deploy.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * {@link LocalDateTime} 本地日期时间：表示了一个没有时区信息的日期和时间，并且它的内部实现是基于本地时钟的，不涉及时区转换。因此，{@link LocalDateTime} 对象的创建和操作通常比较高效。
 * <p>
 * {@link ZonedDateTime} 时区日期时间：类表示了一个带有时区信息的日期和时间。它提供了更多的功能，例如时区转换、跨时区计算等。由于其涉及到时区的处理，创建和操作 {@link ZonedDateTime} 对象可能会比 {@link LocalDateTime} 稍微慢一些。然而，这个性能差异通常是微小的，对于大多数情况来说不会对性能产生显著影响。
 * <p>
 * 日期时间格式
 * "yyyy"：年份
 * "MM"  ：月份
 * "dd"  ：日期
 * "HH"  ：小时（24小时制）
 * "mm"  ：分钟
 * "ss"  ：秒钟
 * "SSS" ：毫秒
 * 例如：
 * "yyyy/MM/dd"：年份、月份和日期（例如：2023/11/13）
 * "HH:mm:ss"  ：小时、分钟和秒钟（例如：10:31:38）
 * "yyyy/MM/dd HH:mm:ss.SSS"：完整的日期时间（例如：2023/11/13 10:31:38.491）
 *
 * @author xiangqian
 * @date 21:24 2022/08/01
 */
public class DateUtil {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static String format(LocalDateTime dateTime) {
        return formatter.format(dateTime);
    }

    public static LocalDateTime parse(String text) {
        return LocalDateTime.parse(text, formatter);
    }

    public static long toSecond(LocalDateTime dateTime) {
        return toSecond(dateTime, ZoneId.systemDefault());
    }

    public static long toSecond(LocalDateTime dateTime, ZoneId zoneId) {
        return dateTime.atZone(zoneId).toEpochSecond();
    }

    public static LocalDateTime ofSecond(long second) {
        return ofSecond(second, ZoneId.systemDefault());
    }

    public static LocalDateTime ofSecond(long second, ZoneId zoneId) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(second), zoneId);
    }

    public static long toMillisecond(LocalDateTime dateTime) {
        return toMillisecond(dateTime, ZoneId.systemDefault());
    }

    public static long toMillisecond(LocalDateTime dateTime, ZoneId zoneId) {
        return dateTime.atZone(zoneId).toInstant().toEpochMilli();
    }

    public static LocalDateTime ofMillisecond(long millisecond) {
        return ofMillisecond(millisecond, ZoneId.systemDefault());
    }

    public static LocalDateTime ofMillisecond(long millisecond, ZoneId zoneId) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millisecond), zoneId);
    }

    public static Date toDate(LocalDateTime dateTime) {
        return toDate(dateTime, ZoneId.systemDefault());
    }

    public static Date toDate(LocalDateTime dateTime, ZoneId zoneId) {
        return Date.from(dateTime.atZone(zoneId).toInstant());
    }

    public static LocalDateTime ofDate(Date date) {
        return ofDate(date, ZoneId.systemDefault());
    }

    public static LocalDateTime ofDate(Date date, ZoneId zoneId) {
        return date.toInstant().atZone(zoneId).toLocalDateTime();
    }

    public static String formatSecond(long second) {
        if (second <= 0) {
            return "";
        }
        return format(ofSecond(second));
    }

    /**
     * 人性化日期时间戳（s）
     *
     * @param second
     * @return
     */
    public static String humanSecond(Long second) {
        if (second == null || second <= 0) {
            return "";
        }

        LocalDateTime dateTime = ofSecond(second);
        Duration duration = Duration.between(dateTime, LocalDateTime.now());

        // 天
        if (duration.toDays() > 0) {
            return format(dateTime);
        }

        // 小时
        long hour = duration.toHours();
        if (hour > 0) {
            return hour + "小时前";
        }

        // 分钟
        long minute = duration.toMinutes();
        if (minute > 0) {
            return minute + "分钟前";
        }

        // 秒
        second = duration.toSeconds();
        return second + "秒前";
    }

    /**
     * 人性化 {@link Duration}
     *
     * @param second
     * @return
     */
    public static String humanDurationSecond(Long second) {
        if (second == null || second <= 0) {
            return "";
        }

        Duration duration = Duration.ofSeconds(second);
        if (duration.toSeconds() == 0) {
            return "0秒";
        }

        StringBuilder builder = new StringBuilder();

        // 小时
        long hour = duration.toHours();
        if (hour > 0) {
            builder.append(hour).append("小时");
        }

        // 分钟
        int minute = duration.toMinutesPart();
        if (minute > 0) {
            builder.append(minute).append("分钟");
        }

        // 秒
        int second0 = duration.toSecondsPart();
        if (second0 > 0) {
            builder.append(second0).append("秒");
        }

        return builder.toString();
    }

}
