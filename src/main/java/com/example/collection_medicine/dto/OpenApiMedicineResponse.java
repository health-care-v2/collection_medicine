package com.example.collection_medicine.dto;

import com.example.collection_medicine.domain.Medicine;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record OpenApiMedicineResponse(
        @JsonProperty("header") Header header,
        @JsonProperty("body") Body body
) {

    public static List<Medicine> toEntityList(OpenApiMedicineResponse response) {
        return response.body.items.stream()
                .map(item -> {
                    return Medicine.of(
                            item.entpName,
                            item.itemName,
                            item.efcyQesitm,
                            item.useMethodQesitm,
                            item.atpnWarnQesitm,
                            item.atpnQesitm,
                            item.intrcQesitm,
                            item.seQesitm,
                            item.depositMethodQesitm,
                            item.itemImage
                    );
                })
                .collect(Collectors.toList());
    }

    public record Header(String resultCode,
                         String resultMsg) {
    }

    public record Body(int pageNo,
                       int totalCount,
                       int numOfRows,
                       List<Item> items) {

        public record Item(String entpName,
                           String itemName,
                           String itemSeq,
                           String efcyQesitm,
                           String useMethodQesitm,
                           String atpnWarnQesitm,
                           String atpnQesitm,
                           String intrcQesitm,
                           String seQesitm,
                           String depositMethodQesitm,
                           String openDe,
                           String updateDe,
                           String itemImage) {
        }
    }

}
