package com.whoz_in_infra.infra_jpa.query.member.activity.today;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import com.whoz_in_infra.infra_jpa.query.device.DeviceRepository;
import com.whoz_in_infra.infra_jpa.query.device.connection.DeviceConnection;
import com.whoz_in_infra.infra_jpa.query.device.connection.DeviceConnectionUtil;
import com.whoz_in_infra.infra_jpa.query.member.activity.MemberConnectionService;
import com.whoz_in.main_api.shared.utils.TimeRanges;
import com.whoz_in.shared.DayEnded;
import com.whoz_in.shared.DayEndedEventPublisher;
import com.whoz_in.shared.domain_event.device_connection.DeviceConnected;
import com.whoz_in.shared.domain_event.device_connection.DeviceDisconnected;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 *  원본 데이터인 {@link DeviceConnection}으로 하루 재실 기록을 계산할 수 있지만, <br>
 *  반복 계산이 비효율적이기 때문에 {@link TodayActivity}를 메모리 캐시로 제공하는 클래스
  */
/*
 TODO: 현재 클래스는 책임 분리가 안돼있음
       TodayActivity를 관리하는 CacheRepository(store?)와 해당 Repo를 이용해 관련 이벤트를 처리하는 서비스(?) 클래스로 분리할 것.
       이때 @Transactional을 사용할 수 없으므로 동시성 문제에 주의해야 함.
       Repository의 find들은 TodayActivity를 복사해서 반환하고 save를 제공할지,
       find가 현재처럼 원본 객체를 반환하되, read-only로만 사용하도록 약속하고 내부 상태 변경은 Repo의 메서드로만 허용할지 고려해볼 수 있을듯
*/
@Slf4j
@Component
@RequiredArgsConstructor
public class TodayActivityService {
    private final DeviceRepository deviceRepository;
    private final MemberConnectionService memberConnectionService;
    // Map<memberId, TodayActivity>
    private final Map<UUID, TodayActivity> todayActivityByMemberId = new ConcurrentHashMap<>();

    // 상태를 인메모리로 저장하기 때문에 서버 시작 시 초기 상태를 구성한다.
    @EventListener(ApplicationReadyEvent.class)
    private void init(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dayStartTime = now
                .minusHours(DayEndedEventPublisher.DAY_END_HOUR).toLocalDate()
                .atTime(DayEndedEventPublisher.DAY_END_HOUR, 0);

        memberConnectionService.getMemberConnections(dayStartTime, now).forEach((memberId, connections) -> {
            TimeRanges timeRanges = DeviceConnectionUtil.toTimeRanges(connections, now);
            List<UUID> connectedDeviceIds = connections.stream()
                    .filter(conn -> conn.getDisconnectedAt() == null)
                    .map(DeviceConnection::getDeviceId)
                    .distinct()
                    .toList();
            // 연결된 디바이스가 하나라도 있으면 재실 중으로 판단
            boolean isActive = !connectedDeviceIds.isEmpty();

            TodayActivity todayActivity = new TodayActivity(
                    memberId,
                    isActive ? timeRanges.getLastRangeStart() : null,
                    isActive ? null : timeRanges.getLastRangeEnd(),
                    isActive ? timeRanges.getDurationWithoutLastRange() : timeRanges.getTotalDuration(),
                    connectedDeviceIds
            );
            if (!todayActivity.isInactiveWithoutActiveTime())
                todayActivityByMemberId.put(memberId, todayActivity);
        });
    }

    // 모든 멤버의 상태 조회 (원본 객체 반환하니 수정 금지)
    public List<TodayActivity> findAll() {
        return todayActivityByMemberId.values().stream().toList();
    }
    // 한 멤버의 상태 조회 (원본 객체 반환하니 수정 금지)
    public Optional<TodayActivity> findOne(UUID memberId) {
        return Optional.ofNullable(todayActivityByMemberId.get(memberId));
    }


    // 새로운 하루가 시작되면 모든 상태 초기화
    @EventListener(DayEnded.class)
    private void clearOnDayEnded() {
        todayActivityByMemberId.clear();
    }

    // 디바이스 연결 시 해당 멤버의 재실 상태를 업데이트
    @TransactionalEventListener(phase = AFTER_COMMIT)
    private void updateOnDeviceConnected(DeviceConnected event) {
        UUID deviceId = event.getDeviceId();
        UUID memberId = getMemberId(deviceId);
        this.findOne(memberId)
                .ifPresentOrElse(
                        s -> s.connect(deviceId, event.getConnectedAt()),
                        () -> {
                            TodayActivity newActivity = new TodayActivity(memberId, event.getConnectedAt(), null, Duration.ZERO, deviceId);
                            todayActivityByMemberId.put(memberId, newActivity);
                        }
                );
    }

    // 연결이 끊겼을때 상태 업데이트
    @TransactionalEventListener(phase = AFTER_COMMIT)
    private void updatedOnDeviceDisconnected(DeviceDisconnected event) {
        UUID deviceId = event.getDeviceId();
        UUID memberId = getMemberId(deviceId);
        this.findOne(memberId)
                .ifPresent(
                        s -> {
                            s.disconnect(deviceId, event.getDisconnectedAt());
                            if (s.isInactiveWithoutActiveTime()) {
                                this.todayActivityByMemberId.remove(memberId);
                            }
                        }
                );
    }

    private UUID getMemberId(UUID deviceId) {
        return deviceRepository.findOneById(deviceId)
                .orElseThrow(()-> new IllegalStateException("기기 없음: " + deviceId))
                .getMemberId();
    }
}
