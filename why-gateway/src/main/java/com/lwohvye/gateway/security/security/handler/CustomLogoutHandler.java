/*
 *    Copyright (c) 2021-2022.  lWoHvYe(Hongyan Wang)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.lwohvye.gateway.security.security.handler;

import com.lwohvye.gateway.security.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 处理退出逻辑
 *
 * @date 2021/11/27 9:42 上午
 */
@Slf4j
public record CustomLogoutHandler(TokenProvider tokenProvider) implements ServerLogoutHandler {

    @Override
    public Mono<Void> logout(WebFilterExchange exchange, Authentication authentication) {
        return Mono.defer(() -> Mono.just(exchange.getExchange().getRequest()).flatMap(request -> {
            var auth = authentication;
            // 根据过滤器的执行顺序。LogoutFilter在UsernamePasswordAuthenticationFilter之前执行。所以在这里时，是还没包办好的，要自己处理
            if (Objects.isNull(auth)) {
                try {
                    String token = tokenProvider.getToken(request);
                    if (StringUtils.hasText(token))
                        auth = tokenProvider.getAuthentication(token);

                } catch (Exception ignored) {
                    // 这里出异常后，不要再抛了，因为会被异常处理器处理，可能被重定向到logout，这就构成♻️了
                }
                // 如果还拿不到，就返回了。无情
                if (Objects.isNull(auth))
                    return Mono.empty();
                // 先放进去，因为后面还有个处理登出成功的要用
                // SecurityContextLogoutHandler在这之后执行，会清除信息
                // SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            if (auth.getPrincipal() instanceof UserDetails userDetails) {
                String username = userDetails.getUsername();
                log.info("username: {}  is offline now", username);
            }
            return Mono.empty();
        }));
    }
}
