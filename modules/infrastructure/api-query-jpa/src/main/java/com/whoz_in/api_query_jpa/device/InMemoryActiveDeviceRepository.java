package com.whoz_in.api_query_jpa.device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class InMemoryActiveDeviceRepository {

    private final Map<UUID, ActiveDeviceEntity> repository;

    public InMemoryActiveDeviceRepository(){
        this.repository = new ConcurrentHashMap<>();
    }

    public UUID save(ActiveDeviceEntity device){
        UUID id = device.getDeviceId();
        repository.put(id, device); // 갱신된 데이터가 삽입될 수 있도록
        return device.getDeviceId();
    }

    public void saveAll(List<ActiveDeviceEntity> devices){
        devices.forEach(this::save);
    }

    public List<ActiveDeviceEntity> findAll(){
        return repository.values().stream().toList();
    }

    public Optional<ActiveDeviceEntity> findByDeviceId(UUID deviceId){
        if(repository.containsKey(deviceId))
            return Optional.of(repository.get(deviceId));
        return Optional.empty();
    }

    public void deleteById(UUID deviceId){
        repository.remove(deviceId);
    }


}
