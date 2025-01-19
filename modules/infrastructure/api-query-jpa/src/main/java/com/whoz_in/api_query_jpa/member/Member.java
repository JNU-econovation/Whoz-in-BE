package com.whoz_in.api_query_jpa.member;

import jakarta.persistence.Column;
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
        + "m.id,"
        + "m.login_id,"
        + "m.password,"
        + "m.generation,"
        + "m.name "
        + "FROM member_entity m")
@Synchronize({"member_entity"})
public class Member {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "password")
    private String encodedPassword;

    @Column(name = "generation")
    private int generation;
}
