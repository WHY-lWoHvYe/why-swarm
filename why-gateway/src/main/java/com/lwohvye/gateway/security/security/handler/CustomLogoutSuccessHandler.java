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

import com.lwohvye.utils.json.JsonUtils;
import com.lwohvye.utils.result.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * 退出成功，后续处理逻辑
 *
 * @date 2021/11/27 9:43 上午
 */
@Slf4j
public class CustomLogoutSuccessHandler implements ServerLogoutSuccessHandler {

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        return Mono.defer(() -> Mono.just(exchange.getExchange().getResponse()).flatMap(response -> {
                    var dataBufferFactory = response.bufferFactory();
                    // 当前过滤器链的配置，这里是拿不到信息的
                    if (Objects.isNull(authentication)) {
                        // 也不要视图通过这个拿，前面有handler专门清理这个信息的
                        // authentication = SecurityContextHolder.getContext().getAuthentication();
                        log.warn(" van：oh,boy why you're runaway ");
                    }
                    var result = JsonUtils.toJSONString(ResultInfo.success("退出成功"));
                    DataBuffer buffer = dataBufferFactory.wrap(result.getBytes(Charset.defaultCharset()));
                    return response.writeWith(Mono.just(buffer));
                })
        );
    }
}
