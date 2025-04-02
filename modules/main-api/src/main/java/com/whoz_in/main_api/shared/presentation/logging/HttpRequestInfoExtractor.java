package com.whoz_in.main_api.shared.presentation.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

// 사람이 읽을 수 있게 파싱하는 역할
@Component
@RequiredArgsConstructor
public class HttpRequestInfoExtractor {
    private final ObjectMapper objectMapper;
    public String extractAll(HttpServletRequest request) {
        return "IP: " + extractIp(request) + "\n"
                + "URL: " + extractUriAndMethod(request) + "\n"
                + "QUERY PARAMS: " + extractQueryParams(request) + "\n"
                + "COOKIES: " + extractCookies(request) + "\n"
                + "BODY: " + extractRequestBody(request);
    }
    public String extractIp(HttpServletRequest request){
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }
    public String extractUriAndMethod(HttpServletRequest request){
        return request.getRequestURI() + "(" + request.getMethod() +")";
    }

    public String extractQueryParams(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        return paramMap.isEmpty() ? "없음" : paramMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
                .collect(Collectors.joining(", "));
    }
    public String extractCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) return "없음";
        return Arrays.stream(cookies)
                .map(cookie -> {
                    String cookieValue = (cookie.getValue().length() >= 30 ?
                            cookie.getValue().substring(0, 15) + "..." + cookie.getValue().substring(cookie.getValue().length() - 15)
                            : cookie.getValue());
                    return cookie.getName() + "=" + cookieValue;
                }).toList().toString();
    }
    public String extractRequestBody(HttpServletRequest request) {
        if (request.getContentLength() == 0) return "없음";
        ContentCachingRequestWrapper wrapper = unwrap(request);
        if (wrapper == null)
            return "감싸지 않은 요청은 읽을 수 없음";

        byte[] buf = wrapper.getContentAsByteArray();
        // 요청 inputstream이 소비될 때 ContentCachingRequestWrapper이 바디를 캐싱함
        if (buf.length == 0) return "바디가 없거나 읽히지 않았음";
        try {
            return objectMapper.readTree(buf).toPrettyString();
        } catch (IOException e) {
            return "바디 JSON 변환 실패";
        }
    }

    private ContentCachingRequestWrapper unwrap(HttpServletRequest request) {
        while (!(request instanceof ContentCachingRequestWrapper) && request instanceof HttpServletRequestWrapper) {
            request = (HttpServletRequest) ((HttpServletRequestWrapper) request).getRequest();
        }
        return (request instanceof ContentCachingRequestWrapper) ? (ContentCachingRequestWrapper) request : null;
    }
}
