package com.whoz_in.main_api.shared.presentation;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.stereotype.Component;

@Component
public final class UserAgentParser {
    private final UserAgentAnalyzer analyzer; // static으로 만들면 메모리 반환 문제가 있다고 하니 인스턴스 필드로 유지하세요

    public UserAgentParser() {
        this.analyzer = UserAgentAnalyzer.newBuilder()
                .withField(UserAgent.OPERATING_SYSTEM_NAME)
                .withField(UserAgent.DEVICE_CLASS)
                .withCache(1000)
                .build();
    }

    public UserAgent parse(HttpServletRequest request) {
        Map<String, String> headers = Collections.list(request.getHeaderNames()).stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        request::getHeader
                ));
        return analyzer.parse(headers);
    }
}
