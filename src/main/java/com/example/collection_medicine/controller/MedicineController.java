package com.example.collection_medicine.controller;

import com.example.collection_medicine.dto.MedicineResponse;
import com.example.collection_medicine.service.MedicineService;
import com.example.collection_medicine.global.util.ResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v2/medicines")
@RestController
public class MedicineController {
    private final MedicineService medicineService;

    @GetMapping
    public ResponseEntity<ResponseDTO<MedicineResponse>> medicines(@RequestParam String productName) {
        return ResponseEntity.ok(ResponseDTO.okWithData(medicineService.searchMedicine(productName)));
    }

}
