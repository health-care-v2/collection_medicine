package com.example.collection_medicine.service;


import com.example.collection_medicine.domain.Medicine;
import com.example.collection_medicine.dto.MedicineResponse;
import com.example.collection_medicine.repository.MedicineRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@DisplayName("MedicineService - 테스트")
@ExtendWith(MockitoExtension.class)
class MedicineServiceTest {
    @InjectMocks private MedicineService sut;

    @Mock private MedicineRepository medicineRepository;

    @DisplayName("약이름으로 조회하면 약 정보를 반환한다.")
    @Test
    void givenProductName_whenSearchMedicine_thenReturnMedicineResponse() {
        // given
        Medicine medicine = createMedicine();
        String productName = "약이름1";
        given(medicineRepository.findByProductName(productName)).willReturn(Optional.of(medicine));

        // when
        MedicineResponse result = sut.searchMedicine(productName);

        // then
        assertThat(result).isNotNull();
        assertThat(result.company()).isEqualTo(medicine.getCompany());
        assertThat(result.productName()).isEqualTo(medicine.getProductName());
        assertThat(result.efficacy()).isEqualTo(medicine.getEfficacy());
        assertThat(result.useMethod()).isEqualTo(medicine.getUseMethod());
        assertThat(result.precautionWaring()).isEqualTo(medicine.getPrecautionsWarning());
        assertThat(result.caution()).isEqualTo(medicine.getCaution());
        assertThat(result.interaction()).isEqualTo(medicine.getInteraction());
        assertThat(result.sideEffect()).isEqualTo(medicine.getSideEffect());
        assertThat(result.storageMethod()).isEqualTo(medicine.getStorageMethod());
        assertThat(result.image()).isEqualTo(medicine.getImage());

        verify(medicineRepository, times(1)).findByProductName(productName);
    }

    @DisplayName("존재하지 않는 약이름으로 조회하면 약 예외가 발생한다.")
    @Test
    void givenProductName_whenSearchMedicine_thenThrowEntityNotFoundException() {
        // given
        String productName = "약이름2";
        given(medicineRepository.findByProductName(productName)).willReturn(Optional.empty());

        // when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            sut.searchMedicine(productName);
        });
        assertThat(exception.getMessage()).isEqualTo("제품명이 없습니다. productName = " + productName);
        verify(medicineRepository, times(1)).findByProductName(productName);
    }

    private Medicine createMedicine() {
        return Medicine.of("제조사1",
                "약이름1",
                "효능1",
                "사용방법1",
                "주의사항 경고1",
                "주의사항1",
                "상호작용1",
                "부작용1",
                "보관방법1",
                "이미지");
    }

}