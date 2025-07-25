package com.whoz_in_infra.infra_jpa.query.member.activity;

import com.whoz_in_infra.infra_jpa.query.device.Device;
import com.whoz_in_infra.infra_jpa.query.device.DeviceRepository;
import com.whoz_in_infra.infra_jpa.query.device.connection.DeviceConnection;
import com.whoz_in_infra.infra_jpa.query.device.connection.DeviceConnectionRepository;
import com.whoz_in_infra.infra_jpa.shared.WithDeleted;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// 주체가 member이며 재실과 강하게 연결되어있으므로 activity 패키지에 위치
@Component
@RequiredArgsConstructor
public class MemberConnectionService {
    private final DeviceRepository deviceRepository;
    private final DeviceConnectionRepository deviceConnectionRepository;

    // 반환되는 Map의 key는 MemberId
    @Transactional(readOnly = true)
    @WithDeleted
    public Map<UUID, List<DeviceConnection>> getMemberConnections(LocalDateTime start, LocalDateTime end){
        // 범위 내의 연결들을 deviceId로 그룹화한다.
        Map<UUID, List<DeviceConnection>> deviceIdToConnections = deviceConnectionRepository.findByConnectedAtBetween(start, end).stream()
                .collect(Collectors.groupingBy(DeviceConnection::getDeviceId));
        // device들의 연결들을 소유자의 memberId로 매핑한다.
        return deviceRepository.findAllByIdIn(
                        deviceIdToConnections.keySet()).stream()
                .collect(Collectors.toMap(
                        Device::getMemberId, // memberId
                        device -> deviceIdToConnections.get(device.getId()), // 해당 deviceId의 연결 리스트
                        (list1, list2) -> { // memberId가 중복될 경우 병합
                            List<DeviceConnection> combined = new ArrayList<>(list1);
                            combined.addAll(list2);
                            return combined;
                        }
                ));
    }
}
