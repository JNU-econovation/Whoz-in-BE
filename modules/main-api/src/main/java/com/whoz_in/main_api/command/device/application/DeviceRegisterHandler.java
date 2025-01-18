package com.whoz_in.main_api.command.device.application;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceInfo;
import com.whoz_in.domain.device.model.MacAddress;
import com.whoz_in.domain.device.service.DeviceFinderService;
import com.whoz_in.domain.device.service.DeviceOwnershipService;
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
    private final DeviceOwnershipService deviceOwnershipService;
    private final DeviceFinderService deviceFinderService;
    private final DeviceRepository deviceRepository;
    private final EventBus eventBus;

    @Transactional
    @Override
    public Void handle(DeviceRegister cmd) {
        MemberId requesterId = requesterInfo.getMemberId();
        memberFinderService.mustExist(requesterId);

        // 등록된 Device 가져오기
        Optional<Device> optionalDevice = cmd.getDeviceId().map(deviceFinderService::find);
        // 내꺼인지 검증
        optionalDevice.ifPresent(device -> deviceOwnershipService.validateIsMine(device, requesterId));

        // 등록된 Device가 있으면 기존에 등록한 와이파이들을 가져옴. 없으면 빈 리스트 반환
        List<String> registeredSsids = optionalDevice.map(this::getRegisteredSsids).orElseGet(List::of);
        // 모든 와이파이에 대해 이미 등록되어있으면 리턴
        if (registeredSsids.equals(ssidConfig.getSsids())) return null;
        // TempDeviceInfo에 추가된 와이파이 가져오기
        List<String> addedSsids = getTempDeviceInfoSsids(requesterId);
        // 추가되지 않은 와이파이가 있으면 예외
        validateAllSsidsAdded(registeredSsids, addedSsids);

        // TempDeviceInfo로부터 DeviceInfo 생성
        Set<DeviceInfo> deviceInfos = createDeviceInfos(requesterId);

        // 기존 Device 사용함. 없으면 생성함
        Device device = optionalDevice
                .map(d->{
                    //기존 Device에 DeviceInfo 추가
                    deviceInfos.forEach(d::registerDeviceInfo);
                    return d;
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
