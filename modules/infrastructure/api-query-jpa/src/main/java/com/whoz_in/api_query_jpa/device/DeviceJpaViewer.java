package com.whoz_in.api_query_jpa.device;

import com.whoz_in.api_query_jpa.member.Member;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import com.whoz_in.main_api.query.device.application.DeviceOwner;
import com.whoz_in.main_api.query.device.application.DeviceCount;
import com.whoz_in.main_api.query.device.application.DevicesStatus;
import com.whoz_in.main_api.query.device.application.DevicesStatus.DeviceStatus;
import com.whoz_in.main_api.query.device.view.DeviceViewer;
import com.whoz_in.main_api.query.device.view.RegisteredSsids;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class DeviceJpaViewer implements DeviceViewer {
    private final DeviceRepository deviceRepository;
    private final MemberRepository memberRepository;
    private final DeviceInfoRepository deviceInfoRepository;
    @Override
    public RegisteredSsids findRegisteredSsids(UUID ownerId, String room, String mac) {
        return new RegisteredSsids(
                deviceInfoRepository.findAllByMac(ownerId, room, mac).stream()
                        .map(DeviceInfo::getSsid)
                        .toList());
    }

    @Transactional(readOnly = true)
    @Override
    public DevicesStatus findDevicesStatus(UUID ownerId) {
        List<DeviceStatus> devicesStatus = deviceRepository.findAllByMemberId(ownerId)
                .stream()
                .map(device -> new DeviceStatus(device.getId(), device.getName(), device.getDeviceInfos()
                        .stream()
                        .collect(Collectors.toMap(
                                DeviceInfo::getSsid,
                                DeviceInfo::getMac)),
                        null))//TODO: connected된 ssid는 기기 현황 구현 후 리팩토링하면서 구현하기
                .toList();
        return new DevicesStatus(devicesStatus);
    }

    @Override
    public DeviceOwner findDeviceOwner(UUID deviceId) {
        return memberRepository.findByDeviceId(deviceId)
                .map(Member::getId)
                .map(DeviceOwner::new)
                .orElse(null);
    }

    @Override
    public DeviceCount findDeviceCount(UUID ownerId) {
        List<Device> devices = deviceRepository.findAllByMemberId(ownerId);
        return new DeviceCount(devices.size());
    }

    @Override
    public List<DevicesStatus> findDevicesStatusAll(List<UUID> ownerIds) {
        return ownerIds.stream()
                .map(this::findDevicesStatus)
                .toList();
    }
}
