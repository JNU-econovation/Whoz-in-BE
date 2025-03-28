package com.whoz_in.api_query_jpa.feedback;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Subselect;

@Entity
@Getter
@Subselect("select * from feedback")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Feedback {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "member_id")
    private UUID memberId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
