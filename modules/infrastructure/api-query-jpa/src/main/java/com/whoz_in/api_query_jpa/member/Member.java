package com.whoz_in.api_query_jpa.member;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Entity
@Immutable
@Subselect("SELECT "
        + "m.id AS id, "
        + "m.login_id AS loginId, "
        + "m.password AS encodedPassword "
        + "FROM member_entity m")
@Synchronize({"member_entity"})
public class Member {
    @Id
    @UuidGenerator
    private UUID id;
    private String loginId;
    private String encodedPassword;
}
