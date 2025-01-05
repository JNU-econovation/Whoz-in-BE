package com.whoz_in.api_query_jpa.device;

import com.whoz_in.domain.device.model.DeviceId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Duration;
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

    @Column(nullable = false)
    private LocalDateTime activeTime;

    private LocalDateTime inactiveTime;

    private Duration totalActiveTime;

    private ActiveDeviceEntity(UUID deviceId, LocalDateTime activeTime) {
        this.deviceId = deviceId;
        this.activeTime = activeTime;
    }

    public void activeOn(LocalDateTime activeTime){
        this.activeTime = activeTime;
    }

    public void isActiveOn(LocalDateTime inactiveTime){
        this.inactiveTime = inactiveTime;
        this.totalActiveTime = Duration.between(this.activeTime, this.inactiveTime);
    }

    public static ActiveDeviceEntity create(DeviceId deviceId, LocalDateTime activeTime){
        return new ActiveDeviceEntity(deviceId.id(), activeTime);
    }

}
