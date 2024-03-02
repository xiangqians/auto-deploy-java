package org.xiangqian.auto.deploy.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.xiangqian.auto.deploy.util.AttributeName;
import org.xiangqian.auto.deploy.util.SecurityUtil;

/**
 * @author xiangqian
 * @date 21:02 2024/02/29
 */
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity(prePostEnabled = true, // 开启预处理验证
        jsr250Enabled = true, // 启用JSR250注解支持
        securedEnabled = true) // 启用 {@link org.springframework.security.access.annotation.Secured} 注解支持
public class SecurityConfiguration implements WebMvcConfigurer {

    // 处理静态资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // http授权请求配置
                .authorizeHttpRequests((authorize) -> authorize
                        // 放行静态资源
                        .requestMatchers("/static/**").permitAll()
                        // 放行/login?error
                        .requestMatchers(request -> "/login?error".equals(request.getServletPath() + "?" + request.getQueryString()) && request.getSession().getAttribute(AttributeName.LOGIN_ERROR) != null).permitAll()
                        // 其他请求需要授权
                        .anyRequest().authenticated())
                // 自定义表单登录
                .formLogin(configurer -> configurer
                        // 自定义登录页面
                        .loginPage("/login")
                        // 登录失败处理器
                        .failureHandler((request, response, exception) -> {
                            HttpSession session = request.getSession(true);
                            String loginError = null;
                            if (exception instanceof BadCredentialsException) {
                                loginError = "用户名或密码不正确";
                            } else if (exception instanceof LockedException) {
                                loginError = "用户帐户已锁定";
                            } else {
                                loginError = exception.getMessage();
                            }
                            session.setAttribute(AttributeName.LOGIN_ERROR, loginError);
                            session.setAttribute(AttributeName.USER_NAME, request.getParameter("username"));
                            response.sendRedirect("/login?error");
                        })
                        // 登录成功后的默认跳转页面
                        .defaultSuccessUrl("/")
                        // 放行资源
                        .permitAll()
                )
                // 处理权限不足的请求
                .exceptionHandling(configurer -> configurer.accessDeniedPage("/error"))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                if ("/login".equals(request.getServletPath()) && SecurityUtil.isLoggedin()) {
                    // 重定向到首页
                    response.sendRedirect("/");
                    // 不继续执行后续的拦截器
                    return false;
                }
                // 继续执行后续的拦截器
                return true;
            }
        });
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
