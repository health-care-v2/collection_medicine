package com.example.collection_medicine.service.collect;


import com.example.collection_medicine.repository.MedicineCustomRepository;
import com.example.collection_medicine.repository.MedicineRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;


@DisplayName("CollectionServiceTest - 테스트")
@ExtendWith(MockitoExtension.class)
class CollectionServiceTest {
    @InjectMocks private CollectionService sut;

    @Mock private MedicineCustomRepository medicineCustomRepository;
    @Mock private MedicineRepository medicineRepository;
    @Mock private CompareService compareService;
    @Mock private RestClientService restClientService;

    @DisplayName("CollectionMedicine - 테스트")
    @Test
    void givenNothing_whenCollectingMedicine_thenDbUpdated() {
        // when & then
        sut.collectMedicine();

        verify(medicineRepository, times(1)).findAll();
        verify(restClientService, times(1)).fetchMedicinesFromApi();
        verify(compareService, times(1)).medicineCompare(anyList(), anyList());
        verify(medicineCustomRepository, times(1)).bulkSave(anyList());
        verify(medicineCustomRepository, times(1)).bulkUpdate(anyList());
    }

}