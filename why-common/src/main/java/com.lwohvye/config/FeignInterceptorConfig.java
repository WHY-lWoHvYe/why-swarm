package com.lwohvye.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 有定义Bean和实现接口两种方式
 *
 * @date 2022/7/28 8:58 PM
 */
@Configuration
public class FeignInterceptorConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
//         这里有一点，feign底层是通过RequestTemplate发起Request的，Request交给Client处理，Client可以是HttpUrlConnection, HttpClient 或 Okhttp
        return requestTemplate -> {
            // Do Something before feign-call
            // 这里是Support Map类型的参数的
            requestTemplate.header("TNT", "Boom!!");
        };
    }
}
