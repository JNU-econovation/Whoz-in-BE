package com.whoz_in.api_query_jpa.device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryActiveDeviceRepository {

    private final Map<UUID, ActiveDeviceEntity> repository;

    public InMemoryActiveDeviceRepository(){
        this.repository = new HashMap<>();
    }

    public UUID save(ActiveDeviceEntity device){
        UUID id = device.getDeviceId();
        repository.put(id, device);
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
