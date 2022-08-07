package com.lwohvye.gateway.security.security.handler;

import com.lwohvye.gateway.rabbitmq.config.RabbitMQGatewayConfig;
import com.lwohvye.gateway.rabbitmq.service.RabbitMQProducerService;
import com.lwohvye.utils.json.JsonUtils;
import com.lwohvye.utils.rabbitmq.AmqpMsgEntity;
import com.lwohvye.utils.result.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

/**
 * @author ShiLei
 * @version 1.0.0
 * @date 2021/3/11 15:14
 * @description 登录失败处理
 */
@Component
public class CustomAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {

    @Autowired
    private RabbitMQProducerService rabbitMQProducerService;

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        return Mono.defer(() -> Mono.just(webFilterExchange.getExchange()
                .getResponse()).flatMap(response -> {
            var dataBufferFactory = response.bufferFactory();

            var request = webFilterExchange.getExchange().getRequest();
            if (response.isCommitted()) return response.setComplete();

            // 针对于密码错误行为，需进行记录
            if (exception instanceof BadCredentialsException) {
                var username = Optional.ofNullable(request.getHeaders().get("customer")).orElseGet(ArrayList::new).get(0);

                if (Objects.nonNull(username)) {
                    var hostName = request.getLocalAddress().getHostName();
                    var lockedIp = hostName + "||authLocked||";

                    var infoMap = new HashMap<String, Object>();
                    infoMap.put("ip", hostName);
                    infoMap.put("username", username);
                    infoMap.put("lockedIp", lockedIp);
                    var authFailedMsg = new AmqpMsgEntity().setMsgType("auth").setMsgData(JsonUtils.toJSONString(infoMap)).setExtraData("solveAuthFailed");
                    //  发送消息
                    rabbitMQProducerService.sendMsg(RabbitMQGatewayConfig.DIRECT_SYNC_EXCHANGE, RabbitMQGatewayConfig.AUTH_LOCAL_ROUTE_KEY, authFailedMsg);
                }
            }
            // 返回错误信息。用下面的sendError会被EntryPoint拦截并覆盖。
            var dataBuffer = dataBufferFactory.wrap(JsonUtils.toJSONString(ResultInfo.validateFailed(exception.getMessage())).getBytes());
            return response.writeWith(Mono.just(dataBuffer));
        }));
    }
}
