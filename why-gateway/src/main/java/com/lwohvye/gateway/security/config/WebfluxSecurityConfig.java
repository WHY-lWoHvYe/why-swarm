package com.lwohvye.gateway.security.config;

import com.lwohvye.gateway.security.security.CustomSecurityContextRepository;
import com.lwohvye.gateway.security.security.DefaultAuthorizationManager;
import com.lwohvye.gateway.security.security.TokenAuthenticationManager;
import com.lwohvye.gateway.security.security.TokenProvider;
import com.lwohvye.gateway.security.security.handler.*;
import com.lwohvye.gateway.security.service.UserDetailsServiceImpl;
import com.lwohvye.reactive.config.security.SimpleReactiveSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.LinkedList;

/**
 * @author ShiLei
 * @version 1.0.0
 * @date 2021/3/11 10:56
 * @description webflux security核心配置类
 */
@EnableWebFluxSecurity
@ConditionalOnMissingBean(SimpleReactiveSecurityConfig.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class WebfluxSecurityConfig {
    @Autowired
    private DefaultAuthorizationManager defaultAuthorizationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private TokenAuthenticationManager tokenAuthenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomSecurityContextRepository customSecurityContextRepository;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    /**
     * 自定义过滤权限
     */
    @Value("${security.noFilter}")
    private String noFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                // 登录认证处理
                .authenticationManager(reactiveAuthenticationManager())
                .securityContextRepository(customSecurityContextRepository)
                // 请求拦截处理
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(noFilter).permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyExchange().access(defaultAuthorizationManager)
                )
                .formLogin()
                // 自定义处理
                .authenticationSuccessHandler(customAuthenticationSuccessHandler)
                .authenticationFailureHandler(customAuthenticationFailureHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .logout()
                .logoutHandler(new CustomLogoutHandler(tokenProvider))
                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                .and()
                .csrf().disable()
        ;
        return httpSecurity.build();
    }

    /**
     * BCrypt密码编码
     */
    @Bean("passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 注册用户信息验证管理器，可按需求添加多个按顺序执行
     */
    @Bean
    ReactiveAuthenticationManager reactiveAuthenticationManager() {
        LinkedList<ReactiveAuthenticationManager> managers = new LinkedList<>();
        managers.add(authentication -> {
            // 其他登陆方式 (比如手机号验证码登陆) 可在此设置不得抛出异常或者 Mono.error
            return Mono.empty();
        });
        // 必须放最后不然会优先使用用户名密码校验但是用户名密码不对时此 AuthenticationManager 会调用 Mono.error 造成后面的 AuthenticationManager 不生效
        managers.add(new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService));
        managers.add(tokenAuthenticationManager);
        return new DelegatingReactiveAuthenticationManager(managers);
    }
}
