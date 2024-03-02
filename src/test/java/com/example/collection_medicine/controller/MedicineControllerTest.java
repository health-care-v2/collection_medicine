package com.example.collection_medicine.controller;

import com.example.collection_medicine.domain.Medicine;
import com.example.collection_medicine.dto.MedicineResponse;
import com.example.collection_medicine.global.util.ResponseDTO;
import com.example.collection_medicine.service.MedicineService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Medicine 컨트롤러 테스트")
@WebMvcTest(MedicineController.class)
class MedicineControllerTest {

    @Autowired private MockMvc mvc;

    @MockBean private MedicineService medicineService;

    @DisplayName("약 정보 조회 - productName 과 함께 호출 : 성공")
    @Test
    public void givenProductName_whenSearchMedicine_thenReturnsMedicine() throws Exception {
        // given
        String productName = "약이름1";
        MedicineResponse medicineResponse = createMedicineResponse();
        ResponseDTO<MedicineResponse> res = ResponseDTO.okWithData(medicineResponse);

        given(medicineService.searchMedicine(eq(productName))).willReturn(medicineResponse);

        // when & then
        mvc.perform(
                        get("/v2/medicines")
                                .queryParam("productName", productName)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(res.getCode()))
                .andExpect(jsonPath("$.data.company").value(res.getData().company()))
                .andExpect(jsonPath("$.data.productName").value(res.getData().productName()))
                .andExpect(jsonPath("$.data.efficacy").value(res.getData().efficacy()))
                .andExpect(jsonPath("$.data.useMethod").value(res.getData().useMethod()))
                .andExpect(jsonPath("$.data.precautionWaring").value(res.getData().precautionWaring()))
                .andExpect(jsonPath("$.data.caution").value(res.getData().caution()))
                .andExpect(jsonPath("$.data.interaction").value(res.getData().interaction()))
                .andExpect(jsonPath("$.data.sideEffect").value(res.getData().sideEffect()))
                .andExpect(jsonPath("$.data.storageMethod").value(res.getData().storageMethod()))
                .andExpect(jsonPath("$.data.image").value(res.getData().image()))
                .andExpect(jsonPath("$.message").value(res.getMessage()));
        then(medicineService).should().searchMedicine(eq(productName));
        verify(medicineService, times(1)).searchMedicine(productName);
    }

    @DisplayName("약 정보 조회 - productName 과 함께 호출 : 서버 에러")
    @Test
    public void givenNotExitProductName_whenSearchMedicine_thenReturnsError() throws Exception {
        // given
        String productName = "약이름2";
        MedicineResponse medicineResponse = createMedicineResponse();
        ResponseDTO<MedicineResponse> res = ResponseDTO.okWithData(medicineResponse);

        given(medicineService.searchMedicine(eq(productName)))
                .willThrow(new EntityNotFoundException("제품명이 없습니다. productName = " + productName));

        // when & then
        mvc.perform(
                        get("/v2/medicines")
                                .queryParam("productName", productName)
                )
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("서버 에러!"));
        verify(medicineService, times(1)).searchMedicine(productName);
    }

    private MedicineResponse createMedicineResponse() {
        return MedicineResponse.of(createMedicine());
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