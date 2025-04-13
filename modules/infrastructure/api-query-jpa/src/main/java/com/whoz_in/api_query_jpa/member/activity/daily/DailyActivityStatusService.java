package com.whoz_in.api_query_jpa.member.activity.daily;

import static com.whoz_in.shared.DayEndedEventPublisher.DAY_END_HOUR;

import com.whoz_in.api_query_jpa.device.DeviceRepository;
import com.whoz_in.api_query_jpa.device.connection.DeviceConnection;
import com.whoz_in.api_query_jpa.device.connection.DeviceConnectionUtil;
import com.whoz_in.api_query_jpa.member.activity.MemberConnectionService;
import com.whoz_in.main_api.shared.utils.TimeRanges;
import com.whoz_in.shared.DayEnded;
import com.whoz_in.shared.domain_event.device_connection.DeviceConnected;
import com.whoz_in.shared.domain_event.device_connection.DeviceDisconnected;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// DailyActivityStatus를 제공하는 클래스
@Slf4j
@Component
@RequiredArgsConstructor
public class DailyActivityStatusService {
    private final DeviceRepository deviceRepository;
    private final MemberConnectionService memberConnectionService;
    private final Map<UUID, DailyActivityStatus> memberIdToStatus = new HashMap<>();

    // 상태를 인메모리로 저장하기 때문에 서버 시작 시 초기 상태를 구성한다.
    @PostConstruct
    private void init(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dayStartTime = now
                .minusHours(DAY_END_HOUR).toLocalDate()
                .atTime(DAY_END_HOUR, 0);

        memberConnectionService.get(dayStartTime, now).forEach((memberId, connections) -> {
            // 현재 연결 중인 디바이스 목록: 연결이 끊기지 않은 deviceId들 (중복 제거)
            List<UUID> connectedDeviceIds = connections.stream()
                    .filter(conn -> conn.getDisconnectedAt() == null)
                    .map(DeviceConnection::getDeviceId)
                    .distinct()
                    .toList();
            TimeRanges timeRanges = DeviceConnectionUtil.toTimeRanges(connections, now);
            // 연결된 디바이스가 하나라도 있으면 재실 중으로 판단
            boolean isActive = !connectedDeviceIds.isEmpty();

            DailyActivityStatus status = new DailyActivityStatus(
                    memberId,
                    isActive ? timeRanges.getLastRangeStart() : null,
                    isActive ? null : timeRanges.getLastRangeEnd(),
                    isActive ? timeRanges.getDurationWithoutLastRange() : timeRanges.getTotalDuration(),
                    connectedDeviceIds
            );
            memberIdToStatus.put(memberId, status);
        });
    }

    // 모든 멤버의 상태 조회
    public Map<UUID, DailyActivityStatus> getMap() {
        return memberIdToStatus;
    }
    public List<DailyActivityStatus> getList() {
        return memberIdToStatus.values().stream().toList();
    }

    // 한 멤버의 상태 조회
    public Optional<DailyActivityStatus> get(UUID memberId) {
        return Optional.ofNullable(memberIdToStatus.get(memberId));
    }


    // 새로운 하루가 시작되면 모든 상태 초기화
    @EventListener(DayEnded.class)
    private void clearOnDayEnded() {
        memberIdToStatus.clear();
    }

    // 연결이 생겼을때 상태 업데이트
    @EventListener(DeviceConnected.class)
    private void updateOnDeviceConnected(DeviceConnected event) {
        UUID deviceId = event.getDeviceId();
        UUID memberId = getMemberId(deviceId);
        this.get(memberId)
                .ifPresentOrElse(
                        s -> s.connect(deviceId, event.getConnectedAt()),
                        () -> {
                            DailyActivityStatus newStatus = new DailyActivityStatus(memberId, event.getConnectedAt(), null, Duration.ZERO, deviceId);
                            memberIdToStatus.put(memberId, newStatus);
                        }
                );
    }

    // 연결이 끊겼을때 상태 업데이트
    @EventListener(DeviceDisconnected.class)
    private void updatedOnDeviceDisconnected(DeviceDisconnected event) {
        UUID deviceId = event.getDeviceId();
        UUID memberId = getMemberId(deviceId);
        this.get(memberId)
                .ifPresent(
                        s -> s.disconnect(deviceId, event.getDisconnectedAt())
                );
    }

    private UUID getMemberId(UUID deviceId) {
        return deviceRepository.findById(deviceId)
                .orElseThrow(()-> new IllegalStateException("기기 없음: " + deviceId))
                .getMemberId();
    }
}
