package com.example.collection_medicine.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(columnList = "productName")
})
@Entity
public class Medicine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4000) private String company; // 업체명
    @Column(length = 4000) private String productName; // 제품명
    @Column(length = 400000000) private String efficacy; // 효능
    @Column(length = 400000000) private String useMethod; // 사용법
    @Column(length = 400000000) private String precautionsWarning; // 주의사항 경고
    @Column(length = 400000000) private String caution; // 주의사항
    @Column(length = 400000000) private String interaction; // 상호작용
    @Column(length = 400000000) private String sideEffect; // 부작용
    @Column(length = 400000000) private String storageMethod; // 보관법
    @Column(length = 3000) private String image; // 이미지

    private Medicine(String company, String productName, String efficacy, String useMethod,
                     String precautionsWarning, String caution, String interaction, String sideEffect,
                     String storageMethod, String image) {
        this.company = company;
        this.productName = productName;
        this.efficacy = efficacy;
        this.useMethod = useMethod;
        this.precautionsWarning = precautionsWarning;
        this.caution = caution;
        this.interaction = interaction;
        this.sideEffect = sideEffect;
        this.storageMethod = storageMethod;
        this.image = image;
    }

    public static Medicine of(String company, String productName, String efficacy, String useMethod,
                              String precautionsWarning, String caution, String interaction, String sideEffect,
                              String storageMethod, String image) {
        return new Medicine(company, productName, efficacy, useMethod, precautionsWarning,
                caution, interaction, sideEffect, storageMethod, image);
    }

}
