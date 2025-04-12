package com.whoz_in.api_query_jpa.member.activity;

import com.whoz_in.api_query_jpa.device.Device;
import com.whoz_in.api_query_jpa.device.DeviceRepository;
import com.whoz_in.api_query_jpa.device.connection.DeviceConnection;
import com.whoz_in.api_query_jpa.device.connection.DeviceConnectionRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// 주체가 member이며 재실과 강하게 연결되어있으므로 activity 패키지에 위치
@Component
@RequiredArgsConstructor
public class MemberConnectionService {
    private final DeviceRepository deviceRepository;
    private final DeviceConnectionRepository deviceConnectionRepository;

    // 멤버가 가진 device들의 연결들을 가져오는 메서드
    public Map<UUID, List<DeviceConnection>> get(LocalDateTime start, LocalDateTime end){
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
