package com.whoz_in.api_query_jpa.shared.service;

import com.whoz_in.api_query_jpa.device.DeviceRepository;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceConnectionService {

    private final ActiveDeviceRepository activeDeviceRepository;
    private final MemberConnectionInfoRepository connectionInfoRepository;
    private final DeviceRepository deviceRepository;
    private final MemberRepository memberRepository;
    private final DeviceService deviceService;
    private final MemberConnectionService memberConnectionService;
    /**
     * 연결이 끊기면 (inActive 상태가 되면)
     * 1. 사용자의 마지막 기기인지 판단.
     *   a) 마지막 기기일 경우, inActive 처리 하고 continuousTime 을 memberConnectionInfo 에 더한다.
     *   b) 마지막 기기가 아닐 경우, ActiveDevice 만 inActive 한다.
     * @param deviceId
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void disconnectDevice(UUID deviceId, LocalDateTime disconnectedAt) {
        boolean isActive = deviceService.isActive(deviceId);
        boolean isLastDevice = deviceService.isLastConnectedDevice(deviceId);
        boolean onlyOne = onlyOne(deviceId);

        if(isActive) {
            Optional<ActiveDeviceEntity> activeDevice = activeDeviceRepository.findByDeviceId(deviceId);
            activeDevice.ifPresent(ad -> {
                log.info("disconnect (deviceId) : {}", ad.getDeviceId());
                ad.disConnect(disconnectedAt);
                activeDeviceRepository.save(ad);
            });
        }

        if(isLastDevice){
            Optional<ActiveDeviceEntity> activeDevice = activeDeviceRepository.findByDeviceId(deviceId);
            Optional<UUID> ownerId = deviceService.findDeviceOwner(deviceId);

            ownerId.ifPresent(id -> memberConnectionService.disconnectMember(id, disconnectedAt));
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void connectDevice(UUID deviceId, LocalDateTime connectedAt) {
        boolean isActive = deviceService.isActive(deviceId);

        if(!isActive){
            Optional<ActiveDeviceEntity> activeDevice = activeDeviceRepository.findByDeviceId(deviceId);
            activeDevice.ifPresent(ad -> {
                log.info("connect (deviceId) : {}", ad.getDeviceId());
                ad.connect(connectedAt);
                activeDeviceRepository.save(ad);
            });
        }
    }

    /**
     * 해당 디바이스가 멤버의 유일한 액티브 디바이스인지 확인
     * @param deviceId 확인할 디바이스 ID
     * @return 유일한 액티브 디바이스인 경우 true, 그렇지 않은 경우 false
     */
    private boolean onlyOne(UUID deviceId) {
        int isOne = deviceService.countActiveDevices(deviceId);
        return isOne == 1;
    }

}
