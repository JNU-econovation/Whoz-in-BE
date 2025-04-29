package com.whoz_in.domain_jpa.shared;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;


/**
 * 레포지토리 구현체의 메서드 중 {@link WithDeleted}가 붙어있으면 SoftDelete 필터를 꺼서 삭제된 것까지 조회하도록 함
 */
@Aspect
@Component
@RequiredArgsConstructor
public class SoftDeleteFilterAspect {
    private final EntityManager entityManager;

    @Around("@annotation(com.whoz_in.domain_jpa.shared.WithDeleted)")
    public Object aroundWithDeleted(ProceedingJoinPoint joinPoint) throws Throwable {
        Session session = entityManager.unwrap(Session.class);

        Filter filter = session.getEnabledFilter("softDeleteFilter");
        boolean wasFilterEnabled = (filter != null);

        try {
            if (wasFilterEnabled) {
                session.disableFilter("softDeleteFilter");
            }
            return joinPoint.proceed();
        } finally {
            if (wasFilterEnabled) {
                session.enableFilter("softDeleteFilter");
            }
        }
    }
}
