package com.example.collection_medicine.service.collect;

import com.example.collection_medicine.domain.Medicine;
import com.example.collection_medicine.dto.MedicineCompareDto;
import com.example.collection_medicine.repository.MedicineCustomRepository;
import com.example.collection_medicine.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final MedicineCustomRepository medicineCustomRepository;
    private final MedicineRepository medicineRepository;
    private final CompareService compareService;
    private final RestClientService restClientService;

    @Scheduled(initialDelayString = "${scheduled.initial-delay}", fixedRateString = "${scheduled.fixed-rate}", timeUnit = TimeUnit.SECONDS)
    public void collectMedicine() {
        List<Medicine> dbMedicines = medicineRepository.findAll();
        List<Medicine> apiMedicines = restClientService.fetchMedicinesFromApi();

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

}
