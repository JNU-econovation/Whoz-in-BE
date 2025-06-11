package com.whoz_in_infra.infra_jpa.domain.feedback;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackEntityJpaRepository extends JpaRepository<FeedbackEntity, UUID> {
}
