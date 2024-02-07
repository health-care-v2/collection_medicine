package com.example.collection_medicine.service;

import com.example.collection_medicine.domain.Medicine;
import com.example.collection_medicine.dto.MedicineResponse;
import com.example.collection_medicine.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
//@Transactional
@Service
public class CollectionService {
    @Value("${open-api.url}") private String apiUrl;
    @Value("${open-api.decoding-key}") private String decodingKey;

    private final MedicineRepository medicineRepository;

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

                List<Medicine> entityList = MedicineResponse.toEntityList(responseEntity.getBody());
                medicineRepository.saveAll(entityList);

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
