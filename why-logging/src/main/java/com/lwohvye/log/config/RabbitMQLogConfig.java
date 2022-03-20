package com.lwohvye.log.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQLogConfig {


    public static final String TOPIC_SYNC_EXCHANGE = "sync_topic_exchange";

    public static final String LOG_ROUTER_KEY = "*.local.log";

    public static final String LOGGING_QUEUE = "xxx.log.queue";

    @Bean
    public TopicExchange topicYExchange() {
        return ExchangeBuilder
                .topicExchange(TOPIC_SYNC_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Queue xLogQueue() {
        return QueueBuilder.durable(LOGGING_QUEUE).build();
    }

    @Bean
    public Binding xLogBinding(TopicExchange topicYExchange, Queue xLogQueue) {
        return BindingBuilder
                .bind(xLogQueue)
                .to(topicYExchange)
                .with(LOG_ROUTER_KEY);
    }
}
