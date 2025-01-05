package com.whoz_in.api_query_jpa.device;

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
@Subselect("SELECT "
        + "device.id as deviceId, "
        + "device.memberId as onwer "
        + "FROM DeviceEntity device "
)
@Immutable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Device {

    @Id
    private UUID deviceId;
    private UUID memberId;

}
