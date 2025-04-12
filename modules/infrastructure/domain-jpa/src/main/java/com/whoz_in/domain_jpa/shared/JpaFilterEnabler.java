package com.whoz_in.domain_jpa.shared;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
public class JpaFilterEnabler {
    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void enableSoftDeleteFilter() {
        entityManager.unwrap(Session.class)
                .enableFilter("softDeleteFilter")
                .setParameter("enabled", true);
    }
}
