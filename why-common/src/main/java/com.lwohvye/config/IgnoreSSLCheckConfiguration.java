package com.lwohvye.config;

import feign.Client;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * 忽略https证书校验
 *
 * @date 2022/7/30 10:20 PM
 */
@Configuration
public class IgnoreSSLCheckConfiguration {

    @Bean
    public Client feignClient() {
        return new Client.Default(getSSLSocketFactory(), NoopHostnameVerifier.INSTANCE);
    }

    private SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustAllStrategy()).build();
            return sslContext.getSocketFactory();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
