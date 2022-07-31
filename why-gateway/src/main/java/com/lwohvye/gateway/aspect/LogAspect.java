/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.lwohvye.gateway.aspect;


import com.lwohvye.config.LocalCoreConfig;
import com.lwohvye.gateway.rabbitmq.config.RabbitMQGatewayConfig;
import com.lwohvye.gateway.rabbitmq.service.RabbitMQProducerService;
import com.lwohvye.utils.RequestHolder;
import com.lwohvye.utils.SecurityUtils;
import com.lwohvye.utils.StringUtils;
import com.lwohvye.utils.ThrowableUtil;
import com.lwohvye.utils.json.JsonUtils;
import com.lwohvye.utils.rabbitmq.AmqpMsgEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class LogAspect {

    private final RabbitMQProducerService rabbitMQProducerService;

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.lwohvye.annotation.log.Log)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        currentTime.set(System.currentTimeMillis());
        result = joinPoint.proceed();
        var time = System.currentTimeMillis() - currentTime.get();
        currentTime.remove();
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        var logMap = Map.of("logType", "INFO", "time", time,
                "username", getUsername(), "browser", StringUtils.getBrowser(request), "ip", StringUtils.getIp(request), "joinPoint", joinPoint);
        var logMsg = new AmqpMsgEntity().setMsgType("APILog").setMsgData(JsonUtils.toJSONString(logMap)).setOrigin(LocalCoreConfig.ORIGIN);
        rabbitMQProducerService.sendMsg(RabbitMQGatewayConfig.TOPIC_SYNC_EXCHANGE, RabbitMQGatewayConfig.LOG_ROUTER_KEY, logMsg);
        return result;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        var time = System.currentTimeMillis() - currentTime.get();
        currentTime.remove();
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        var logMap = Map.of("logType", "ERROR", "time", time, "exceptionDetail", ThrowableUtil.getStackTrace(e).getBytes(),
                "username", getUsername(), "browser", StringUtils.getBrowser(request), "ip", StringUtils.getIp(request), "joinPoint", joinPoint);
        var logMsg = new AmqpMsgEntity().setMsgType("APILog").setMsgData(JsonUtils.toJSONString(logMap)).setOrigin(LocalCoreConfig.ORIGIN);
        rabbitMQProducerService.sendMsg(RabbitMQGatewayConfig.TOPIC_SYNC_EXCHANGE, RabbitMQGatewayConfig.LOG_ROUTER_KEY, logMsg);
    }

    public String getUsername() {
        try {
            return SecurityUtils.getCurrentUsername();
        } catch (Exception e) {
            return "";
        }
    }
}
