package com.example.core.web.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    @Value("${payment.port_one.rest_api_key}")
    private String apiKey;
    @Value("${payment.port_one.rest_api_secret}")
    private String apiSecret;

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, apiSecret);
    }
}
