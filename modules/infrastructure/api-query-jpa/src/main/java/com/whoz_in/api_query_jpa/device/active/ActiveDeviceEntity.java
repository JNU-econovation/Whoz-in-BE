package com.whoz_in.api_query_jpa.device.active;

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

    private LocalDateTime connectedAt;

    private LocalDateTime disConnectedAt;

    private ActiveDeviceEntity(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public void connect(LocalDateTime time){
        this.connectedAt = time;
        if(Objects.nonNull(disConnectedAt)) this.disConnectedAt=null;
    }

    public void disConnect(LocalDateTime time){
        this.disConnectedAt = time;
    }

    public boolean isActive(){
        // connectedAt 이 null 이 아니고, disConnectedAt 이 null 이면 active
        return Objects.nonNull(connectedAt) && Objects.isNull(disConnectedAt);
    }

    public Duration getContinuousTime(){
        if(Objects.isNull(connectedAt)) return Duration.ZERO;
        if(Objects.isNull(disConnectedAt)) return Duration.between(connectedAt, LocalDateTime.now()).abs();
        return Duration.between(connectedAt, disConnectedAt).abs();
    }

    public static ActiveDeviceEntity create(UUID deviceId){
        return new ActiveDeviceEntity(deviceId);
    }

}
