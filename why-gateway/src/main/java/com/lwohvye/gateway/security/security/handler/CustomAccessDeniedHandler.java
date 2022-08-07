package com.lwohvye.gateway.security.security.handler;

import com.lwohvye.utils.json.JsonUtils;
import com.lwohvye.utils.result.ResultInfo;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * @author ShiLei
 * @version 1.0.0
 * @date 2021/3/11 11:12
 * @description 鉴权管理
 */
@Component
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return Mono.defer(() -> Mono.just(exchange.getResponse())).flatMap(response -> {
            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            DataBufferFactory dataBufferFactory = response.bufferFactory();
            var result = JsonUtils.toJSONString(ResultInfo.forbidden(denied.getMessage()));
            DataBuffer buffer = dataBufferFactory.wrap(result.getBytes(Charset.defaultCharset()));
            return response.writeWith(Mono.just(buffer));
        });
    }
}
