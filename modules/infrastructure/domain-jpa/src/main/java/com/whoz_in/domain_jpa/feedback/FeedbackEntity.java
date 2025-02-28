package com.whoz_in.domain_jpa.feedback;

import com.whoz_in.domain_jpa.shared.BaseEntity;
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
    private String content;

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID memberId;

    public FeedbackEntity(UUID id, String content, UUID memberId) {
        this.id = id;
        this.content = content;
        this.memberId = memberId;
    }
}
