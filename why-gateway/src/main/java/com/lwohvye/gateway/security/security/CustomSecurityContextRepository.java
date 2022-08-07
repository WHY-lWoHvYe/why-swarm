package com.lwohvye.gateway.security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author ShiLei
 * @version 1.0.0
 * @date 2021/3/11 16:27
 * @description 存储认证授权的相关信息，JWT Token认证管理
 */
@Component
public class CustomSecurityContextRepository implements ServerSecurityContextRepository {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private TokenAuthenticationManager tokenAuthenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    // Get token than genAuthenticate
    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();

        String token = tokenProvider.getToken(request);
        if (StringUtils.hasText(token)) {
            var authentication = tokenProvider.getAuthentication(token);

            // 在这里把用户名放进去，后端的服务会用到取出
            request.getHeaders().add("GWUName", authentication.getName());
            return tokenAuthenticationManager.authenticate(authentication).map(SecurityContextImpl::new);
        }
        return Mono.empty();
    }
}
