package com.whoz_in.main_api.shared.presentation.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogBodyAspect {
    private final HttpRequestInfoExtractor extractor;

    @After("@annotation(com.whoz_in.main_api.shared.presentation.logging.LogBody)")
    public void logRequestBody() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return;
        log.info("[REQUEST BODY]\n{}", extractor.extractRequestBody(attributes.getRequest()));
    }
}
