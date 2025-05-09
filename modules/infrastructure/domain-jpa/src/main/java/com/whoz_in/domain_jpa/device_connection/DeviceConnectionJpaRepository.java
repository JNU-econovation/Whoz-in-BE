package com.whoz_in.domain_jpa.device_connection;

import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.device_connection.DeviceConnection;
import com.whoz_in.domain.device_connection.DeviceConnectionRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeviceConnectionJpaRepository implements DeviceConnectionRepository {
    private final DeviceConnectionEntityRepository entityRepository;
    private final DeviceConnectionEntityConverter converter;

    @Override
    public void save(DeviceConnection connection) {
        entityRepository.save(converter.from(connection));
    }

    // 여러 연결 나오면 애플리케이션에서 connection 관리 잘못한거
    @Override
    public Optional<DeviceConnection> findConnectedByDeviceId(DeviceId id) {
        return entityRepository.findByDeviceIdAndDisconnectedAtIsNull(id.id())
                .map(converter::to);
    }

    @Override
    public List<DeviceConnection> findAllConnected() {
        return entityRepository.findByDisconnectedAtIsNull().stream()
                .map(converter::to)
                .toList();
    }
}
