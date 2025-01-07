package com.whoz_in.api_query_jpa.device;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Getter
@Subselect("SELECT id , member_id FROM device_entity")
@Immutable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Device {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name ="member_id", nullable = false)
    private UUID memberId;

}
