package com.example.collection_medicine.dto;

import com.example.collection_medicine.domain.Medicine;

public record MedicineResponse(String company,
                               String productName,
                               String efficacy,
                               String useMethod,
                               String precautionWaring,
                               String caution,
                               String interaction,
                               String sideEffect,
                               String storageMethod,
                               String image) {

    public static MedicineResponse of(Medicine medicine) {
        return new MedicineResponse(medicine.getCompany(),
                medicine.getProductName(),
                medicine.getEfficacy(),
                medicine.getUseMethod(),
                medicine.getPrecautionsWarning(),
                medicine.getCaution(),
                medicine.getInteraction(),
                medicine.getSideEffect(),
                medicine.getStorageMethod(),
                medicine.getImage());
    }
}
