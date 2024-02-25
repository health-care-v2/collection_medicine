package com.example.collection_medicine.dto;

import com.example.collection_medicine.domain.Medicine;
import com.example.collection_medicine.dto.constant.DmFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MedicineCompareDto {
    private String company;
    private String productName;
    private String efficacy;
    private String useMethod;
    private String precautionsWarning;
    private String caution;
    private String interaction;
    private String sideEffect;
    private String storageMethod;
    private String image;
    private DmFlag dmFlag;

    public String getKey() {
        return company + "-" + productName;
    }

    public static MedicineCompareDto from(Medicine medicine) {
        return new MedicineCompareDto(
                medicine.getCompany(),
                medicine.getProductName(),
                medicine.getEfficacy(),
                medicine.getUseMethod(),
                medicine.getPrecautionsWarning(),
                medicine.getCaution(),
                medicine.getInteraction(),
                medicine.getSideEffect(),
                medicine.getStorageMethod(),
                medicine.getImage(),
                DmFlag.NONE);
    }

    public Medicine toEntity() {
        return Medicine.of(
                company,
                productName,
                efficacy,
                useMethod,
                precautionsWarning,
                caution,
                interaction,
                sideEffect,
                storageMethod,
                image
        );
    }
}
