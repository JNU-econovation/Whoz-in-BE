package com.whoz_in.main_api.shared.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whoz_in.main_api.config.RequestCachingFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
@RequiredArgsConstructor
public class HttpRequestInfoExtractor {
    private final ObjectMapper objectMapper;
    public String extractInfoFrom(HttpServletRequest request) {
        return "IP: " + request.getRemoteAddr() + "\n"
                + "URL: " + request.getRequestURI() + ", METHOD: " + request.getMethod() + "\n"
                + "QUERY PARAMS: " + getQueryParams(request) + "\n"
                + "COOKIES: " + getCookies(request) + "\n"
                + "BODY: " + getRequestBody(request);
    }
    private String getQueryParams(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        return paramMap.isEmpty() ? "없음" : paramMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
                .collect(Collectors.joining(", "));
    }
    private String getCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) return "없음";
        return Arrays.stream(cookies)
                .map(cookie -> cookie.getName() + "=" + cookie.getValue())
                .collect(Collectors.joining(", "));
    }
    private String getRequestBody(HttpServletRequest request) {
        if (request.getContentLength() == 0) return "없음";

        if (!(request instanceof ContentCachingRequestWrapper wrapper)) return "감싸지 않은 요청은 읽을 수 없음";

        byte[] buf = wrapper.getContentAsByteArray();
        // 요청 inputstream이 소비될 때 ContentCachingRequestWrapper이 바디를 캐싱함
        if (buf.length == 0) return "바디가 읽히지 않았음";
        try {
            return objectMapper.readTree(buf).toPrettyString();
        } catch (IOException e) {
            return "바디 JSON 변환 실패";
        }
    }
}
