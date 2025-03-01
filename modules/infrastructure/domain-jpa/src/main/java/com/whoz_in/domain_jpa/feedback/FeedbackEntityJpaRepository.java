package com.whoz_in.domain_jpa.feedback;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackEntityJpaRepository extends JpaRepository<FeedbackEntity, UUID> {
}
