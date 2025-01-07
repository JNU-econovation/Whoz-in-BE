package com.whoz_in.main_api.shared.domain.device.active;

import com.whoz_in.domain.device.model.Device;
import java.util.List;

// Device 들을 받으면, 정해진 기준에 맞춰 거르는 역할이라는 뜻
public abstract class DeviceFilter {

    public List<Device> execute(List<Device> devices){
        return devices.stream()
                .filter(this::judge)
                .toList();
    }

    public abstract boolean judge(Device device);

}
