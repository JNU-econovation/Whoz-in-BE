package com.whoz_in.api_query_jpa.feedback;

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
    private UUID id;

    private UUID memberId;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
