package com.example.collection_medicine.service;

import com.example.collection_medicine.dto.MedicineResponse;
import com.example.collection_medicine.repository.MedicineRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MedicineService {
    private final MedicineRepository medicineRepository;


    public MedicineResponse searchMedicine(String productName) {
        return medicineRepository.findByProductName(productName)
                .map(MedicineResponse::of)
                .orElseThrow(() -> new EntityNotFoundException("제품명이 없습니다. productName = " + productName));
    }
}
