package com.example.collection_medicine.httpclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;

public class MedicineRestTemplateBuilder {
    @Value("${open-api.url}") private static String apiUrl;

    public static RestTemplateBuilder get() {
        return new RestTemplateBuilder()
                .errorHandler(new MedicineRestClientErrorHandler());
    }

}
