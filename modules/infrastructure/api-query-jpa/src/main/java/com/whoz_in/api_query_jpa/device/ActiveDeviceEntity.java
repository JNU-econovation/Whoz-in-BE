package com.whoz_in.api_query_jpa.device;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO: JPA 적용
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActiveDeviceEntity {

    @Id
    private UUID deviceId;

    @Column(nullable = false)
    private LocalDateTime activeTime;

    private LocalDateTime inactiveTime;

    private Duration totalActiveTime;

    private boolean isActive;

    private ActiveDeviceEntity(UUID deviceId, LocalDateTime activeTime) {
        this.deviceId = deviceId;
        this.activeTime = activeTime;
    }

    public void activeOn(LocalDateTime activeTime){
        this.isActive = true;
        this.activeTime = activeTime;
    }

    public void inActiveOn(LocalDateTime inactiveTime){
        this.isActive = false;
        this.inactiveTime = inactiveTime;
        this.totalActiveTime = this.totalActiveTime.plus(Duration.between(this.activeTime, this.inactiveTime));
    }

    public static ActiveDeviceEntity create(UUID deviceId, LocalDateTime activeTime){
        return new ActiveDeviceEntity(deviceId, activeTime);
    }

}
