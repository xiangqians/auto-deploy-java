package org.xiangqian.auto.deploy.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.xiangqian.auto.deploy.entity.UserEntity;
import org.xiangqian.auto.deploy.mapper.UserMapper;
import org.xiangqian.auto.deploy.service.UserService;
import org.xiangqian.auto.deploy.util.AttributeName;
import org.xiangqian.auto.deploy.util.DateUtil;
import org.xiangqian.auto.deploy.util.SecurityUtil;

import java.io.IOException;
import java.time.LocalDateTime;

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

    @Autowired
    private ApplicationContext applicationContext;

    // 处理静态资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationFailureHandler authenticationFailureHandler,
                                                   AuthenticationSuccessHandler authenticationSuccessHandler) throws Exception {
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
                        .failureHandler(authenticationFailureHandler)
                        // 登录成功处理器
                        .successHandler(authenticationSuccessHandler)
                        // 放行资源
                        .permitAll()
                )
                // 处理权限不足的请求
                .exceptionHandling(configurer -> configurer.accessDeniedPage("/error"))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new AuthenticationFailureHandler() {
            @Autowired
            private UserService service;

            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                String userName = request.getParameter("username");
                HttpSession session = request.getSession(true);
                String loginError = null;
                if (exception instanceof BadCredentialsException) {
                    loginError = "用户名或密码不正确";
                    ThreadLocal<UserEntity> threadLocal = service.getThreadLocal();
                    UserEntity entity = threadLocal.get();
                    if (entity != null) {
                        threadLocal.remove();
                        UserEntity updEntity = new UserEntity();
                        updEntity.setId(entity.getId());
                        updEntity.setTryCount(entity.getTryCount() + 1);
                        updEntity.setUpdTime(DateUtil.toSecond(LocalDateTime.now()));
                        service.updById(updEntity);
                        if (updEntity.getTryCount() == 2) {
                            loginError = "您已连续两次输错密码，如连续输错三次，用户将被锁定";
                        }
                    }
                } else if (exception instanceof LockedException) {
                    loginError = "用户已锁定";
                } else {
                    loginError = exception.getMessage();
                }
                session.setAttribute(AttributeName.LOGIN_ERROR, loginError);
                session.setAttribute(AttributeName.USER_NAME, userName);
                response.sendRedirect("/login?error");
            }
        };
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SavedRequestAwareAuthenticationSuccessHandler() {
            @Autowired
            private UserMapper mapper;

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
                UserEntity entity = (UserEntity) authentication.getPrincipal();
                UserEntity updEntity = new UserEntity();
                updEntity.setId(entity.getId());
                updEntity.setTryCount(0);
                updEntity.setLastLoginIp(entity.getCurrentLoginIp());
                updEntity.setLastLoginTime(entity.getCurrentLoginTime());
                updEntity.setCurrentLoginIp(request.getRemoteHost());
                updEntity.setCurrentLoginTime(DateUtil.toSecond(LocalDateTime.now()));
                updEntity.setUpdTime(DateUtil.toSecond(LocalDateTime.now()));
                mapper.updateById(updEntity);

                super.onAuthenticationSuccess(request, response, authentication);
            }
        };
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
