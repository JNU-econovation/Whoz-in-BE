package com.whoz_in.main_api.command.device.application;

import com.whoz_in.domain.device.model.DeviceId;
import com.whoz_in.domain.shared.Nullable;
import com.whoz_in.main_api.command.shared.application.Command;
import java.util.Optional;
import java.util.UUID;

//사용자가 와이파이마다 맥을 모두 추가했을 경우 그 맥들을 저장한다. (DeviceInfo)
public record DeviceRegister(
        @Nullable UUID deviceId, //이전에 등록된 기기인 경우 - Device를 불러옴
        @Nullable String deviceName //새로 등록하는 경우 - 기기 이름이 필요함
) implements Command {

    public DeviceRegister {
        if ((deviceId == null) == (deviceName == null))
            throw new IllegalArgumentException("device id, device name 중 하나를 보내야 함");
    }

    public Optional<DeviceId> getDeviceId(){
        return Optional.ofNullable(deviceId).map(DeviceId::new);
    }
}
