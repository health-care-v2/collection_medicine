package com.example.collection_medicine.dto;

import com.example.collection_medicine.domain.Medicine;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record MedicineResponse(
        @JsonProperty("header") Header header,
        @JsonProperty("body") Body body
) {

    public static List<Medicine> toEntityList(MedicineResponse response) {
        List<Medicine> entityList = new ArrayList<>();

        for(Body.Item item : response.body().items) {
            entityList.add(Medicine.of(
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
            ));
        }

        return entityList;
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
