package com.example.collection_medicine.service;

import com.example.collection_medicine.dto.MedicineResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class CollectionService {
    @Value("${open-api.url}") private String apiUrl;
    @Value("${open-api.decoding-key}") private String decodingKey;

    @Scheduled(initialDelayString = "${scheduled.initial-delay}", fixedRateString = "${scheduled.fixed-rate}", timeUnit = TimeUnit.SECONDS)
    public void collectMedicine() throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        int pageNo = 1;

        for (int i = 0; i < pageNo; i++) {
            URI uri = createUrl(i + 1);

            try {
                ResponseEntity<MedicineResponse> responseEntity = restTemplate.exchange(
                        uri, HttpMethod.GET, null, MedicineResponse.class);
                log.info("response header = {}", responseEntity.getBody().header());
                log.info("response body = {}", responseEntity.getBody().body());
                if (i == 0) {
                    pageNo = responseEntity.getBody().body().totalCount();
                }
            } catch (RestClientException e) {
                log.warn("Request error. {}", e.getLocalizedMessage());
            }
        }
    }

    private URI createUrl(int pageNo) throws UnsupportedEncodingException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("serviceKey", decodingKey)
                .queryParam("pageNo", pageNo)
                .queryParam("type", "json");

        log.info(builder.toUriString());

        return builder.build().encode().toUri();
    }

}
