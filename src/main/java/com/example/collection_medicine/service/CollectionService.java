package com.example.collection_medicine.service;

import com.example.collection_medicine.domain.Medicine;
import com.example.collection_medicine.dto.MedicineCompareDto;
import com.example.collection_medicine.dto.OpenApiMedicineResponse;
import com.example.collection_medicine.repository.MedicineCustomRepository;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.collection_medicine.dto.constant.DmFlag.CREATE;
import static com.example.collection_medicine.dto.constant.DmFlag.UPDATE;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CollectionService {
    @Value("${open-api.url}") private String apiUrl;
    @Value("${open-api.decoding-key}") private String decodingKey;

    private final RestTemplate restTemplate;
    private final MedicineCustomRepository medicineCustomRepository;
    private final MedicineRepository medicineRepository;
    private final CompareService compareService;

    @Scheduled(initialDelayString = "${scheduled.initial-delay}", fixedRateString = "${scheduled.fixed-rate}", timeUnit = TimeUnit.SECONDS)
    public void collectMedicine() {
        List<Medicine> dbMedicines = medicineRepository.findAll();
        List<Medicine> apiMedicines = fetchMedicinesFromApi();

        List<MedicineCompareDto> dbMedicineDtoList = dbMedicines.stream()
                .map(MedicineCompareDto::from)
                .collect(Collectors.toList());
        List<MedicineCompareDto> apiMedicineDtoList = apiMedicines.stream()
                .map(MedicineCompareDto::from)
                .collect(Collectors.toList());

        // db 동기화를 위해 db 데이터와 api 데이터를 비교한다.
        compareService.medicineCompare(dbMedicineDtoList, apiMedicineDtoList);

        List<Medicine> insertMedicines = apiMedicineDtoList.stream()
                .filter(medicineCompareDto -> CREATE == medicineCompareDto.getDmFlag())
                .map(MedicineCompareDto::toEntity)
                .collect(Collectors.toList());
        log.info("insert size {}", insertMedicines.size());
        List<Medicine> updateMedicines = apiMedicineDtoList.stream()
                .filter(medicineCompareDto -> UPDATE == medicineCompareDto.getDmFlag())
                .map(MedicineCompareDto::toEntity)
                .collect(Collectors.toList());
        log.info("update size {}", updateMedicines.size());

        medicineCustomRepository.bulkSave(insertMedicines);
        medicineCustomRepository.bulkUpdate(updateMedicines);
    }

    private List<Medicine> fetchMedicinesFromApi() {
        List<Medicine> medicines = new ArrayList<>();

        int maxPage = 1;
        for (int pageNo = 0; pageNo < 1; pageNo++) {
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

        log.info(builder.toUriString());

        return builder.build().encode().toUri();
    }

}
