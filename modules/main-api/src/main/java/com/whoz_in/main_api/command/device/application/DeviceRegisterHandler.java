package com.whoz_in.main_api.command.device.application;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceInfo;
import com.whoz_in.domain.device.model.MacAddress;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.member.service.MemberFinderService;
import com.whoz_in.domain.shared.event.EventBus;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.config.RoomSsidConfig;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.caching.device.TempDeviceInfo;
import com.whoz_in.main_api.shared.caching.device.TempDeviceInfoStore;
import com.whoz_in.main_api.shared.utils.RequesterInfo;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@Handler
@RequiredArgsConstructor
public class DeviceRegisterHandler implements CommandHandler<DeviceRegister, Void> {
    private final RequesterInfo requesterInfo;
    private final RoomSsidConfig ssidConfig;
    private final TempDeviceInfoStore tempDeviceInfoStore;
    private final MemberFinderService memberFinderService;
    private final DeviceRepository deviceRepository;
    private final EventBus eventBus;

    @Transactional
    @Override
    public Void handle(DeviceRegister cmd) {
        MemberId requesterId = requesterInfo.getMemberId();

        // 사용자가 존재하는지 검증
        memberFinderService.mustExist(requesterId);

        // 등록된 Device 가져오기
        Optional<Device> optionalDevice = cmd.getDeviceId().map(deviceRepository::getByDeviceId);

        // 등록된 Device가 있으면 등록된 SSID를 가져오고 없으면 빈 리스트 반환
        List<String> registeredSsids = optionalDevice.map(this::getRegisteredSsids).orElseGet(List::of);
        // 모든 와이파이에 대해 이미 등록되어있으면 리턴
        if (registeredSsids.equals(ssidConfig.getSsids())) return null;
        // TempDeviceInfo에 등록된 SSID 가져오기
        List<String> addedSsids = getTempDeviceInfoSsids(requesterId);
        // 추가되지 않은 SSID는 없어야 함
        validateAllSsidsAdded(registeredSsids, addedSsids);

        // TempDeviceInfo로부터 DeviceInfo 생성
        Set<DeviceInfo> deviceInfos = createDeviceInfos(requesterId);

        // Device를 생성 후 저장 (기존 Device가 없을 때만)
        Device device = optionalDevice
                .map(d->{
                    d.registerDeviceInfo(deviceInfos);return d;
                })
                .orElseGet(() -> Device.create(requesterId, deviceInfos, cmd.deviceName()));

        deviceRepository.save(device);

        eventBus.publish(device.pullDomainEvents());
        return null;
    }

    private List<String> getRegisteredSsids(Device device){
        return device.getDeviceInfos()
                .stream()
                .map(DeviceInfo::getSsid)
                .toList();
    }

    private List<String> getTempDeviceInfoSsids(MemberId requesterId) {
        return tempDeviceInfoStore.get(requesterId.id())
                .stream()
                .map(TempDeviceInfo::getSsid)
                .toList();
    }

    private void validateAllSsidsAdded(List<String> registeredSsids, List<String> addedSsids) {
        List<String> notAddedSsids = ssidConfig.getSsids().stream()
                .filter(ssid -> !registeredSsids.contains(ssid))
                .filter(ssid -> !addedSsids.contains(ssid))
                .toList();

        if (!notAddedSsids.isEmpty()) {
            throw new IllegalArgumentException("등록하지 않은 SSID: " + notAddedSsids);
        }
    }

    private Set<DeviceInfo> createDeviceInfos(MemberId requesterId) {
        return tempDeviceInfoStore.takeout(requesterId.id())
                .stream()
                .map(tempDI -> DeviceInfo.create(tempDI.getRoom(), tempDI.getSsid(), MacAddress.create(tempDI.getMac())))
                .collect(Collectors.toSet());
    }
}
