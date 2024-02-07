package com.example.collection_medicine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record MedicineResponse(
        @JsonProperty("header") Header header,
        @JsonProperty("body") Body body
) {

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
