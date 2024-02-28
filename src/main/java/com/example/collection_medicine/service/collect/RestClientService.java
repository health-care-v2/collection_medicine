package com.example.collection_medicine.service.collect;

import com.example.collection_medicine.domain.Medicine;
import com.example.collection_medicine.dto.OpenApiMedicineResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RestClientService {
    @Value("${open-api.url}") private String apiUrl;
    @Value("${open-api.decoding-key}") private String decodingKey;

    private final RestTemplate restTemplate;

    public List<Medicine> fetchMedicinesFromApi() {
        List<Medicine> medicines = new ArrayList<>();

        int maxPage = 1;
        for (int pageNo = 0; pageNo < maxPage; pageNo++) {
            URI uri = createUrl(pageNo + 1);

            try {
                ResponseEntity<OpenApiMedicineResponse> response = restTemplate.exchange(
                        uri, HttpMethod.GET, null, OpenApiMedicineResponse.class);
                log.info("response header = {}", response.getBody().header());
                log.info("response body = {}", response.getBody().body());

                List<Medicine> resMedicines = OpenApiMedicineResponse.toEntityList(response.getBody());
                medicines.addAll(resMedicines);

                if (pageNo == 0) {
                    maxPage = response.getBody().body().totalCount();
                }
            } catch (RestClientException e) {
                log.warn("Request error. {}", e.getLocalizedMessage());
            }
        }

        return medicines;
    }

    private URI createUrl(int pageNo) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("serviceKey", decodingKey)
                .queryParam("pageNo", pageNo)
                .queryParam("type", "json");

        return builder.build().encode().toUri();
    }

}
