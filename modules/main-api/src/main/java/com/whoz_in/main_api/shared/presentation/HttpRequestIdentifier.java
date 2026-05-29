package com.whoz_in.main_api.shared.presentation;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

// 서버 간 요청인지 공개 api 요청인지 구분
@Component
public final class HttpRequestIdentifier {
    // 상수 관리 체계화되면 이거 분리해야 할듯
    public static final String NETWORK_API_PREFIX = "/network-api";
    public static final String OPEN_API_PREFIX = "/open-api";

    public boolean isInternal(HttpServletRequest request) {
        return request.getRequestURI().startsWith(NETWORK_API_PREFIX);
    }
    public boolean isPublic(HttpServletRequest request) {
        return !isInternal(request);
    }
}
