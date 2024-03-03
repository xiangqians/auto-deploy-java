package org.xiangqian.auto.deploy.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.xiangqian.auto.deploy.entity.UserEntity;
import org.xiangqian.auto.deploy.mapper.UserMapper;
import org.xiangqian.auto.deploy.service.UserService;
import org.xiangqian.auto.deploy.util.DateUtil;
import org.xiangqian.auto.deploy.util.SecurityUtil;
import org.xiangqian.auto.deploy.util.SessionUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

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
                                                   AuthenticationSuccessHandler authenticationSuccessHandler,
                                                   SessionRegistry sessionRegistry) throws Exception {
        http
                // http授权请求配置
                .authorizeHttpRequests((authorize) -> authorize
                        // 放行静态资源
                        .requestMatchers("/static/**").permitAll()
                        // 放行/login?error
                        .requestMatchers(request -> "/login?error".equals(request.getServletPath() + "?" + request.getQueryString()) && SessionUtil.getError() != null).permitAll()
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
                // session会话管理：限制同一个用户只允许一端登录
                .sessionManagement(configurer -> configurer
                        // 设置每个用户最大会话数为1，这意味着同一个用户只允许一端登录
                        .maximumSessions(1)
                        // 当达到最大会话数时阻止新的登录，确保用户无法在多个地方同时登录
                        //.maxSessionsPreventsLogin(true)
                        // 当达到最大会话数时剔除旧登录
                        .sessionRegistry(sessionRegistry)
                        // 会话过期后跳转的URL
                        .expiredUrl("/login?expired")
                )
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
                String name = request.getParameter("username");
                HttpSession session = request.getSession(true);
                String error = null;
                if (exception instanceof BadCredentialsException) {
                    error = "用户名或密码不正确";
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
                            error = "已连续两次输错密码，如连续输错三次，用户将被锁定";
                        }
                    }
                } else if (exception instanceof LockedException) {
                    error = "用户已被锁定";
                } else {
                    error = exception.getMessage();
                }

                SessionUtil.setError(error);

                UserEntity entity = new UserEntity();
                entity.setName(name);
                SessionUtil.setVo(entity);

                response.sendRedirect("/login?error");
            }
        };
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SavedRequestAwareAuthenticationSuccessHandler() {
            @Autowired
            private UserMapper mapper;

            @Autowired
            private SessionRegistry sessionRegistry;

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
                UserEntity entity = (UserEntity) authentication.getPrincipal();

                // 限制同一个用户只允许一端登录
                List<SessionInformation> sessions = sessionRegistry.getAllSessions(entity,
                        false); // 是否包括过期的会话
                if (CollectionUtils.isNotEmpty(sessions)) {
                    HttpSession currentSession = request.getSession(true);
                    String currentSessionId = currentSession.getId();
                    for (SessionInformation session : sessions) {
                        if (!currentSessionId.equals(session.getSessionId())) {
                            session.expireNow();
                        }
                    }
                }

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

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * <form method="post" action="/your-endpoint">
     * <input type="hidden" name="_method" value="PUT">
     * <button type="submit">提交</button>
     * </form>
     * 由于HTML表单只支持GET和POST请求方法，因此需要进行一些额外的处理来实现PUT和DELETE请求
     * 配置过滤器 {@link HiddenHttpMethodFilter} 来解析这个名为 _method 的隐藏字段，并将请求方法修改为PUT或DELETE
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<HiddenHttpMethodFilter> hiddenHttpMethodFilter() {
        FilterRegistrationBean<HiddenHttpMethodFilter> filterRegBean = new FilterRegistrationBean<>(new HiddenHttpMethodFilter());
        filterRegBean.addUrlPatterns("/*");
        return filterRegBean;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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

}
