package com.whoz_in.main_api.shared.presentation.logging;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 Http 요청을 로깅함. <br>
 요청 바디를 로깅하기 위해 <br>
 {@link com.whoz_in.main_api.shared.presentation.RequestBodyCachingFilter} 이후로 등록
 **/
@Slf4j
@Component
@RequiredArgsConstructor
@Order(20) // TODO: 요청자 정보를 위해 시큐리티 필터 체인 지나도록 순위를 높여둠. 분리하면 원래대로 바꾸기
public class HttpRequestLoggingFilter extends OncePerRequestFilter {
    private final RequesterInfo requesterInfo;
    private final HttpRequestLogger httpRequestLogger;
    private final HttpRequestExceptionLogger httpRequestExceptionLogger;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            httpRequestLogger.log(request, getRequesterId()); // TODO: 요청자 정보 같은건 인터셉터로 빼기
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            httpRequestExceptionLogger.log("필터에서 예외 발생!", request, e);
            throw e;
        }
    }

    private String getRequesterId(){
        return requesterInfo.findMemberId()
                .map(MemberId::id)
                .map(Object::toString)
                .orElse("요청자 정보가 없음");
    }
}
