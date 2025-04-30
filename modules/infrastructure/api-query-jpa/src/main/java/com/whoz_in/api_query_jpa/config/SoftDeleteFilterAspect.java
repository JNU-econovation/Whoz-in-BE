package com.whoz_in.api_query_jpa.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;


@Aspect
@Component("querySoftDeleteFilterAspect")
@RequiredArgsConstructor
public class SoftDeleteFilterAspect {
    @PersistenceContext(unitName = "api_query_jpa")
    private final EntityManager entityManager;

    @Around("execution(* com.whoz_in.api_query_jpa..*(..)) && @annotation(com.whoz_in.shared_jpa.WithDeleted)")
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
