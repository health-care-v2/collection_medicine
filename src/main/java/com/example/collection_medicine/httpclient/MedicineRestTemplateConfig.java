package com.example.collection_medicine.httpclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class MedicineRestTemplateConfig {
    @Value("${rest-template.connect-timeout}") private int connectTimeout;
    @Value("${rest-template.read-timeout}") private int readTimeout;

    @Bean
    public MedicineRestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = MedicineRestTemplateBuilder.get()
                .setConnectTimeout(Duration.ofSeconds(connectTimeout))
                .setReadTimeout(Duration.ofSeconds(readTimeout))
                .build();

        return new MedicineRestTemplate(restTemplate);
    }

}
