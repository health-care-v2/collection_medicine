package com.example.collection_medicine.service;

import com.example.collection_medicine.domain.Medicine;
import com.example.collection_medicine.dto.MedicineCompareDto;
import com.example.collection_medicine.dto.constant.DmFlag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CompareService - 테스트")
@ExtendWith(MockitoExtension.class)
class CompareServiceTest {

    @InjectMocks private CompareService sut;

    @DisplayName("약 비교 - NONE")
    @Test
    void givenCompareDto_whenMedicineCompare_thenCompareNoneResult() {
        List<MedicineCompareDto> dbDtoList = Arrays.asList(MedicineCompareDto.from(createMedicine1()));
        List<MedicineCompareDto> apiDtoList = Arrays.asList(MedicineCompareDto.from(createMedicine1()));

        sut.medicineCompare(dbDtoList, apiDtoList);

        assertThat(apiDtoList.get(0).getDmFlag()).isEqualTo(DmFlag.NONE);
    }

    @DisplayName("약 비교 - UPDATE")
    @Test
    void givenCompareDto_whenMedicineCompare_thenCompareCreateResult() {
        List<MedicineCompareDto> dbDtoList = Arrays.asList(MedicineCompareDto.from(createMedicine1()));
        List<MedicineCompareDto> apiDtoList = Arrays.asList(MedicineCompareDto.from(createMedicine2()));

        sut.medicineCompare(dbDtoList, apiDtoList);

        assertThat(apiDtoList.get(0).getDmFlag()).isEqualTo(DmFlag.UPDATE);
    }

    @DisplayName("약 비교 - CREATE")
    @Test
    void givenCompareDto_whenMedicineCompare_thenCompareUpdateResult() {
        List<MedicineCompareDto> dbDtoList = Arrays.asList(MedicineCompareDto.from(createMedicine1()));
        List<MedicineCompareDto> apiDtoList = Arrays.asList(MedicineCompareDto.from(createMedicine3()));

        sut.medicineCompare(dbDtoList, apiDtoList);

        assertThat(apiDtoList.get(0).getDmFlag()).isEqualTo(DmFlag.CREATE);
    }

    private Medicine createMedicine1() {
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

    private static Medicine createMedicine2() {
        return Medicine.of("제조사1",
                "약이름1",
                "효능2",
                "사용방법1",
                "주의사항 경고1",
                "주의사항1",
                "상호작용1",
                "부작용1",
                "보관방법1",
                "이미지1");
    }

    private static Medicine createMedicine3() {
        return Medicine.of("제조사2",
                "약이름1",
                "효능1",
                "사용방법1",
                "주의사항 경고1",
                "주의사항1",
                "상호작용1",
                "부작용1",
                "보관방법1",
                "이미지1");
    }

}