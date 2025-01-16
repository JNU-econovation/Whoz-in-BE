package com.whoz_in.api_query_jpa.device;

import com.whoz_in.api_query_jpa.member.Member;
import com.whoz_in.api_query_jpa.member.MemberRepository;
import com.whoz_in.main_api.query.device.application.active.ActiveDevice;
import com.whoz_in.main_api.query.device.application.active.ActiveDeviceViewer;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActiveDeviceJpaViewer implements ActiveDeviceViewer {

    private final ActiveDeviceRepository activeDeviceRepository;
    private final DeviceRepository deviceRepository;
    private final MemberRepository memberRepository;

    @Override
    public Optional<ActiveDevice> findByDeviceId(String deviceId) {
        ActiveDeviceEntity activeDevice = activeDeviceRepository.findByDeviceId(UUID.fromString(deviceId)).orElse(null);
        Member member = memberRepository.findByDeviceId(UUID.fromString(deviceId)).orElse(null);

        if(member == null || activeDevice == null) return Optional.empty();

        return createOptionalActiveDevice(activeDevice, member);
    }

    @Override
    public List<ActiveDevice> findAll() {
        List<ActiveDeviceEntity> entities = activeDeviceRepository.findAll();
        List<UUID> deviceIds = entities.stream().map(ActiveDeviceEntity::getDeviceId).toList();
        List<Device> devices = deviceRepository.findAll();
        List<Member> members = memberRepository.findByDeviceIds(deviceIds);

        return entities.stream()
                .map(entity-> {
                    Device device = devices.stream().filter(d -> d.getId().equals(entity.getDeviceId())).findFirst().orElse(null);
                    if(device == null) return null;
                    Member member = members.stream().filter(m -> m.getId().equals(device.getMemberId())).findFirst().orElse(null);
                    if (member == null) return null;
                    return createOptionalActiveDevice(entity, member).orElse(null);
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private Optional<ActiveDevice> createOptionalActiveDevice(ActiveDeviceEntity entity, Member member){
        return Optional.of(new ActiveDevice(
                entity.getDeviceId(),
                member.getId(),
                entity.getConnectedTime(),
                entity.getDisConnectedTime(),
                entity.getTotalActiveTime(),
                entity.isActive()));
    }
}
