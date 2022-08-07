package com.lwohvye.gateway.security.security;

import com.lwohvye.gateway.security.service.UserDetailsServiceImpl;
import com.lwohvye.gateway.security.service.dto.JwtUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author ShiLei
 * @version 1.0.0
 * @date 2021/3/11 13:23
 * @description token 认证处理
 */
@Component
@Primary
public class TokenAuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private TokenProvider tokenProvider; // Token

    @Autowired
    private UserDetailsServiceImpl userDetailsService; // UserDetails

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .publishOn(Schedulers.boundedElastic())
                .map(auth -> {
                    var token = (String) authentication.getCredentials();
                    if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                        // 根据用户名，从服务侧获取用户详细信息
                        var jwtUserDto = (JwtUserDto) userDetailsService.findByUsername(userDetails.getUsername())
                                .block();
                        // 校验
                        if (Boolean.TRUE.equals(tokenProvider.validateToken(token, jwtUserDto))) {
                            // 剩余时常不足时，进行通知（只通知一次）
                            tokenProvider.noticeExpire5Token(token);
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                    return authentication;
                });
    }
}
