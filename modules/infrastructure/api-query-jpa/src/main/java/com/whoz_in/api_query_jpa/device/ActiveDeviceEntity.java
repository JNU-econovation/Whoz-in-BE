package com.whoz_in.api_query_jpa.device;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActiveDeviceEntity {

    @Id
    private UUID deviceId;

    private LocalDateTime connectedTime;

    private LocalDateTime disConnectedTime;

    private ActiveDeviceEntity(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public static ActiveDeviceEntity create(UUID deviceId){
        return new ActiveDeviceEntity(deviceId);
    }

}
