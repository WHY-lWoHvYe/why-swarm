/*
 *    Copyright (c) 2022.  lWoHvYe(Hongyan Wang)
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

package com.lwohvye.gateway.rabbitmq.config;

import com.lwohvye.config.LocalCoreConfig;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqGatewayConfig {

    // region 交换机
    // direct交换机
    public static final String DIRECT_SYNC_EXCHANGE = "sync_direct_exchange";
    // topic交换机
    public static final String TOPIC_SYNC_EXCHANGE = "sync_topic_exchange";
    // 延迟队列交换机
    public static final String DIRECT_SYNC_TTL_EXCHANGE = "sync_direct_ttl_exchange";
    // 延迟队列交换机-插件版
    public static final String DIRECT_SYNC_DELAY_EXCHANGE = "sync_delay_direct_exchange";
    public static final String TOPIC_SYNC_DELAY_EXCHANGE = "sync_delay_topic_exchange";
    // endregion

    // region 路由键
    public static final String DATA_SYNC_ROUTE_KEY = "data.sync";

    public static final String DATA_SYNC_TTL_ROUTE_KEY = "data.sync.ttl";

    public static final String DATA_COMMON_DELAY_ROUTE_KEY = "data.common.delay";

    public static final String AUTH_LOCAL_ROUTE_KEY = "auth.local";

    public static final String LOG_ROUTER_KEY = "gateway.local.log";

    public static final String SP_SYNC_ROUTE_KEY = "sp.sync.x0x"; // 对应topic   sp.sync.*
    // endregion

    // region 队列
    public static final String DATA_SYNC_QUEUE = "data.sync.queue";

    public static final String DATA_SYNC_TTL_QUEUE = "data.sync.ttl.queue";

    public static final String DATA_COMMON_DELAY_QUEUE = "data.common.delay.queue";


    // endregion

    /**
     * 消费队列所绑定的交换机
     */
    @Bean
    DirectExchange dataSyncDirect() {
        return ExchangeBuilder
                .directExchange(DIRECT_SYNC_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * 实际消费队列
     */
    @Bean
    public Queue dataSyncQueue() {
        return new Queue(DATA_SYNC_QUEUE);
    }

    /**
     * 将消费队列绑定到交换机
     */
    @Bean
    Binding dataSyncBinding(DirectExchange dataSyncDirect, Queue dataSyncQueue) {
        return BindingBuilder
                .bind(dataSyncQueue)
                .to(dataSyncDirect)
                .with(DATA_SYNC_ROUTE_KEY);
    }

    /**
     * 延迟队列交换机-插件
     * direct模式
     *
     * @return ex
     */
    @Bean
    public CustomExchange dataDelayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DIRECT_SYNC_DELAY_EXCHANGE, "x-delayed-message", true, false, args);
    }

    /**
     * 延迟队列-插件
     *
     * @return q
     */
    @Bean
    public Queue dataDelayQueue() {
        return QueueBuilder.durable(DATA_COMMON_DELAY_QUEUE).build();
    }

    /**
     * 给延时队列绑定交换机-插件
     *
     * @return binding
     */
    @Bean
    public Binding delayBinding(CustomExchange dataDelayExchange, Queue dataDelayQueue) {
        return BindingBuilder
                .bind(dataDelayQueue)
                .to(dataDelayExchange)
                .with(DATA_COMMON_DELAY_ROUTE_KEY)
                .noargs();
    }

    @Bean
    public Binding delayBinding2(DirectExchange dataSyncDirect, Queue dataDelayQueue) {
        return BindingBuilder
                .bind(dataDelayQueue)
                .to(dataSyncDirect)
                .with(AUTH_LOCAL_ROUTE_KEY);
    }

    /**
     * topic交换机。支持路由通配符 *代表一个单词 #代表零个或多个单词
     *
     * @return org.springframework.amqp.core.TopicExchange
     * @date 2021/9/30 10:25 上午
     */
    @Bean
    public TopicExchange topicYExchange() {
        return ExchangeBuilder
                .topicExchange(TOPIC_SYNC_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * 延迟队列交换机-插件
     * topic模式
     * https://github.com/rabbitmq/rabbitmq-delayed-message-exchange
     *
     * @return ex
     */
    @Bean
    public CustomExchange topicDelayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "topic");
        return new CustomExchange(TOPIC_SYNC_DELAY_EXCHANGE, "x-delayed-message", true, false, args);
    }

    /**
     * 该队列，集群各实例配置不相同，实现同一事件被各实例都消费，比如更新本地缓存
     *
     * @return org.springframework.amqp.core.Queue
     * @date 2022/3/8 10:22 AM
     */
    @Bean
    public Queue spSyncQueue() {
        return QueueBuilder
                .durable(LocalCoreConfig.SP_SYNC_DELAY_QUEUE)
                .build();
    }

    @Bean
    public Binding spSyncBinding(CustomExchange topicDelayExchange, Queue spSyncQueue) {
        return BindingBuilder.bind(spSyncQueue).to(topicDelayExchange).with("sp.sync.*").noargs();
    }
}
