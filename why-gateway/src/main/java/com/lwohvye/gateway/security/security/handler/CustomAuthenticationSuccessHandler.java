package com.lwohvye.gateway.security.security.handler;

import com.lwohvye.gateway.rabbitmq.config.RabbitMQGatewayConfig;
import com.lwohvye.gateway.rabbitmq.service.RabbitMQProducerService;
import com.lwohvye.gateway.security.config.bean.SecurityProperties;
import com.lwohvye.gateway.security.security.TokenProvider;
import com.lwohvye.gateway.security.service.dto.JwtUserDto;
import com.lwohvye.utils.json.JsonUtils;
import com.lwohvye.utils.rabbitmq.AmqpMsgEntity;
import com.lwohvye.utils.result.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author ShiLei
 * @version 1.0.0
 * @date 2021/3/11 15:00
 * @description 登录成功处理
 */
@Component
public class CustomAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private SecurityProperties properties;

    @Autowired
    private RabbitMQProducerService rabbitMQProducerService;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        return Mono.defer(() -> Mono.just(webFilterExchange.getExchange().getResponse()).flatMap(response -> {
            var dataBufferFactory = response.bufferFactory();
            if (response.isCommitted()) return response.setComplete();

            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 生成令牌与第三方系统获取令牌方式
            // UserDetails userDetails = userDetailsService.loadUserByUsername(userInfo.getUsername());
            // Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // SecurityContextHolder.getContext().setAuthentication(authentication);
            var token = tokenProvider.createToken(authentication, true);
            var refreshToken = tokenProvider.createToken(authentication, false);
            final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
            // 用户登录成功后，写一条消息
            var authSuccessMsg = new AmqpMsgEntity().setMsgType("authSave").setMsgData(jwtUserDto.getUser().toString()).setExtraData("saveAuthorizeLog");
            rabbitMQProducerService.sendMsg(RabbitMQGatewayConfig.DIRECT_SYNC_EXCHANGE, RabbitMQGatewayConfig.AUTH_LOCAL_ROUTE_KEY, authSuccessMsg);

            // 返回 token 与 用户信息
            var authInfo = Map.of("token", properties.getTokenStartWith() + token, "refreshToken", refreshToken, "user", jwtUserDto);
            // 这里需要进行响应
            var dataBuffer = dataBufferFactory.wrap(JsonUtils.toJSONString(ResultInfo.success(authInfo)).getBytes());
            return response.writeWith(Mono.just(dataBuffer));
        }));
    }
}
