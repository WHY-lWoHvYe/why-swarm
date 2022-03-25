package com.lwohvye.tools.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolsMqConfig {

    public static final String DIRECT_SYNC_EXCHANGE = "sync_direct_exchange";

    public static final String MAIL_ROUTE_KEY = "common.mail";

    public static final String MAIL_COMMON_QUEUE = "common.mail.queue";

    @Bean
    DirectExchange dataSyncDirect() {
        return ExchangeBuilder
                .directExchange(DIRECT_SYNC_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    Queue mailQueue() {
        return QueueBuilder
                .durable(MAIL_COMMON_QUEUE)
                .build();
    }

    @Bean
    Binding mailBinding(DirectExchange dataSyncDirect, Queue mailQueue) {
        return BindingBuilder
                .bind(mailQueue)
                .to(dataSyncDirect)
                .with(MAIL_ROUTE_KEY);
    }
}
