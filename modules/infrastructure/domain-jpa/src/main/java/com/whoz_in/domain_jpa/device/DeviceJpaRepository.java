package com.whoz_in.domain_jpa.device;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.member.model.MemberId;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeviceJpaRepository implements DeviceRepository {
    private final DeviceEntityRepository repository;
    private final DeviceEntityConverter converter;
    @Override
    public void save(Device device) {
        repository.save(converter.from(device));
    }

    @Override
    public boolean delete(DeviceId deviceId) {
        int result = repository.deleteById(deviceId.id());
        return result > 0;
    }

    @Override
    public List<Device> findByMemberId(MemberId ownerId) {
        return repository.findAllByMemberId(ownerId.id()).stream().map(converter::to).toList();
    }

    @Override
    public Optional<Device> findByMac(String mac) {
        return repository.findByMac(mac).map(converter::to);
    }

    @Override
    public Optional<Device> findByDeviceId(DeviceId deviceId) {
        return repository.findById(deviceId.id()).map(converter::to);
    }

    @Override
    public List<Device> findByMacs(Set<String> macs) {
        return repository.findByMacs(macs).stream().map(converter::to).toList();
    }

    @Override
    public List<Device> findByDeviceIds(List<DeviceId> deviceIds) {
        List<UUID> ids = deviceIds.stream().map(DeviceId::id).toList();
        return repository.findByDeviceIds(ids).stream().map(converter::to).toList();
    }
}
