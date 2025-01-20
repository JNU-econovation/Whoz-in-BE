package com.whoz_in.api_query_jpa.device;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
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

    private Duration totalActiveTime;

    private boolean isActive;

    private ActiveDeviceEntity(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public void activeOn(LocalDateTime connectedTime){
        this.isActive = true;
        this.connectedTime = connectedTime;
        this.disConnectedTime = null;
    }

    public void inActiveOn(LocalDateTime disConnectedTime){
        this.isActive = false;
        this.disConnectedTime = disConnectedTime;
        addTotalActiveTime();
    }

    private void addTotalActiveTime(){
        Duration add = Duration.between(connectedTime, disConnectedTime).abs();
        this.totalActiveTime = Objects.nonNull(totalActiveTime) ? totalActiveTime.plus(add) :  add;
    }

    public static ActiveDeviceEntity create(UUID deviceId){
        return new ActiveDeviceEntity(deviceId);
    }

}
