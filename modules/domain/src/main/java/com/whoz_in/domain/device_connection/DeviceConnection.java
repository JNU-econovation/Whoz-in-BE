package com.whoz_in.domain.device_connection;

import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.shared.AggregateRoot;
import com.whoz_in.shared.Nullable;
import com.whoz_in.shared.domain_event.device_connection.DeviceConnected;
import com.whoz_in.shared.domain_event.device_connection.DeviceDisconnected;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

// 의미적으론 Device가 어느 방에서 언제부터 언제까지 연결되었는가를 나타낸다.
// 기능적으론 Member의 재실 시간을 제공한다.
// 따라서 Device와 삭제되었더라도 Member가 존재한다면 DeviceConnection은 사라지지 않아야 한다.
@Getter
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeviceConnection extends AggregateRoot {
    private final DeviceConnectionId id;
    private final DeviceId deviceId;
    private final String room;
    private final LocalDateTime connectedAt;
    @Nullable private LocalDateTime disconnectedAt;

    public static DeviceConnection create(DeviceId deviceId, String room, LocalDateTime connectedAt) {
        DeviceConnectionId id = new DeviceConnectionId();
        DeviceConnection deviceConnection = new DeviceConnection(id, deviceId, room, connectedAt);
        deviceConnection.register(
                new DeviceConnected(id.id(), deviceId.id(), room, connectedAt));
        return deviceConnection;
    }

    public static DeviceConnection load(DeviceConnectionId id, DeviceId deviceId, String room, LocalDateTime connectedAt, LocalDateTime disconnectedAt) {
        return new DeviceConnection(id, deviceId, room, connectedAt, disconnectedAt);
    }

    public boolean isConnectedIn(String room) {
        return !isDisconnected() && this.room.equals(room);
    }

    public boolean isConnected() {
        return this.disconnectedAt == null;
    }
    public boolean isDisconnected() {
        return !isConnected();
    }

    public void disconnect(@NonNull LocalDateTime at) { // 같은 방에 있는지도 검증해야 하나
        if (isDisconnected()) throw DeviceAlreadyDisconnectedException.EXCEPTION;
        this.disconnectedAt = at;
        this.register(new DeviceDisconnected(id.id(), deviceId.id(), room, connectedAt, disconnectedAt));
    }

    public Duration getConnectedDuration() {
        return Duration.between(connectedAt, isDisconnected() ? disconnectedAt : LocalDateTime.now());
    }
}
