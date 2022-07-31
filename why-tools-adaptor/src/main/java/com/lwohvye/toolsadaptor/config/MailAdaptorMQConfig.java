package com.lwohvye.toolsadaptor.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MailAdaptorMQConfig {

    public static final String DIRECT_SYNC_EXCHANGE = "sync_direct_exchange";

    public static final String MAIL_ROUTE_KEY = "common.mail";

}
