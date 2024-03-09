package org.xiangqian.auto.deploy.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.xiangqian.auto.deploy.util.AttributeName;
import org.xiangqian.auto.deploy.util.DateUtil;
import org.xiangqian.auto.deploy.util.OsUtil;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author xiangqian
 * @date 17:27 2024/03/02
 */
public abstract class AbsController {

    // 在每个请求之前设置ModelAndView值
    @ModelAttribute
    public void modelAttribute(ModelAndView modelAndView, HttpServletRequest request, HttpSession session) {
        modelAndView.addObject("servletPath", request.getServletPath());
        modelAndView.addObject("timestamp", DateUtil.toSecond(LocalDateTime.now()));

        // 操作系统信息
        OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
        // 操作系统名称（操作系统名称 + 操作系统架构）
        String osName = osMXBean.getName() + " " + osMXBean.getArch().toUpperCase();
        // 操作系统CPU负载
        String osCpuLoad = null;
        // 操作系统总内存
        String osTotalMemory = null;
        // 操作系统空闲内存
        String osFreeMemory = null;
        if (osMXBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsMXBean = (com.sun.management.OperatingSystemMXBean) osMXBean;
            osCpuLoad = String.format("%.2f%%", sunOsMXBean.getCpuLoad() * 100);
            osTotalMemory = OsUtil.humanByte(sunOsMXBean.getTotalMemorySize());
            osFreeMemory = OsUtil.humanByte(sunOsMXBean.getFreeMemorySize());
        }
        modelAndView.addObject("os",
                Map.of("name", osName,
                        "cpuLoad", osCpuLoad,
                        "totalMemory", osTotalMemory,
                        "freeMemory", osFreeMemory));

        // JVM信息
        modelAndView.addObject("jvm",
                // JVM厂商
                Map.of("vendor", System.getProperty("java.vendor"),
                        // JVM版本
                        "version", System.getProperty("java.version"),
                        // JVM最大内存
                        "maxMemory", OsUtil.humanByte(Runtime.getRuntime().maxMemory()),
                        // JVM空闲内存
                        "freeMemory", OsUtil.humanByte(Runtime.getRuntime().freeMemory())));

        Object vo = getVoAttribute(session);
        if (vo != null) {
            delVoAttribute(session);
            modelAndView.addObject(AttributeName.VO, vo);
        }

        Object error = getErrorAttribute(session);
        if (error != null) {
            delErrorAttribute(session);
            modelAndView.addObject(AttributeName.ERROR, error);
        }
    }

    protected final void delVoAttribute(HttpSession session) {
        session.removeAttribute(AttributeName.VO);
    }

    protected final void setVoAttribute(HttpSession session, Object value) {
        session.setAttribute(AttributeName.VO, value);
    }

    protected final Object getVoAttribute(HttpSession session) {
        return session.getAttribute(AttributeName.VO);
    }

    protected final void delErrorAttribute(HttpSession session) {
        session.removeAttribute(AttributeName.ERROR);
    }

    protected final void setErrorAttribute(HttpSession session, Object value) {
        session.setAttribute(AttributeName.ERROR, value);
    }

    protected final Object getErrorAttribute(HttpSession session) {
        return session.getAttribute(AttributeName.ERROR);
    }

}
