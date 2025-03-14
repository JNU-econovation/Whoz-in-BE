package com.whoz_in.network_api.main_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public final class CorsWriter {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String mainApiBaseUrl;
    private final String mainApiKey;

    public CorsWriter(RestTemplate restTemplate, ObjectMapper objectMapper,
            @Value("${main-api.base-url}") String mainApiBaseUrl,
            @Value("${main-api.api-key}") String mainApiKey) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.mainApiBaseUrl = mainApiBaseUrl;
        this.mainApiKey = mainApiKey;
    }

    //TODO: 다른 요청도 하게 되면 공통 로직 묶기
    public void write(String room, String origin) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", mainApiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(createRequestBody(room, origin), headers);

        try {
            restTemplate.exchange(
                    mainApiBaseUrl + "/internal/api/v1/cors-origin",
                    HttpMethod.PUT,
                    requestEntity,
                    Void.class
            );
        } catch (ResourceAccessException e){
            log.error("main api에 접근할 수 없음 : {}", e.getMessage()); //서버가 꺼져있는지 확인
        } catch (HttpClientErrorException.Forbidden e) {
            log.error("Api key 인증 실패 : {}", e.getMessage());
        } catch (Exception e) {
            log.error("알 수 없는 예외 : {}", e.getMessage());
        }
    }

    private String createRequestBody(String room, String origin) {
        try {
            return objectMapper.writeValueAsString(Map.of(
                    "room", room,
                    "origin", origin
            ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize request body", e);
        }
    }
}
