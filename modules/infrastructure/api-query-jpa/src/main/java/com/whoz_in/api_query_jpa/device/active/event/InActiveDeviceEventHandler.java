package com.whoz_in.api_query_jpa.device.active.event;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.member.Member;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfo;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import com.whoz_in.api_query_jpa.shared.service.DeviceConnectionService;
import com.whoz_in.api_query_jpa.shared.util.ActiveTimeUpdateWriter;
import com.whoz_in.api_query_jpa.shared.util.InActiveMemberConnectionManager;
import com.whoz_in.main_api.shared.domain.device.active.event.InActiveDeviceFinded;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InActiveDeviceEventHandler {

    private final ActiveDeviceRepository activeDeviceRepository;
    private final InActiveMemberConnectionManager inActiveMemberConnectionManager;
    private final DeviceConnectionService deviceConnectionService;

    // ActiveDeviceEvent 핸들러에서 데이터를 수정할 수 있으므로 격리 수준을 serializable 로 설정
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @EventListener(InActiveDeviceFinded.class)
    public void handle(InActiveDeviceFinded event) {

        // 찾은 InActiveDevice 들을 처리하는 로직
        // Event 에 담기는 Device 의 ID 들은, 반드시 후즈인에 등록된 디바이들이다.
        List<UUID> devices = event.getDevices();
        List<ActiveDeviceEntity> inActives = activeDeviceRepository.findByDeviceIds(devices);

        // 기기 disConnect
        inActives.stream()
                .map(ActiveDeviceEntity::getDeviceId)
                .forEach(deviceConnectionService::disconnectDevice);
    }

}
