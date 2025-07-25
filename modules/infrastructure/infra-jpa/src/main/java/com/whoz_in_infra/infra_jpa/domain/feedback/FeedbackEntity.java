package com.whoz_in_infra.infra_jpa.domain.feedback;

import com.whoz_in_infra.infra_jpa.domain.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedbackEntity extends BaseEntity {
    @Id
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID memberId;

    public FeedbackEntity(UUID id, String title, String content, UUID memberId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }
}
