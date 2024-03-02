package com.example.collection_medicine.httpclient;

import com.example.collection_medicine.domain.Medicine;
import com.example.collection_medicine.dto.OpenApiMedicineResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MedicineRestTemplate {
    @Value("${open-api.url}") private String apiUrl;
    @Value("${open-api.decoding-key}") private String decodingKey;

    private static final int NUM_OF_ROWS = 100;

    private final RestTemplate restTemplate;

    public List<Medicine> getMedicines() {
        int maxPage = getPageCount();
        List<Medicine> medicines = new ArrayList<>();

        for (int pageNo = 1; pageNo <= maxPage; pageNo++) {
            URI uri = createIncludeGetParamsToUrl(pageNo);

            ResponseEntity<OpenApiMedicineResponse> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    OpenApiMedicineResponse.class);

            List<Medicine> resMedicines = OpenApiMedicineResponse.toEntityList(response.getBody());
            medicines.addAll(resMedicines);
        }

        return medicines;
    }

    private URI createIncludeGetParamsToUrl(int page) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("serviceKey", decodingKey)
                .queryParam("pageNo", page)
                .queryParam("numOfRows", NUM_OF_ROWS)
                .queryParam("type", "json");

        return builder.build().encode().toUri();
    }

    private int getPageCount() {
        URI uri = createIncludeGetParamsToUrl(1);

        ResponseEntity<OpenApiMedicineResponse> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                OpenApiMedicineResponse.class);

        int totalCount =  response.getBody().body().totalCount();

        return (int) Math.ceil((double) totalCount / NUM_OF_ROWS);
    }

}
