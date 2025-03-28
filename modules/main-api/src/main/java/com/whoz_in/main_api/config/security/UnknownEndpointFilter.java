package com.whoz_in.main_api.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/*
시큐리티는 존재하지 않는 엔드포인트 api에 대해서 403을 보내지만, 404를 보내도록 하려고 했다.
이를 위해 시큐리티에 명시되지 않은 엔드포인트는 모두 허용하여 요청을 DispatcherServlet로 보내서 404를 반환할 수는 있지만
이 방법은 개발자가 실수로 특정 엔드포인트를 시큐리티에 명시하지 않았는데 컨트롤러엔 해당 엔드포인트가 존재하여 실행될 수 있는 위험이 있음
따라서 시큐리티가 403을 띄우기 전에 미리 처리할 수 있는 엔드포인트인지 이 필터에서 확인하고 처리할 수 없다면 404를 반환한다.
시큐리티와 관련 없이 서블릿 필터로 둬도 잘 동작할테지만 시큐리티의 제약으로 인해 구현하게 됐으니 시큐리티의 일부로 구성한다.
엔드포인트가 존재하든 말든 404를 반환하기 때문에 우선순위가 제일 낮은 시큐리티 필터 체인에 둬야 함!!
*/
@Slf4j
@Component
@RequiredArgsConstructor
public class UnknownEndpointFilter extends OncePerRequestFilter {
    private final RequestMappingHandlerMapping mapping;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            if (mapping.getHandler(request) != null) {
                log.warn("{}는 존재하지만 시큐리티 필터 체인에 등록되지 않은 엔드포인트입니다.", request.getRequestURI());
            }
        } catch (HttpRequestMethodNotSupportedException e) {
            log.warn("{}는 {}를 지원하지 않음", request.getRequestURI(), request.getMethod());
            response405(response);
        } catch (Exception e) {
            throw new IllegalStateException("엔드포인트 확인 중 오류 발생", e);
        } finally {
            response404(response); // 핸들러가 있든 없든 실패하든 말든 프론트로 404 반환
        }
    }

    private void response404(HttpServletResponse response) throws IOException {
        // 엔드포인트가 존재하지 않으면 404 반환
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("{\"message\": \"존재하지 않는 엔드포인트입니다.\"}");
        response.getWriter().flush();
        response.getWriter().close();
    }
    private void response405(HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.getWriter().write("{\"message\": \"이 엔드포인트는 해당 메소드를 지원하지 않습니다.\"}");
        response.getWriter().flush();
        response.getWriter().close();
    }
}
