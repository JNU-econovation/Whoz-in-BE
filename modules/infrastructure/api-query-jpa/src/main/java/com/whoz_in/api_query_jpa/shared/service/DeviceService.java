package com.whoz_in.api_query_jpa.shared.service;

import com.whoz_in.api_query_jpa.device.Device;
import com.whoz_in.api_query_jpa.device.DeviceInfo;
import com.whoz_in.api_query_jpa.device.DeviceRepository;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceEntity;
import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.member.Member;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import com.whoz_in.api_query_jpa.monitor.MonitorLog;
import com.whoz_in.api_query_jpa.monitor.MonitorLogRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final MemberConnectionInfoRepository connectionInfoRepository;
    private final ActiveDeviceRepository activeDeviceRepository;
    private final MemberRepository memberRepository;
    private final MonitorLogRepository monitorLogRepository;


    /**
     *  해당 기기가 사용자의 마지막 기기인지 판단.
     *
     *  어떻게 판단하는가? 파라미터로 전달 받은 device 이외의 다른 device 가 active 상태인지 아닌지 판단.
     *  파라미터로 전달 받은 기기는 active 상태임을 가정.
     *
     *
     * @param deviceId
     * @return
     */
    public boolean isLastConnectedDevice(UUID deviceId) {
//        Member owner = memberRepository.getByDeviceId(deviceId);

ActiveDeviceEntity ad = activeDeviceRepository.findByDeviceId(deviceId)
        .orElseThrow(() -> new IllegalStateException("Device not found with ID: " + deviceId));
Member owner = memberRepository.findById(ad.getMemberId())
        .orElseThrow(() -> new IllegalStateException("Member not found with ID: " + ad.getMemberId()));

        List<ActiveDeviceEntity> ownerDevices = activeDeviceRepository.findByMemberId(owner.getId());

        List<ActiveDeviceEntity> active = ownerDevices.stream()
                .filter(ActiveDeviceEntity::isActive)
                .toList();

        if(active.size()>1) return false; // active 상태인 기기가 2개 이상일 경우

        else { // active 상태인 기기가 1개일 경우

            // active 상태인 기기가, 전달 받은 기기가 아닐 경우
            if (active.stream().anyMatch(activeDevice -> !activeDevice.getDeviceId().equals(deviceId)))
                return false;

            // active 상태인 기기가, 전달 받은 기기일 경우
            return true;
        }
    }

    public int countActiveDevices(UUID deviceId) {
        Optional<Member> member = memberRepository.findByDeviceId(deviceId);
        if(member.isPresent()) {
            UUID memberId = member.get().getId();
            return activeDeviceRepository.countByMemberId(memberId);
        }
        return 0;
    }

    public Optional<ActiveDeviceEntity> findLastConnectedDevice(UUID memberId) {
        List<ActiveDeviceEntity> activeDevices =
                activeDeviceRepository.findByMemberId(memberId);

        return activeDevices.stream()
                .max((ad1, ad2) -> ad1.getDisConnectedAt().isAfter(ad2.getDisConnectedAt()) ? 1 : -1);
    }

    public boolean isActive(UUID deviceId) {
        ActiveDeviceEntity activeDevice = activeDeviceRepository.findByDeviceId(deviceId).get(); // 기기 등록은 되었지만, ActiveDeviceEntity 가 없다면 에러 발생함
        return activeDevice.isActive();
    }

    public Optional<UUID> findDeviceOwner(UUID deviceId) {
        return memberRepository.findByDeviceId(deviceId)
                .map(Member::getId);
    }

    public MonitorLog findLatestMonitorLogAt(UUID deviceId) {
        Device device = deviceRepository.findById(deviceId).get();
        List<DeviceInfo> deviceInfos = device.getDeviceInfos();

        List<String> macs = deviceInfos.stream()
                .map(DeviceInfo::getMac)
                .toList();

        List<MonitorLog> logs = macs.stream().map(monitorLogRepository::findLatestByMac).toList();

        return logs.stream()
                .max((log1, log2) -> log1.getUpdatedAt().isAfter(log2.getUpdatedAt()) ? 1 : -1)
                .orElse(null);
    }

}
