package com.whoz_in_infra.infra_jpa.query.member.activity.today;

import com.whoz_in.shared.Nullable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

// 한 멤버에 대한 오늘의 재실 상태를 나타내는 클래스
@Slf4j
@Getter
public final class TodayActivity {
    private final UUID memberId;
    @Nullable private LocalDateTime activeAt;
    @Nullable private LocalDateTime inactiveAt;
    private Duration prevActiveTime;
    private final List<UUID> connectedDeviceIds;
    // active room 추가 가능

    // 새로운 DeviceConnection으로 만들때 사용
    public TodayActivity(
            UUID memberId,
            @Nullable LocalDateTime activeAt, @Nullable LocalDateTime inactiveAt,
            Duration prevActiveTime, UUID connectedDeviceId) {
        this(memberId, activeAt, inactiveAt, prevActiveTime, List.of(connectedDeviceId));
    }

    // 기존 DeviceConnection들로 만들때 사용
    public TodayActivity(
            UUID memberId,
            @Nullable LocalDateTime activeAt, @Nullable LocalDateTime inactiveAt,
            Duration prevActiveTime, List<UUID> connectedDeviceIds) {
        if (activeAt == null && inactiveAt == null) {
            throw new IllegalArgumentException("재실 시각과 퇴실 시각이 둘 다 null일 수 없음");
        }
        // 활성 상태와 연결된 디바이스 상태가 일치하는지 검증
        boolean hasConnectedDevices = !connectedDeviceIds.isEmpty();
        if ((activeAt == null) == hasConnectedDevices || (inactiveAt == null) != hasConnectedDevices) {
            throw new IllegalArgumentException("재실 상태와 연결된 디바이스 상태가 맞지 않음");
        }
        this.memberId = memberId;
        this.activeAt = activeAt;
        this.inactiveAt = inactiveAt;
        this.prevActiveTime = prevActiveTime;
        this.connectedDeviceIds = new ArrayList<>(connectedDeviceIds);
    }

    public boolean isActive() {
        return activeAt != null;
    }

    public void connect(UUID deviceId, LocalDateTime connectedAt) {
        if (!connectedDeviceIds.contains(deviceId)) {
            if (connectedDeviceIds.isEmpty()){
                activeAt = connectedAt;
                inactiveAt = null;
            }
            connectedDeviceIds.add(deviceId);
        }
    }

    public void disconnect(UUID deviceId, LocalDateTime disconnectedAt) {
        if (connectedDeviceIds.remove(deviceId) && connectedDeviceIds.isEmpty()) {
            prevActiveTime = prevActiveTime.plus(Duration.between(activeAt, disconnectedAt));
            activeAt = null;
            this.inactiveAt = disconnectedAt;
        }
    }

    // 정밀하지 않아도 되므로 여기서 LocalDateTime.now() 사용했음
    public Duration getContinuousActiveTime() {
        return isActive() ? Duration.between(activeAt, LocalDateTime.now()) : Duration.ZERO;
    }

    public Duration getActiveTime() {
        return prevActiveTime.plus(getContinuousActiveTime());
    }

    // 연결이 끊겼는데 활동 시간이 없을경우
    public boolean isInactiveWithoutActiveTime() {
        return !isActive() && getPrevActiveTime().isZero();
    }
}
