package com.example.collection_medicine.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Slf4j
public class MedicineRestClientErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return !response.getStatusCode().is2xxSuccessful(); // true이면 hadleError() 호출
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is4xxClientError()) {
            log.error("{} - 클라이언트 에러", response.getStatusCode());
        } else if (response.getStatusCode().is5xxServerError()) {
            log.error("{} - 서버 에러", response.getStatusCode());
        }
    }

}
