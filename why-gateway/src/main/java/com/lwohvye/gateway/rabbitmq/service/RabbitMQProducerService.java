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
package com.lwohvye.gateway.rabbitmq.service;

import cn.hutool.core.util.ObjectUtil;
import com.lwohvye.gateway.rabbitmq.config.RabbitMqGatewayConfig;
import com.lwohvye.utils.json.JsonUtils;
import com.lwohvye.utils.rabbitmq.AmqpMsgEntity;
import com.lwohvye.utils.rabbitmq.SimpleMQProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

// 发消息时指定了 交换机和路由键。 所以可以把发消息 和 队列的绑定分开，发消息方定义交换机、消费方定义队列 及队列与交换机、路由键的绑定。提高灵活性。
@Component
@Slf4j
public class RabbitMQProducerService extends SimpleMQProducerService {

    /**
     * 发送消息
     *
     * @param amqpMsgEntity
     * @date 2021/4/27 2:49 下午
     */
    public void sendMsg(AmqpMsgEntity amqpMsgEntity) {
        amqpTemplate.convertAndSend(RabbitMqGatewayConfig.DIRECT_SYNC_EXCHANGE, RabbitMqGatewayConfig.DATA_SYNC_ROUTE_KEY, JsonUtils.toJSONString(amqpMsgEntity));

    }

    /**
     * 通过延迟插件实现延迟消息
     *
     * @param commonEntity
     * @date 2021/7/26 1:17 下午
     */
    public void sendDelayMsg(AmqpMsgEntity commonEntity) {
        amqpTemplate.convertAndSend(RabbitMqGatewayConfig.DIRECT_SYNC_DELAY_EXCHANGE,
                RabbitMqGatewayConfig.DATA_COMMON_DELAY_ROUTE_KEY, JsonUtils.toJSONString(commonEntity),
                message -> {
                    var expire = commonEntity.getExpire();
                    var timeUnit = commonEntity.getTimeUnit();
                    if (ObjectUtil.isNotEmpty(expire) && ObjectUtil.isNotEmpty(timeUnit)) {
                        Long expireMill = TimeUnit.MILLISECONDS.convert(expire, timeUnit);
                        //通过给消息设置x-delay头来设置消息从交换机发送到队列的延迟时间；
                        message.getMessageProperties().setHeader("x-delay", expireMill);
                    }
                    return message;
                });
    }

    /**
     * 延迟消息，topic模式
     *
     * @param routeKey     路由键
     * @param commonEntity 消息体
     * @date 2021/9/30 1:38 下午
     */
    public void sendSyncDelayMsg(String routeKey, AmqpMsgEntity commonEntity) {
        amqpTemplate.convertAndSend(RabbitMqGatewayConfig.TOPIC_SYNC_DELAY_EXCHANGE, routeKey,
                JsonUtils.toJSONString(commonEntity),
                message -> {
                    // 延迟 500ms
                    message.getMessageProperties().setHeader("x-delay", 500L);
                    return message;
                });
    }
}
