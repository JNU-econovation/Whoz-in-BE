package com.whoz_in.api_query_jpa.shared.service;

import com.whoz_in.api_query_jpa.device.DeviceRepository;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfo;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionService {

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
    @Transactional
    public void disconnectDevice(UUID deviceId) {
        boolean isActive = deviceService.isActive(deviceId);
        boolean isLastDevice = deviceService.isLastConnectedDevice(deviceId);

        if(isActive) {
            Optional<ActiveDeviceEntity> activeDevice = activeDeviceRepository.findByDeviceId(deviceId);
            activeDevice.ifPresent(ad -> ad.disConnect(LocalDateTime.now()));
        }

        if(isLastDevice){
            Optional<ActiveDeviceEntity> activeDevice = activeDeviceRepository.findByDeviceId(deviceId);
            Optional<UUID> ownerId = deviceService.findDeviceOwner(deviceId);

            ownerId.ifPresent(memberConnectionService::disconnectMember);
        }
    }

}
